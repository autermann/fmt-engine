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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response.Status;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.utils.constants.RESTConstants.MediaTypes;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.utils.constants.RESTConstants.QueryParams;
import de.ifgi.fmt.web.filter.auth.Authentication;

public class FlashMobTest extends AbstractFlashMobTest {
	

	public FlashMobTest() {
		super();
	}

	@Test
	public void testGetFlashmobs() {
		getWebResource().path(Paths.FLASHMOBS).accept(MediaTypes.FLASHMOB_LIST).get(JSONObject.class);
	}

	@Test
	public void createFlashmob() throws JSONException {
		DateTime start = new DateTime().plusHours(5);
		Point p = new GeometryFactory().createPoint(new Coordinate(7, 42));
		p.setSRID(4326);
		ClientResponse useradd = addUser(createUserJson(getRandomUsername(),
				getRandomMail(), getRandomPassword()));
		assertEquals(Status.CREATED.getStatusCode(), useradd.getStatus());
		
		ObjectId user = new ObjectId(useradd.getEntity(JSONObject.class)
				.getString(JSONConstants.ID_KEY));
		
		JSONObject entity = createFlashmobJson(user, "Ein Flashmob",
				"Eine Bescreibung", start, start.plusHours(2), p, null,
				true, start.minusHours(1));
		System.err.println(entity.toString(4));
		ClientResponse flashmobadd = addFlashmob(entity);
		
		assertEquals(Status.CREATED.getStatusCode(), flashmobadd.getStatus());
		JSONObject createdFlashmob = flashmobadd.getEntity(JSONObject.class);
		
		ObjectId flashmobID = new ObjectId(createdFlashmob.getString(JSONConstants.ID_KEY));
		
		ClientResponse getflashmob = getFlashmob(flashmobID);
		assertEquals(Status.OK.getStatusCode(), getflashmob.getStatus());
		
		JSONObject gettedFlashmob = getflashmob.getEntity(JSONObject.class);
		
		assertEquals(
				user,
				new ObjectId(createdFlashmob.getJSONObject(
						JSONConstants.COORDINATOR_KEY).getString(
						JSONConstants.ID_KEY)));
		assertEquals(
				user,
				new ObjectId(gettedFlashmob.getJSONObject(
						JSONConstants.COORDINATOR_KEY).getString(
						JSONConstants.ID_KEY)));
		
		if (true) {
			// do nothing
		}
		
	}
	
	
	protected ClientResponse getFlashmob(ObjectId key) {
		return getWebResource().uri(uri().path(Paths.FLASHMOB).build(key))
				.accept(MediaTypes.FLASHMOB)
				.get(ClientResponse.class);
				
	}
	
	@Test
	public void testFrom() {
		ClientResponse cr = getWebResource().path(Paths.FLASHMOBS)
				.queryParam(QueryParams.FROM, "2012-05-27T21:00:00.000+02:00")
				.queryParam(QueryParams.TO, "2012-06-07T15:15:32.000+02:00")
				.accept(MediaTypes.FLASHMOB_LIST)
				.get(ClientResponse.class);
		assertEquals(Status.OK.getStatusCode(), cr.getStatus());
	}
	
	@Test
	public void testEmptyFlashmob() throws JSONException {
		ClientResponse cr = getWebResource()
				.path(Paths.FLASHMOBS)
				.accept(MediaTypes.FLASHMOB_TYPE)
				.type(MediaTypes.FLASHMOB_TYPE)
				.entity(new JSONObject())
				.post(ClientResponse.class);
		assertEquals(Status.BAD_REQUEST.getStatusCode(), cr.getStatus());
		JSONObject j = cr.getEntity(JSONObject.class);
		assertNotNull(j);
		System.err.println(j.toString(4));
	}
	

	@Test
	public void testCreateUserGetTokenAndChangeUser() throws JSONException {
		ClientResponse cr = addUser(createUserJson(getRandomUsername(), getRandomMail(), getRandomPassword()));
		assertEquals(Status.CREATED.getStatusCode(), cr.getStatus());
		Cookie token = null;
		for (Cookie c : cr.getCookies()) {
			if (c.getName().equals(Authentication.COOKIE_NAME)) {
				token  = c;
			}
		}
		assertNotNull(token.getValue());
		String id = cr.getEntity(JSONObject.class).optString(JSONConstants.ID_KEY, null);
		assertNotNull(id);
		
		cr = changeUser(id, createUserJson(null, null, "neuespassword"), token);
		assertEquals(Status.OK.getStatusCode(), cr.getStatus());
		
	}

	public void printUsers() throws ClientHandlerException, UniformInterfaceException, JSONException {
//		JSONArray a = getUsers().getEntity(JSONObject.class).getJSONArray(JSONConstants.USERS_KEY);
//		Set<String> ids = Utils.set();
//		for (int i = 0; i< a.length();++i) {
//			ids.add(a.getJSONObject(i).getString(JSONConstants.ID_KEY));
//		}
//		System.err.println("Users: " + Utils.join(", ", ids));
		System.err.println(getUsers().getEntity(JSONObject.class).toString(4));
	}
	
	/* if this single test case is executed everything is fine. for the entire class it fails */
	@Ignore@Test
	public void testCreateUserWithDuplicateMailAddress() throws JSONException {
		String mail = getRandomMail();
		assertEquals(Status.CREATED.getStatusCode(), addUser(createUserJson(getRandomUsername(), mail, getRandomPassword())).getStatus());
		assertEquals(Status.BAD_REQUEST.getStatusCode(), addUser(createUserJson(getRandomUsername(), mail, getRandomPassword())).getStatus());
	}
	
	/* if this single test case is executed everything is fine. for the entire class it fails */
	@Ignore@Test
	public void testCreateUserWithDuplicateUsername() throws JSONException {
		String username = getRandomUsername();
		assertEquals(Status.CREATED.getStatusCode(), addUser(createUserJson(username, getRandomMail(), getRandomPassword())).getStatus());
		assertEquals(Status.BAD_REQUEST.getStatusCode(), addUser(createUserJson(username, getRandomMail(), getRandomPassword())).getStatus());
	}
	
	
	@Test
	public void testGetUsers() {
		getWebResource().path(Paths.USERS).accept(MediaTypes.USER_LIST).get(JSONObject.class);
	}
	
	
	@Test
	public void testCreateUserWithoutMailAddress() throws UniformInterfaceException, ClientHandlerException, JSONException {
		assertEquals(Status.CREATED.getStatusCode(), getWebResource().path(Paths.USERS)
													 .accept(MediaTypes.USER)
													 .type(MediaTypes.USER)
													 .entity(createUserJson(getRandomUsername(), null, getRandomPassword()))
													 .post(ClientResponse.class).getStatus());
		
	}
	
}
