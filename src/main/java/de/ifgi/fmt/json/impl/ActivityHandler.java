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
import static de.ifgi.fmt.utils.constants.JSONConstants.TRIGGER_KEY;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.json.JSONHandler.DefaultView;
import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.utils.constants.RESTConstants.PathParams;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.utils.constants.RESTConstants.View;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Decodes(Activity.class)
@Encodes(Activity.class)
@DefaultView(View.ACTIVITY_OF_FLASHMOB)
public class ActivityHandler extends JSONHandler<Activity> {

	@Override
	public Activity decode(JSONObject j) throws JSONException {
		Activity a = new Activity();
		a.setId(parseId(j));
		a.setDescription(j.optString(DESCRIPTION_KEY, null));
		a.setTitle(j.optString(TITLE_KEY, null));
		return a;
	}

	@Override
	protected void encodeObject(JSONObject j, Activity t, UriInfo uri)
			throws JSONException {
		j.put(ID_KEY, t.getId());

		switch (t.getView()) {
		case ACTIVITY_OF_FLASHMOB:
		case ACTIVITY_OF_FLASHMOB_OF_USER:
		case ACTIVITY_OF_ROLE_OF_FLASHMOB:
			j.put(TITLE_KEY, t.getTitle());
			j.put(DESCRIPTION_KEY, t.getDescription());
			j.put(FLASHMOB_KEY, encode(t, t.getFlashmob(), uri));
			j.put(TRIGGER_KEY, encode(t, t.getTrigger(), uri));
			j.put(SIGNAL_KEY, encode(t, t.getSignal(), uri));

			JSONArray roles = new JSONArray();
			for (Role r : t.getRoles())
				roles.put(encode(t, r, uri));
			j.put(ROLES_KEY, roles);
			break;

		case ACTIVITIES_OF_FLASHMOB:
			if (uri != null) {
				MultivaluedMap<String, String> map = uri.getPathParameters();
				j.put(HREF_KEY,
						uri.getBaseUriBuilder()
								.path(Paths.ACTIVITY_OF_FLASHMOB)
								.build(map.getFirst(PathParams.FLASHMOB),
										t.getId()));
			}
			break;
		case ACTIVITIES_OF_FLASHMOB_OF_USER:
			if (uri != null) {
				MultivaluedMap<String, String> map = uri.getPathParameters();
				j.put(HREF_KEY,
						uri.getBaseUriBuilder()
								.path(Paths.ACTIVITY_OF_FLASHMOB_OF_USER)
								.build(map.getFirst(PathParams.USER),
										map.getFirst(PathParams.FLASHMOB),
										t.getId()));
			}
			break;
		case ACTIVITIES_OF_ROLE_OF_FLASHMOB:
			if (uri != null) {
				MultivaluedMap<String, String> map = uri.getPathParameters();
				j.put(HREF_KEY,
						uri.getBaseUriBuilder()
								.path(Paths.ACTIVITY_OF_ROLE_OF_FLASHMOB)
								.build(map.getFirst(PathParams.FLASHMOB),
										map.getFirst(PathParams.ROLE),
										t.getId()));
			}
			break;
		}
	}

	@Override
	protected void encodeUris(JSONObject j, Activity t, UriInfo uri)
			throws JSONException {
	}

}
