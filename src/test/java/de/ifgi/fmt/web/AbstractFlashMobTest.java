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

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriBuilder;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.grizzly.util.buf.Base64;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.grizzly.web.GrizzlyWebTestContainerFactory;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.mongo.MongoDB;
import de.ifgi.fmt.utils.Utils;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.utils.constants.RESTConstants.MediaTypes;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.filter.CorsFilter;
import de.ifgi.fmt.web.filter.auth.Authorization;
import de.ifgi.fmt.web.filter.auth.RolesAllowedFilterFactory;

public class AbstractFlashMobTest extends JerseyTest {
	protected static final Logger log = LoggerFactory.getLogger(AbstractFlashMobTest.class);
	private static final String LOG = LoggingFilter.class.getName();
	private static final String AUTH = Authorization.class.getName();
	private static final String CORS = CorsFilter.class.getName();
	private static final String ROLES_ALLOWED = RolesAllowedFilterFactory.class.getName();
	private static final String PACKAGES = "de.ifgi.fmt";
	
	private final AtomicInteger i = new AtomicInteger(100000);

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
		return String.valueOf(getNextInt());
	}
	
	protected String getRandomUsername() {
		return "user" + getNextInt();
	}
	
	private int getNextInt() {
		return i.getAndIncrement();
	}
	
	protected String getRandomMail() {
		return getNextInt() + "@email.tld";
	}
	
	protected JSONObject createUserJson(String username, String mail,
			String password) throws JSONException {
		User u = new User().setUsername(username).setEmail(mail)
				.setPassword(password);
		JSONObject j = JSONFactory.getEncoder(User.class).encode(u, null);
		j.put(JSONConstants.PASSWORD_KEY, password);
		return j;
	}

	protected JSONObject createFlashmobJson(String coordinator, String title,
			String description, DateTime start, DateTime end, Point position,
			String key, boolean isPublic, DateTime publishTime)
			throws JSONException {
		Flashmob f = new Flashmob()
				.setCoordinator(new User().setUsername(coordinator))
				.setDescription(description).setEnd(end).setStart(start)
				.setKey(key).setPublic(isPublic).setLocation(position)
				.setPublish(publishTime).setStart(start).setTitle(title);
		return JSONFactory.getEncoder(Flashmob.class).encode(f, null);
	}
	
	protected ClientResponse getUsers() {
		return getWebResource()
				.path(Paths.USERS)
				.accept(MediaTypes.USER_LIST)
				.get(ClientResponse.class);
	}
	
	protected ClientResponse addFlashmob(JSONObject f) {
		return getWebResource()
				.path(Paths.FLASHMOBS)
				.accept(MediaTypes.FLASHMOB)
				.type(MediaTypes.FLASHMOB)
				.entity(f)
				.post(ClientResponse.class);
				
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
				.header(HttpHeaders.AUTHORIZATION, getAuthHeaderValue(user, pass))
				.put(ClientResponse.class);
	}
	
	protected static void isBadRequest(ClientResponse cr) {
		isStatus(Status.BAD_REQUEST, cr);
	}
	
	protected static void isOk(ClientResponse cr) {
		isStatus(Status.OK, cr);
	}
	
	protected static void isCreated(ClientResponse cr) {
		isStatus(Status.CREATED, cr);
	}
	
	protected static void isStatus(Status status, ClientResponse cr) {
		Assert.assertEquals(status.getStatusCode(), cr.getStatus());
	}
	
	
}
