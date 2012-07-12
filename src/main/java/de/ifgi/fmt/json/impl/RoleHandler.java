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
package de.ifgi.fmt.json.impl;

import static de.ifgi.fmt.utils.constants.JSONConstants.ACTIVITIES_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.CATEGORY_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.DESCRIPTION_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.FLASHMOB_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.HREF_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ID_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ITEMS_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.LOCATION_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.MAX_PARTICIPANTS_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.MIN_PARTICIPANTS_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TASK_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TITLE_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.USERS_KEY;

import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.json.JSONHandler.DefaultView;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.Role.Category;
import de.ifgi.fmt.utils.Utils;
import de.ifgi.fmt.utils.constants.RESTConstants.PathParams;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.utils.constants.RESTConstants.View;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Encodes(Role.class)
@Decodes(Role.class)
@DefaultView(View.ROLE_FOR_FLASHMOB)
public class RoleHandler extends JSONHandler<Role> {

	// public static void main(String[] args) throws JSONException {
	// JSONObject jr = new JSONObject()
	// .put("title", "archer")
	// .put("description", "Dress like a archer")
	// .put("maxParticipants", 100)
	// .put("minParticipants",1)
	// .put("items", Utils.list("bow", "arrow", "costume"))
	// .put("location", new JSONObject()
	// .put("type", "Point")
	// .put("coordinates", Utils.list(51.963467, 7.613604))
	// .put("crs", new JSONObject()
	// .put("type", "name")
	// .put("properties",new JSONObject()
	// .put("name", "http://www.opengis.net/def/crs/EPSG/0/4326"))));
	// System.out.println(jr.toString(4));
	// RoleHandler h = new RoleHandler();
	// Role r = h.decode(jr);
	// System.out.println(h.encode(r, null));
	// }

	@Override
	public Role decode(JSONObject j) throws JSONException {
		Role r = new Role();
		r.setId(parseId(j));
		r.setTitle(j.optString(TITLE_KEY, null));
		r.setDescription(j.optString(DESCRIPTION_KEY, null));
		r.setCategory(parseEnum(j, Category.class, CATEGORY_KEY));
		r.setStartPoint(parseGeometry(j, Point.class, LOCATION_KEY));
		r.setMinCount(j.optInt(MIN_PARTICIPANTS_KEY, -1));
		r.setMaxCount(j.optInt(MAX_PARTICIPANTS_KEY, -1));

		if (isNull(j, ITEMS_KEY)) {
			r.setItems(null);
		} else if (hasKeyNotNull(j, ITEMS_KEY)) {
			Object io = j.opt(ITEMS_KEY);
			if (io instanceof JSONArray) {
				JSONArray jtems = (JSONArray) io;
				Set<String> items = Utils.set();
				for (int i = 0; i < jtems.length(); ++i) {
					items.add(jtems.getString(i));
				}
				r.setItems(items);
			} else if (io instanceof String) {
				r.setItems(Utils.set((String) io));
			}
		}

		return r;
	}

	@Override
	protected void encodeObject(JSONObject j, Role t, UriInfo uri)
			throws JSONException {
		j.put(ID_KEY, t.getId());
		switch (t.getView()) {
		case ROLE_FOR_FLASHMOB:
		case ROLE_OF_USER_IN_FLASHMOB:
		case ROLE_OF_ACTIVITY_OF_FLASHMOB:
			j.put(ITEMS_KEY, new JSONArray(t.getItems()));
			j.put(TITLE_KEY, t.getTitle());
			j.put(DESCRIPTION_KEY, t.getDescription());
			j.put(LOCATION_KEY, encodeGeometry(t.getStartPoint()));
			j.put(CATEGORY_KEY, t.getCategory());
			j.put(FLASHMOB_KEY, encode(t, t.getFlashmob(), uri));
			if (t.getMinCount() != null && t.getMinCount() >= 0) {
				j.put(MIN_PARTICIPANTS_KEY, t.getMinCount());
			}
			if (t.getMaxCount() != null && t.getMaxCount() >= 0) {
				j.put(MAX_PARTICIPANTS_KEY, t.getMaxCount());
			}
			break;
		case ACTIVITY_OF_ROLE_OF_FLASHMOB:
		case ROLES_FOR_FLASHMOB:
			if (uri != null) {
				MultivaluedMap<String, String> map = uri.getPathParameters();
				j.put(HREF_KEY,
						uri.getBaseUriBuilder()
								.path(Paths.ROLE_FOR_FLASHMOB)
								.build(map.getFirst(PathParams.FLASHMOB),
										t.getId()));
			}
			break;
		case ACTIVITY_OF_FLASHMOB:
		case ACTIVITY_OF_FLASHMOB_OF_USER:
		case ROLES_OF_ACTIVITY_OF_FLASHMOB:
		case TASK_OF_ACTIVITY_OF_FLASHMOB_OF_USER:
			if (uri != null) {
				MultivaluedMap<String, String> map = uri.getPathParameters();
				j.put(HREF_KEY,
						uri.getBaseUriBuilder()
								.path(Paths.ROLE_OF_ACTIVITY_OF_FLASHMOB)
								.build(map.getFirst(PathParams.FLASHMOB),
										map.getFirst(PathParams.ACTIVITY),
										t.getId()));
			}
			break;
		case TASK_OF_ROLE_OF_ACTIVITY_OF_FLASHMOB:
		case TASK_OF_ACTIVITY_OF_ROLE_OF_FLASHMOB:
			if (uri != null) {
				MultivaluedMap<String, String> map = uri.getPathParameters();
				j.put(HREF_KEY,	uri.getBaseUriBuilder()
								.path(Paths.ROLE_OF_ACTIVITY_OF_FLASHMOB)
								.build(map.getFirst(PathParams.FLASHMOB), 
									   map.getFirst(PathParams.ACTIVITY), t.getId()));
			}
			break;
		}
	}

	@Override
	protected void encodeUris(JSONObject j, Role t, UriInfo uri)
			throws JSONException {
		MultivaluedMap<String, String> map = uri.getPathParameters();
		switch (t.getView()) {
		case ROLE_FOR_FLASHMOB:
		case ROLE_OF_ACTIVITY_OF_FLASHMOB:
			j.put(ACTIVITIES_KEY,
					uri.getBaseUriBuilder()
							.path(Paths.ACTIVITIES_OF_ROLE_OF_FLASHMOB)
							.build(t.getFlashmob().getId(), t.getId()));
			j.put(USERS_KEY,
					uri.getBaseUriBuilder()
							.path(Paths.USERS_OF_ROLE_OF_FLASHMOB)
							.build(t.getFlashmob().getId(), t.getId()));
		break;
		case ROLE_OF_USER_IN_FLASHMOB:
			j.put(ACTIVITIES_KEY,
					uri.getBaseUriBuilder()
							.path(Paths.ACTIVITIES_OF_FLASHMOB_OF_USER)
							.build(map.get(PathParams.USER),
									t.getFlashmob().getId(), t.getId()));
			j.put(USERS_KEY,
					uri.getBaseUriBuilder()
							.path(Paths.USERS_OF_ROLE_OF_FLASHMOB)
							.build(t.getFlashmob().getId(), t.getId()));
		break;
		}
		if (t.getView() == View.ROLE_OF_ACTIVITY_OF_FLASHMOB) {
			j.put(TASK_KEY, uri.getBaseUriBuilder()
					.path(Paths.TASK_OF_ROLE_OF_ACTIVITY_OF_FLASHMOB)
					.build(t.getFlashmob().getId(), 
							map.getFirst(PathParams.ACTIVITY), t.getId()));
		}
	}
}
