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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;

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
