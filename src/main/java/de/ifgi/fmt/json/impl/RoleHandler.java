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
import static de.ifgi.fmt.utils.constants.JSONConstants.TITLE_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.USERS_KEY;

import java.util.Set;

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Encodes(Role.class)
@Decodes(Role.class)
public class RoleHandler extends JSONHandler<Role>{
	
//	public static void main(String[] args) throws JSONException {
//		JSONObject jr = new JSONObject()
//			.put("title", "archer")
//			.put("description", "Dress like a archer")
//			.put("maxParticipants", 100)
//			.put("minParticipants",1)
//			.put("items", Utils.list("bow", "arrow", "costume"))
//			.put("location", new JSONObject()
//				.put("type", "Point")
//				.put("coordinates", Utils.list(51.963467, 7.613604))
//				.put("crs", new JSONObject()
//					.put("type", "name")
//					.put("properties",new JSONObject()
//					.put("name", "http://www.opengis.net/def/crs/EPSG/0/4326"))));
//		System.out.println(jr.toString(4));
//		RoleHandler h = new RoleHandler();
//		Role r = h.decode(jr);
//		System.out.println(h.encode(r, null));
//	}
	
    /**
     * 
     * @param j
     * @return
     * @throws JSONException
     */
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

    /**
     * 
     * @param t
     * @param uri
     * @return
     * @throws JSONException
     */
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
		if (t.getTitle() != null) {
		    j.put(TITLE_KEY, t.getTitle());
		}
		
		if (t.getMinCount() != null && t.getMinCount() >= 0) {
			j.put(MIN_PARTICIPANTS_KEY, t.getMinCount());
		}
		if (t.getMaxCount() != null && t.getMaxCount() >= 0) {
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

    /**
     * 
     * @param t
     * @param uriInfo
     * @return
     * @throws JSONException
     */
    @Override
	public JSONObject encodeAsRef(Role t, UriInfo uriInfo) throws JSONException {
		//TODO check if its an role of an activity (different urls)
		return new JSONObject()
			.put(ID_KEY, t.getId())
			.put(HREF_KEY, uriInfo.getBaseUriBuilder().path(Paths.ROLE_FOR_FLASHMOB).build(t.getFlashmob().getId(), t));
	}

}
