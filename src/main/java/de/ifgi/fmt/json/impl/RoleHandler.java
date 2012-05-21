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

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;

@Encodes(Role.class)
@Decodes(Role.class)
public class RoleHandler extends JSONHandler<Role>{
	
	@Override
	public Role decode(JSONObject j) throws JSONException {
		//TODO
		throw new UnsupportedOperationException("Not yet implemented");
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
		if (t.getFlashmob() != null) {
			j.put(FLASHMOB_KEY, (uri != null) ? JSONFactory.getEncoder(Flashmob.class).encodeAsReference(t.getFlashmob(), uri) : t.getFlashmob());
		}
		
		if (uri != null) {
			j.put(ACTIVITIES_KEY, uri.getAbsolutePathBuilder().path(Paths.ACTIVITIES).build());
			j.put(USERS_KEY, uri.getAbsolutePathBuilder().path(Paths.USERS).build());
		}
		
		return j;
	}

	@Override
	public JSONObject encodeAsReference(Role t, UriInfo uriInfo) throws JSONException {
		//TODO check if its an role of an activity (different urls)
		return new JSONObject()
			.put(ID_KEY, t.getId())
			.put(HREF_KEY, uriInfo.getAbsolutePathBuilder().path(Paths.ROLE).build(t));
	}

}
