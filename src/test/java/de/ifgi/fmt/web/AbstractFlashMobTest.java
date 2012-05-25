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
package de.ifgi.fmt.web;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.UriBuilder;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.grizzly.util.buf.Base64;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.grizzly.web.GrizzlyWebTestContainerFactory;

import de.ifgi.fmt.mongo.MongoDB;
import de.ifgi.fmt.utils.Utils;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.utils.constants.RESTConstants.HeaderParams;
import de.ifgi.fmt.utils.constants.RESTConstants.MediaTypes;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.filter.CorsFilter;
import de.ifgi.fmt.web.filter.auth.Authentication;

public class AbstractFlashMobTest extends JerseyTest {
	protected static final Logger log = LoggerFactory.getLogger(AbstractFlashMobTest.class);
	private static final String LOG = LoggingFilter.class.getName();
	private static final String AUTH = Authentication.class.getName();
	private static final String CORS = CorsFilter.class.getName();
	private static final String ROLES_ALLOWED = RolesAllowedResourceFilterFactory.class.getName();
	private static final String PACKAGES = "de.ifgi.fmt";

	@BeforeClass
	public static void initLogger() {
		java.util.logging.Logger rootLogger = java.util.logging.LogManager
				.getLogManager().getLogger("");
		java.util.logging.Handler[] handlers = rootLogger.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			rootLogger.removeHandler(handlers[i]);
		}
		org.slf4j.bridge.SLF4JBridgeHandler.install();
	}

	@Before
	public void setUp() throws Exception {
		try {
			super.setUp();
			clearDatabase();
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	protected void clearDatabase() {
		MongoDB db = MongoDB.getInstance();
		db.getMongo().dropDatabase(db.getDatabase());
	}
	
	protected WebResource getWebResource() {
		return this.client().resource(getBaseURI());
	}

	public AbstractFlashMobTest() {
		super(new GrizzlyWebTestContainerFactory());
	}
	
	public AppDescriptor configure() {
		return new WebAppDescriptor.Builder(PACKAGES)
				.initParam("com.sun.jersey.spi.container.ContainerRequestFilters", Utils.join(";", LOG, AUTH))
				.initParam("com.sun.jersey.spi.container.ContainerResponseFilters", Utils.join(";", CORS, AUTH, LOG))
				.initParam("com.sun.jersey.spi.container.ResourceFilters", Utils.join(";", ROLES_ALLOWED))
				.initParam(LoggingFilter.FEATURE_LOGGING_DISABLE_ENTITY, "true")
		.build();
	}

	protected String getRandomPassword() {
		return String.valueOf(getRandomInt());
	}
	
	protected String getRandomUsername() {
		return "user" + getRandomInt();
	}
	
	private int getRandomInt() {
		return (int) Math.floor(Math.random()*1e6);
	}
	
	protected String getRandomMail() {
		return getRandomInt() + "@email.tld";
	}
	
	protected JSONObject createUserJson(String username, String mail, String password) throws JSONException {
		JSONObject j = new JSONObject();
		if (username != null) {
			j.put(JSONConstants.USERNAME_KEY, username);
		}
		if (mail != null) {
			j.put(JSONConstants.EMAIL_KEY, mail);
		}
		if (password != null) {
			j.put(JSONConstants.PASSWORD_KEY, password);
		}
		return j;
	}
	
	protected ClientResponse getUsers() {
		return getWebResource()
				.path(Paths.USERS)
				.accept(MediaTypes.USER_LIST)
				.get(ClientResponse.class);
	}
	
	protected ClientResponse addUser(JSONObject u) {
		return getWebResource()
				.path(Paths.USERS)
				.accept(MediaTypes.USER)
				.type(MediaTypes.USER)
				.entity(u)
				.post(ClientResponse.class);
	}
	
	protected UriBuilder uri() {
		return UriBuilder.fromUri(getWebResource().getURI());
	}
	
	
	protected ClientResponse changeUser(String id, JSONObject u, Cookie cookie) {
		return getWebResource().uri(uri().path(Paths.USER).build(id))
				.accept(MediaTypes.USER)
				.type(MediaTypes.USER)
				.entity(u)
				.cookie(cookie)
				.put(ClientResponse.class);
	}
	
	protected String getAuthHeaderValue(String user, String pass) {
		return new String(Base64.encode(("Basic " + user + ":" + pass).getBytes()));
	}

	protected ClientResponse changeUser(ObjectId id, JSONObject u, String user, String pass) {
		return getWebResource().uri(uri().path(Paths.USER).build(id))
				.accept(MediaTypes.USER)
				.type(MediaTypes.USER)
				.entity(u)
				.header(HeaderParams.AUTHORIZATION, getAuthHeaderValue(user, pass))
				.put(ClientResponse.class);
	}
	
}
