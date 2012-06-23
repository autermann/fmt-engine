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

import java.security.SecureRandom;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

public class Authentication implements ContainerResponseFilter, ContainerRequestFilter {
	
	public static final String COOKIE_NAME = "fmt_oid";
	public static final String USER_SESSION_ATTRIBUTE = "user";
	public static final String REMOVE_COOKIE_SESSION_ATTRIBUTE = "remove-cookie";
	
	private static final Logger log = LoggerFactory.getLogger(Authentication.class);
	
	
	private final SecureRandom random = new SecureRandom();
	private @Context HttpServletRequest sr;
	private @Context UriInfo uri;
	
	@Override
	public ContainerRequest filter(ContainerRequest cr) {
		if (!cookieLogin(cr)) {
			if (!headerLogin(cr)) {
				cr.setSecurityContext(new FmtSecurityContext(null));
			}
		}
		return cr;
	}
	
	@Override
	public ContainerResponse filter(ContainerRequest rq, ContainerResponse rs) {
		HttpSession s = sr.getSession(true);
		if (s.getAttribute(USER_SESSION_ATTRIBUTE) != null && rq.getCookies().get(COOKIE_NAME) == null) {
			ResponseBuilder rb = Response.fromResponse(rs.getResponse());
			User u = (User) s.getAttribute(USER_SESSION_ATTRIBUTE);
			if (u != null) {
				rb.cookie(createCookie(u));
			}
			rs.setResponse(rb.build());
		} else if (sr.getAttribute(REMOVE_COOKIE_SESSION_ATTRIBUTE) != null) {
			sr.removeAttribute(REMOVE_COOKIE_SESSION_ATTRIBUTE);
			User u = (User) s.getAttribute(USER_SESSION_ATTRIBUTE);
			if (u != null) {
				u.setAuthToken(null);
				Service.getInstance().getStore().users().save(u);
			}
			rs.setResponse(Response.fromResponse(rs.getResponse()).cookie(getInvalidCookie()).build());
		}
		return rs;
	}
	
	private boolean headerLogin(ContainerRequest cr) {
		String auth = cr.getHeaderValue(HttpHeaders.AUTHORIZATION);
		if (auth == null) {
			return false;
		}
		if (!auth.startsWith("Basic ")) {
			throw ServiceError.badRequest("Authentication not supported: " + auth);
		}
		auth = new String(Base64.decodeBase64(auth.replaceFirst("Basic ", "")));
		String[] uap = auth.split(":");
		if (uap.length != 2) {
			throw ServiceError.badRequest("Could not decode user:pass: " + auth);
		}
		
		User u = Service.getInstance().getStore().users().get(uap[0]);
		if (u == null) {
			throw ServiceError.forbidden("no such username");//TODO 401 or 403?
		}
		if (u.isValidPassword(uap[1])) {
			authSession(cr, u);
			//set new cookie
			return true;
		} else {
			throw ServiceError.notAuthorized("invalid password");
		}
	}
	
	private boolean cookieLogin(ContainerRequest cr) {
		Cookie c = cr.getCookies().get(COOKIE_NAME);
		if (c == null) {
			return false;
		}
		User u = Service.getInstance().getStore().users().getByAuthToken(c.getValue());
		if (u == null) {
			log.warn("No user with this auth token!");
			return false;
		}
		authSession(cr, u);
		return true;
	}
	
	public void authSession(ContainerRequest cr, User u) {
		authSession(cr, sr, u);
	}
	
	// TODO logout?
	@SuppressWarnings("unused")
	private void deauthSession(ContainerRequest cr) {
		deauthSession(cr, sr);
	}

	private NewCookie getInvalidCookie() {
		return new NewCookie(COOKIE_NAME, "", uri.getBaseUri().getPath(), uri
				.getBaseUri().getHost(), 1, null, 0, false);
	}
	private NewCookie createCookie(User u) {
		String token = getAuthToken(u);
		String path = uri.getBaseUri().getPath();
		if (path.endsWith("/")) {
			path = (path.length() == 1) ? null : path.substring(0, path.length() - 1);
		}
		Service.getInstance().getStore().users().save(u.setAuthToken(token));
		return new NewCookie(COOKIE_NAME, token, path, null, null, -1, false);
	}

	private String getAuthToken(User u) {
		final byte[] bytes = new byte[16];
		random.nextBytes(bytes);
		
		final String t = new StringBuilder()
			.append(UUID.randomUUID())
			.append("|")
			.append(new String(bytes))
			.append("|")
			.append("fmt")
			.append("|")
			.append(u.getUsername())
			.append("|")
			.append(u.getCreationTime())
			.toString();
		return new String(Base64.encodeBase64(t.getBytes()));
	}

	public static void deauthSession(ContainerRequest cr, HttpServletRequest sr) {
		if (sr != null) {
			sr.getSession(true).removeAttribute(USER_SESSION_ATTRIBUTE);
			sr.getSession(true).setAttribute(REMOVE_COOKIE_SESSION_ATTRIBUTE, new Boolean(true));
		}
		if (cr != null) {
			cr.setSecurityContext(new FmtSecurityContext(null));
		}
	}
	
	public static void authSession(ContainerRequest cr, HttpServletRequest sr, User u) {
		log.debug("Authorizing session for user {}", u);
		if (sr != null) {
			sr.getSession(true).setAttribute(USER_SESSION_ATTRIBUTE, u);
		}
		if (cr != null) {
			cr.setSecurityContext(new FmtSecurityContext(u));
		}
	}
	
}
