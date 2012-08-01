/*
 * Copyright (C) 2012  Christian Autermann, Dustin Demuth, Maurin Radtke
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package de.ifgi.fmt.web.filter.auth;

import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import de.ifgi.fmt.Service;
import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.RESTConstants.Headers;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
public class Authorization implements ContainerResponseFilter,
		ContainerRequestFilter {
	private static final Logger log = LoggerFactory
			.getLogger(Authorization.class);
	public static final String COOKIE_NAME = "fmt_oid";
	private static final String USER_SESSION_ATTRIBUTE = "user";
	private static final String AUTH_TYPE_SESSION_ATTRIBUTE = "authType";
	private static final String REMOVE_COOKIE_SESSION_ATTRIBUTE = "remove-cookie";
	
	private final SecureRandom random = new SecureRandom();
	private @Context
	HttpServletRequest sr;
	private @Context
	UriInfo uri;

	private enum Auth {
		HEADER, COOKIE, HTTP_BASIC;
	}
	
	protected User tokenAuth(String token) {
		if (token == null)  return null;
		User u = Service.getInstance().getStore().users().getByAuthToken(token);
		if (u == null) {
			log.warn("No user with this auth token!");
			throw ServiceError.forbidden("invalid auth token");
		}
		return u;
	}
	
	protected User cookieAuth(Cookie cookie) {
		User u = null;
		if (cookie != null) {
			u = tokenAuth(cookie.getValue());
		}
		return u;
	}
	
	protected User httpBasicAuth(String headerValue) {
		User u = null;
		if (headerValue != null) {
			if (!headerValue.startsWith("Basic ")) {
				throw ServiceError.badRequest("Authentication not supported: " + headerValue);
			}
			headerValue = new String(Base64.decodeBase64(headerValue.replaceFirst("Basic ", "")));
			String[] uap = headerValue.split(":");
			if (uap.length != 2) {
				throw ServiceError.badRequest("Could not decode user:pass: " + headerValue);
			}

			u = Service.getInstance().getStore().users().get(uap[0]);
			if (u == null) {
				throw ServiceError.forbidden("no such username");// TODO 401 or 403?
			}
			if (!u.isValidPassword(uap[1])) {
				throw ServiceError.notAuthorized("invalid password");
			}
		}
		return u;
	}

	@Override
	public ContainerRequest filter(ContainerRequest cr) {
		if (cr.getMethod().equals("OPTIONS")) {
			return cr;
		}
		Auth a = null;
		User u = null;
		if (u == null) {
			if ((u = cookieAuth(cr.getCookies().get(COOKIE_NAME))) != null) {
				a = Auth.COOKIE;
			}
		}
		if (u == null) {
			if ((u = tokenAuth(cr.getHeaderValue(Headers.FMT_AUTH_TOKEN))) != null) {
				a = Auth.HEADER;
			}
		}
		if (u == null) {
			if ((u = httpBasicAuth(cr.getHeaderValue(HttpHeaders.AUTHORIZATION))) != null) {
				a = Auth.HTTP_BASIC;
			}
		}
		
		if (sr != null) {
			log.debug("AuthType: {}", a);
			sr.setAttribute(AUTH_TYPE_SESSION_ATTRIBUTE, a);
		}
		
		if (u == null) {
			cr.setSecurityContext(new FmtSecurityContext(null));
		} else {
			authSession(cr, u);
		}
		return cr;
	}

	@Override
	public ContainerResponse filter(ContainerRequest rq, ContainerResponse rs) {
		if (rq.getMethod().equals("OPTIONS")) {
			return rs;
		}
		Type t = rs.getEntityType();
		Auth type = (Auth) sr.getAttribute(AUTH_TYPE_SESSION_ATTRIBUTE);
		log.debug("AuthType: {}", type);
		if (type == null) type = Auth.HTTP_BASIC;
		if (sr.getAttribute(USER_SESSION_ATTRIBUTE) != null && type == Auth.HTTP_BASIC) {
			ResponseBuilder rb = Response.fromResponse(rs.getResponse());
			User u = (User) sr.getAttribute(USER_SESSION_ATTRIBUTE);
			if (u != null) {
				NewCookie c = createCookie(u);
				rb.cookie(c);
				rb.header(Headers.FMT_AUTH_TOKEN, c.getValue());
			}
			rs.setResponse(rb.build());
		} else if (sr.getAttribute(REMOVE_COOKIE_SESSION_ATTRIBUTE) != null) {
			sr.removeAttribute(REMOVE_COOKIE_SESSION_ATTRIBUTE);
			User u = (User) sr.getAttribute(USER_SESSION_ATTRIBUTE);
			if (u != null) {
				u.setAuthToken(null);
				Service.getInstance().getStore().users().save(u);
			}
			rs.setResponse(Response.fromResponse(rs.getResponse())
					.cookie(getInvalidCookie()).build());
		}
		rs.setEntity(rs.getEntity(), t);
		return rs;
	}

	/**
	 * 
	 * @param cr
	 * @param u
	 */
	public void authSession(ContainerRequest cr, User u) {
		auth(cr, sr, u);
	}

	@SuppressWarnings("unused")
	private void deauthSession(ContainerRequest cr) {
		deauth(cr, sr);
	}

	private NewCookie getInvalidCookie() {
		return new NewCookie(COOKIE_NAME, "", uri.getBaseUri().getPath(), uri
				.getBaseUri().getHost(), 1, null, 0, false);
	}

	private NewCookie createCookie(User u) {
		String token = getAuthToken(u);
		String path = uri.getBaseUri().getPath();
		if (path.endsWith("/")) {
			path = (path.length() == 1) ? null : path.substring(0,
					path.length() - 1);
		}
		Service.getInstance().getStore().users().setAuthToken(u, token);
		return new NewCookie(COOKIE_NAME, token, path, null, null, -1, false);
	}

	private String getAuthToken(User u) {
		final byte[] bytes = new byte[16];
		random.nextBytes(bytes);
		final String t = new StringBuilder().append(UUID.randomUUID())
				.append("|").append(new String(bytes)).append("|")
				.append("fmt").append("|").append(u.getUsername()).append("|")
				.append(u.getCreationTime()).toString();
		return new String(Base64.encodeBase64(t.getBytes()));
	}

	/**
	 * 
	 * @param cr
	 * @param sr
	 */
	public static void deauth(ContainerRequest cr, HttpServletRequest sr) {
		if (sr != null) {
			sr.removeAttribute(USER_SESSION_ATTRIBUTE);
			sr.setAttribute(REMOVE_COOKIE_SESSION_ATTRIBUTE, new Boolean(true));
		}
		if (cr != null) {
			cr.setSecurityContext(new FmtSecurityContext(null));
		}
	}

	/**
	 * 
	 * @param cr
	 * @param sr
	 * @param u
	 */
	public static void auth(ContainerRequest cr, HttpServletRequest sr, User u) {
		log.debug("Authorizing session for user {}", u);
		if (sr != null) {
			if (sr.getAttribute(AUTH_TYPE_SESSION_ATTRIBUTE) == null) {
				sr.setAttribute(AUTH_TYPE_SESSION_ATTRIBUTE, Auth.HTTP_BASIC);
			}
			sr.setAttribute(USER_SESSION_ATTRIBUTE, u);
		}
		if (cr != null) {
			cr.setSecurityContext(new FmtSecurityContext(u));
		}
	}

}
