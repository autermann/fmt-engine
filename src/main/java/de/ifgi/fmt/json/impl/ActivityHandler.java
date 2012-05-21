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

import static de.ifgi.fmt.utils.constants.JSONConstants.DESCRIPTION_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.FLASHMOB_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.HREF_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ID_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ROLES_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.SIGNAL_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TITLE_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TRIGGERS_KEY;

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.json.JSONEncoder;
import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;

@Decodes(Activity.class)
@Encodes(Activity.class)
public class ActivityHandler extends JSONHandler<Activity> {

	@Override
	public Activity decode(JSONObject j) throws JSONException {
		// TODO decoding
		throw ServiceError.internal("Not yet implemented");
	}

	@Override
	public JSONObject encode(Activity t, UriInfo uri) throws JSONException {
		// will be called at this URL's
		// - Paths.ACTIVITY_OF_FLASHMOB
		// - Paths.ACTIVITY_OF_FLASHMOB_OF_USER
		// - Paths.ACTIVITY_OF_ROLE_OF_FLASHMOB
		// needs to check which URL was called to adjust links...
		
		JSONEncoder<Role> renc = JSONFactory.getEncoder(Role.class);
		JSONObject j = new JSONObject().put(ID_KEY, t.getId());
		if (uri != null) {
			j.put(FLASHMOB_KEY, uri.getBaseUriBuilder().path(Paths.FLASHMOB).build(t.getFlashmob()));
			if (t.getTrigger() != null) {
				j.put(TRIGGERS_KEY, uri.getAbsolutePathBuilder().path(uri.getPath()).path(Paths.TRIGGERS).build());
			}
			if (t.getSignal() != null) {
				j.put(SIGNAL_KEY, uri.getAbsolutePathBuilder().path(uri.getPath()).path(Paths.SIGNAL).build());
			}
			
			JSONArray roles = new JSONArray();
			for (Role r : t.getRoles()) {
				roles.put(renc.encodeAsReference(r, uri));
			}
			j.put(ROLES_KEY, roles);
		}
		if (t.getTitle() != null) {
			j.put(TITLE_KEY, t.getTitle());
		}
		if (t.getDescription() != null) {
			j.put(DESCRIPTION_KEY, t.getDescription());
		}
		return j;
	}

	@Override
	public JSONObject encodeAsReference(Activity t, UriInfo uriInfo) throws JSONException {
		//TODO check if its an activity of an user (different urls)
		//Paths.ACTIVITIES_OF_FLASHMOB
		//Paths.ACTIVITIES_OF_FLASHMOB_OF_USER
		//Paths.ACTIVITIES_OF_ROLE_OF_FLASHMOB
		return new JSONObject().put(HREF_KEY, uriInfo.getBaseUriBuilder().path(Paths.ACTIVITY_OF_FLASHMOB).build(t.getFlashmob(),t)).put(ID_KEY, t);
	}


}
