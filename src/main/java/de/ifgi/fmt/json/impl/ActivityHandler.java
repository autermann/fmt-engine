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

import javax.ws.rs.core.UriInfo;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.json.JSONEncoder;
import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;

@Decodes(Activity.class)
@Encodes(Activity.class)
public class ActivityHandler extends JSONHandler<Activity> {

	@Override
	public Activity decode(JSONObject j) throws JSONException {
		Activity a = new Activity();
		String id = j.optString(ID_KEY, null);
		if (id != null) {
			a.setId(new ObjectId(id));
		}
		
		a.setDescription(j.optString(DESCRIPTION_KEY, null));
		a.setTitle(j.optString(TITLE_KEY, null));
		
		String flashmob = j.optString(FLASHMOB_KEY, null);
		if (flashmob == null) {
			a.setFlashmob(new Flashmob().setId(new ObjectId(flashmob)));
		}
		String trigger = j.optString(TRIGGER_KEY, null);
		if (trigger != null) {
			a.setTrigger(new Trigger().setId(new ObjectId(trigger)));
		}
		return a;
	}

	@Override
	public JSONObject encode(Activity t, UriInfo uri) throws JSONException {
		JSONObject j = new JSONObject().put(ID_KEY, t.getId());
		if (t.getTitle() != null) {
			j.put(TITLE_KEY, t.getTitle());
		}
		if (t.getDescription() != null) {
			j.put(DESCRIPTION_KEY, t.getDescription());
		}
		
		if (uri != null) {
			if (t.getFlashmob() != null) {
				j.put(FLASHMOB_KEY, JSONFactory.getEncoder(Flashmob.class).encodeAsRef(t.getFlashmob(), uri));
			}
			if (t.getTrigger() != null) {
				j.put(TRIGGER_KEY, JSONFactory.getEncoder(Trigger.class).encodeAsRef(t.getTrigger(), uri));
			}
			if (t.getSignal() != null) {
				j.put(SIGNAL_KEY, JSONFactory.getEncoder(Signal.class).encodeAsRef(t.getSignal(), uri));
			}
			if (t.getRoles() != null) {
				JSONEncoder<Role> renc = JSONFactory.getEncoder(Role.class);
				JSONArray roles = new JSONArray();
				for (Role r : t.getRoles()) {
					roles.put(renc.encodeAsRef(r, uri));
				}
				j.put(ROLES_KEY, roles);
			}
		}
		return j;
	}

	@Override
	public JSONObject encodeAsRef(Activity t, UriInfo uri) throws JSONException {
		return new JSONObject()
			.put(ID_KEY, t)
			.put(HREF_KEY, uri.getAbsolutePathBuilder().path(Paths.ACTIVITY).build(t));
	}


}
