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
import static de.ifgi.fmt.utils.constants.JSONConstants.COMMENTS_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.COORDINATOR_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.DESCRIPTION_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.END_TIME_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.HREF_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ID_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.KEY_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.LOCATION_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.PARTICIPANTS_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.PUBLIC_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.PUBLISH_TIME_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.REQUIRED_USERS_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ROLES_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.START_TIME_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TITLE_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TRIGGERS_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.USERS_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.VALIDITY_KEY;

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;

@Decodes(Flashmob.class)
@Encodes(Flashmob.class)
public class FlashmobHandler extends JSONHandler<Flashmob> {
	private static final Logger log = LoggerFactory.getLogger(FlashmobHandler.class);

	@Override
	public Flashmob decode(JSONObject j) throws JSONException {
		log.debug("Decoding Flashmob {}", j);
		Flashmob f = new Flashmob();

		f.setTitle(j.optString(TITLE_KEY, null));
		f.setPublic(j.optBoolean(PUBLIC_KEY));
		f.setDescription(j.optString(DESCRIPTION_KEY, null));
		f.setKey(j.optString(KEY_KEY, null));
		
		String geom = j.optString(LOCATION_KEY, null);
		if (geom != null) {
			log.debug("Decoding Geometry {}", geom);
			try {
				f.setLocation((Point) getGeometryDecoder().parseUwGeometry(geom));
			} catch (Exception e) {
				throw ServiceError.badRequest(e);
			}
		}
		
		String start = j.optString(START_TIME_KEY, null);
		String end = j.optString(END_TIME_KEY, null);
		String publish = j.optString(PUBLISH_TIME_KEY, null);

		if (start != null) { f.setStart(getDateTimeFormat().parseDateTime(start)); }
		if (end != null) { f.setEnd(getDateTimeFormat().parseDateTime(end)); }
		if (publish != null) { f.setPublish(getDateTimeFormat().parseDateTime(publish)); }
		return f;
	}
		
	@Override
	public JSONObject encode(Flashmob f, UriInfo uri) throws JSONException {
		
		JSONObject j = new JSONObject().put(ID_KEY, f.getId());
		
		if (f.getLocation() != null) {
			try {
				j.put(LOCATION_KEY, new JSONObject(getGeometryEncoder().encodeGeometry(f.getLocation())));
			} catch (Exception e) {
				throw ServiceError.internal(e);
			}
		}
		
		if (f.getTitle() != null) {
			j.put(TITLE_KEY, f.getTitle());
		}
		if (f.getDescription() != null) {
			j.put(DESCRIPTION_KEY, f.getDescription());
		}
		if (f.getStart() != null) {
			j.put(START_TIME_KEY, getDateTimeFormat().print(f.getStart()));
		}
		if (f.getEnd() != null) {
			j.put(END_TIME_KEY, getDateTimeFormat().print(f.getEnd()));
		}
		if (f.getPublish() != null) {
			j.put(PUBLISH_TIME_KEY, getDateTimeFormat().print(f.getPublish()));
		}
		if (f.getKey() != null) {
			j.put(KEY_KEY, f.getKey());
		}
		
		int users = 0;
		int requiredUsers = 0;
		
		for (Role r : f.getRoles()) {
			requiredUsers += r.getMinCount();
			users += r.getUsers().size();
		}
		
		j.put(REQUIRED_USERS_KEY, requiredUsers);
		j.put(USERS_KEY, users);
		
		
		j.put(PUBLIC_KEY, f.isPublic());
		j.put(VALIDITY_KEY, f.getValidity());
		if (f.getCoordinator() != null) {
			if (uri != null) {
				j.put(COORDINATOR_KEY, uri.getBaseUriBuilder().path(Paths.USER).build(f.getCoordinator().getId()));
			} else {
				j.put(COORDINATOR_KEY, f.getCoordinator().getId());
			}
		}
		
		if (uri != null) {
			j.put(ACTIVITIES_KEY, uri.getBaseUriBuilder().path(Paths.ACTIVITIES_OF_FLASHMOB).build(f.getId()));
			j.put(ROLES_KEY, uri.getBaseUriBuilder().path(Paths.ROLES_FOR_FLASHMOB).build(f.getId()));
			j.put(TRIGGERS_KEY, uri.getBaseUriBuilder().path(Paths.TRIGGERS_OF_FLASHMOB).build(f.getId()));
			j.put(COMMENTS_KEY, uri.getBaseUriBuilder().path(Paths.COMMENTS_FOR_FLASHMOB).build(f.getId()));
			j.put(PARTICIPANTS_KEY, uri.getBaseUriBuilder().path(Paths.USERS_OF_FLASHMOB).build(f.getId()));
		}
		return j;
	}

	@Override
	public JSONObject encodeAsReference(Flashmob t, UriInfo uriInfo) throws JSONException {
		return encode(t, null).put(HREF_KEY, uriInfo.getBaseUriBuilder().path(Paths.FLASHMOB).build(t.getId()));
	}

}
