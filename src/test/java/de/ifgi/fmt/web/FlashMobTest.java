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

import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.Role.Category;
import de.ifgi.fmt.utils.Utils;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.utils.constants.RESTConstants.MediaTypes;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.utils.constants.RESTConstants.QueryParams;
import de.ifgi.fmt.web.filter.auth.Authorization;

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
		isCreated(useradd);
		
		String user = useradd.getEntity(JSONObject.class).getString(JSONConstants.USERNAME_KEY);
		
		JSONObject entity = createFlashmobJson(user, "Ein Flashmob",
				"Eine Bescreibung", start, start.plusHours(2), p, null,
				true, start.minusHours(1));
		System.err.println(entity.toString(4));
		ClientResponse flashmobadd = addFlashmob(entity);
		isCreated(flashmobadd);
		JSONObject createdFlashmob = flashmobadd.getEntity(JSONObject.class);
		
		ObjectId flashmobID = new ObjectId(createdFlashmob.getString(JSONConstants.ID_KEY));
		
		ClientResponse getflashmob = getFlashmob(flashmobID);
		isOk(getflashmob);
		
		JSONObject gettedFlashmob = getflashmob.getEntity(JSONObject.class);
		
		assertEquals(user, createdFlashmob
						.getJSONObject(JSONConstants.COORDINATOR_KEY)
						.getString(JSONConstants.USERNAME_KEY));
		assertEquals(user, gettedFlashmob
						.getJSONObject(JSONConstants.COORDINATOR_KEY)
						.getString(JSONConstants.USERNAME_KEY));
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
		isOk(cr);
	}
	
	@Test
	public void testEmptyFlashmob() throws JSONException {
		ClientResponse cr = getWebResource()
				.path(Paths.FLASHMOBS)
				.accept(MediaTypes.FLASHMOB_TYPE)
				.type(MediaTypes.FLASHMOB_TYPE)
				.entity(new JSONObject())
				.post(ClientResponse.class);
		isBadRequest(cr);
		JSONObject j = cr.getEntity(JSONObject.class);
		assertNotNull(j);
		System.err.println(j.toString(4));
	}
	
	@Test
	public void testCreateUserGetTokenAndChangeUser() throws JSONException {
		ClientResponse cr = addUser(createUserJson(getRandomUsername(), getRandomMail(), getRandomPassword()));
		isCreated(cr);
		Cookie token = null;
		for (Cookie c : cr.getCookies()) {
			if (c.getName().equals(Authorization.COOKIE_NAME)) {
				token  = c;
			}
		}
		assertNotNull(token.getValue());
		String id = cr.getEntity(JSONObject.class).optString(JSONConstants.USERNAME_KEY, null);
		assertNotNull(id);
		
		cr = changeUser(id, createUserJson(null, null, "neuespassword"), token);
		isOk(cr);
		
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
		isCreated(addUser(createUserJson(getRandomUsername(), mail, getRandomPassword())));
		isBadRequest(addUser(createUserJson(getRandomUsername(), mail, getRandomPassword())));
	}
	
	/* if this single test case is executed everything is fine. for the entire class it fails */
	@Ignore@Test
	public void testCreateUserWithDuplicateUsername() throws JSONException {
		String username = getRandomUsername();
		isCreated(addUser(createUserJson(username, getRandomMail(), getRandomPassword())));
		isBadRequest(addUser(createUserJson(username, getRandomMail(), getRandomPassword())));
	}
	
	
	@Test
	public void testGetUsers() {
		getWebResource().path(Paths.USERS).accept(MediaTypes.USER_LIST).get(JSONObject.class);
	}
	
	@Test
	public void testGetFlashmobsOfUser() throws Exception {
		String user = "auti";
		Point p = new GeometryFactory().createPoint(new Coordinate(7, 42));
		p.setSRID(4326);
		isCreated(addUser(createUserJson(user, getRandomMail(), getRandomPassword())));
		JSONObject j = createFlashmobJson(user, "title", "description", new DateTime(), new DateTime().plusHours(5), p	, null, true, null);
		ClientResponse cr = addFlashmob(j);
		isCreated(cr);
		String fid = cr.getEntity(JSONObject.class).getString(JSONConstants.ID_KEY);
		ClientResponse r = getWebResource()
			.uri(uri().path(Paths.ROLES_FOR_FLASHMOB).build(fid))
			.entity(JSONFactory.getEncoder(Role.class).encode(new Role()
				.setCategory(Category.EASY)
				.setDescription("description")
				.setItems(Utils.set("apfel", "birne"))
				.setMinCount(1)
				.setMaxCount(100)
				.setStartPoint(p)
				.setTitle("title"), null))
			.type(MediaTypes.ROLE)
			.post(ClientResponse.class);
		isCreated(r);
		String rid = r.getEntity(JSONObject.class).getString(JSONConstants.ID_KEY);
		
		ClientResponse flashmobsOfUser = getWebResource()
				.path(Paths.FLASHMOBS)
				.queryParam(QueryParams.PARTICIPANT, user)
				.accept(MediaTypes.FLASHMOB_LIST)
				.get(ClientResponse.class);
			isOk(flashmobsOfUser);
			assertEquals(0, flashmobsOfUser.getEntity(JSONObject.class).getJSONArray(JSONConstants.FLASHMOBS_KEY).length());
		
		ClientResponse addUserToRole = getWebResource()
			.uri(uri().path(Paths.USERS_OF_ROLE_OF_FLASHMOB).build(fid, rid))
			.type(MediaTypes.USER_TYPE)
			.entity(new JSONObject()
				.put(JSONConstants.USERNAME_KEY, user))
			.post(ClientResponse.class);
		isCreated(addUserToRole);
		flashmobsOfUser = getWebResource()
			.path(Paths.FLASHMOBS)
			.queryParam(QueryParams.PARTICIPANT, user)
			.accept(MediaTypes.FLASHMOB_LIST)
			.get(ClientResponse.class);
		isOk(flashmobsOfUser);
		assertEquals(1, flashmobsOfUser.getEntity(JSONObject.class).getJSONArray(JSONConstants.FLASHMOBS_KEY).length());
	}
	
	@Test
	public void testCreateUserWithoutMailAddress() throws UniformInterfaceException, ClientHandlerException, JSONException {
		isCreated(getWebResource()
				.path(Paths.USERS)
				.accept(MediaTypes.USER)
				.type(MediaTypes.USER)
			 	.entity(createUserJson(getRandomUsername(), null, getRandomPassword()))
			 	.post(ClientResponse.class));
		
	}
	
}