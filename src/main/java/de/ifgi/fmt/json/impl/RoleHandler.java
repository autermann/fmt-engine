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
import static de.ifgi.fmt.utils.constants.JSONConstants.USERS_KEY;

import java.util.Set;

import javax.ws.rs.core.UriInfo;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.Role.Category;
import de.ifgi.fmt.utils.Utils;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;

@Encodes(Role.class)
@Decodes(Role.class)
public class RoleHandler extends JSONHandler<Role>{
	
	public static void main(String[] args) throws JSONException {
		String j = "{\"title\":\"Standard\",\"description\":\"Standard role\",\"maxParticipants\":100,\"minParticipants\":0}";
		new RoleHandler().decode(new JSONObject(j));
	}
	
	@Override
	public Role decode(JSONObject j) throws JSONException {
		Role r = new Role();
		String id = j.optString(ID_KEY, null);
		if (id != null) {
			r.setId(new ObjectId(id));
		}
		r.setDescription(j.optString(DESCRIPTION_KEY, null));

		String category = j.optString(CATEGORY_KEY, null);
		if (category != null) {
			try {
				r.setCategory(Category.valueOf(category));
			}catch (IllegalArgumentException e) {
				throw ServiceError.badRequest(e);
			}
		}
		String location = j.optString(LOCATION_KEY, null);
		if (location != null) {
			Geometry g;
			try {
				g = getGeometryDecoder().parseUwGeometry(location);
			} catch (Exception e) {
				throw ServiceError.badRequest(e);
			}
			if (!(g instanceof Point)) {
				throw ServiceError.badRequest("only points are allowed");
			}
			r.setStartPoint((Point) g);
		}
		
		r.setMinCount(j.optInt(MIN_PARTICIPANTS_KEY, -1));
		r.setMaxCount(j.optInt(MAX_PARTICIPANTS_KEY, -1));
		JSONArray jtems = j.optJSONArray(ITEMS_KEY);
		if (jtems != null) {
			Set<String> items = Utils.set();
			for (int i = 0; i < jtems.length(); ++i) {
				items.add(jtems.getString(i));
			}
			r.setItems(items);
		}
		
		return r;
	}

	@Override
	public JSONObject encode(Role t, UriInfo uri) throws JSONException {
		JSONObject j = new JSONObject().put(ID_KEY, t.getId());
		
		if (t.getItems() != null) {
			JSONArray items = new JSONArray();
			for (String item : t.getItems()) {
				items.put(item);
			}
			j.put(ITEMS_KEY, items);
		}
		if (t.getMinCount() >= 0) {
			j.put(MIN_PARTICIPANTS_KEY, t.getMinCount());
		}
		if (t.getMaxCount() >= 0) {
			j.put(MAX_PARTICIPANTS_KEY, t.getMaxCount());
		}
		if (t.getDescription() != null) {
			j.put(DESCRIPTION_KEY, t.getDescription());
		}
		if (t.getStartPoint() != null) {
			try {
				j.put(LOCATION_KEY, new JSONObject(getGeometryEncoder().encodeGeometry(t.getStartPoint())));
			} catch (Exception e) {
				throw ServiceError.internal(e);
			}
		}
		
		if (t.getCategory() != null) {
			j.put(CATEGORY_KEY, t.getCategory());
		}
		
		
		if (uri != null) {
			if (t.getFlashmob() != null) {
				j.put(FLASHMOB_KEY, JSONFactory.getEncoder(Flashmob.class).encodeAsRef(t.getFlashmob(), uri));
			}
			
			j.put(ACTIVITIES_KEY, uri.getAbsolutePathBuilder().path(Paths.ACTIVITIES).build());
			j.put(USERS_KEY, uri.getAbsolutePathBuilder().path(Paths.USERS).build());
		}
		
		return j;
	}

	@Override
	public JSONObject encodeAsRef(Role t, UriInfo uriInfo) throws JSONException {
		//TODO check if its an role of an activity (different urls)
		return new JSONObject()
			.put(ID_KEY, t.getId())
			.put(HREF_KEY, uriInfo.getBaseUriBuilder().path(Paths.ROLE_FOR_FLASHMOB).build(t.getFlashmob().getId(), t));
	}

}
