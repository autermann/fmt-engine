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
import static de.ifgi.fmt.utils.constants.JSONConstants.LOCATION_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TIME_KEY;

import javax.ws.rs.core.UriInfo;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.trigger.EventTrigger;
import de.ifgi.fmt.model.trigger.LocationTrigger;
import de.ifgi.fmt.model.trigger.TimeTrigger;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.utils.Utils;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;

@Encodes(Trigger.class)
@Decodes(Trigger.class)
public class TriggerHandler extends JSONHandler<Trigger> {

	@Override
	public Trigger decode(JSONObject j) throws JSONException {
		Trigger t = null;
		String time = j.optString(TIME_KEY, null);
		String geom = j.optString(LOCATION_KEY, null);
		String desc = j.optString(DESCRIPTION_KEY, null);
		
		if (Utils.moreThanOneNotNull(time, geom, desc)) {
			throw ServiceError.badRequest(String.format(
					"Only one of %s, %s and %s can be present", 
					TIME_KEY, LOCATION_KEY, DESCRIPTION_KEY));
		}
		
		if (time != null) {
			TimeTrigger tt = new TimeTrigger();
			try {
				tt.setTime(getDateTimeFormat().parseDateTime(time));
			} catch (Exception e) {
				throw ServiceError.badRequest(e);
			}
			t = tt;
		} else if (geom != null) {
			LocationTrigger tt = new LocationTrigger();
			try {
				tt.setLocation((Point) getGeometryDecoder().parseUwGeometry(geom));
			} catch (Exception e) {
				throw ServiceError.badRequest(e);
			}
			t = tt;
		} else if (desc != null) {
			EventTrigger tt = new EventTrigger();
			tt.setDescription(desc);
			t = tt;
		} else {
			throw ServiceError.badRequest("trigger not specified");
		}
		
		String id = j.optString(ID_KEY);
		if (id != null) {
			t.setId(new ObjectId(id));
		}
		return t;
	}

	@Override
	public JSONObject encode(Trigger t, UriInfo uri) throws JSONException {
		JSONObject j = new JSONObject().put(ID_KEY, t.getId());
		if (uri != null) {
			if (t.getFlashmob() != null) {
				j.put(FLASHMOB_KEY, JSONFactory.getEncoder(Flashmob.class)
						.encodeAsRef(t.getFlashmob(), uri));
			}
		}
		if (t instanceof TimeTrigger) {
			j.put(TIME_KEY, getDateTimeFormat().print(((TimeTrigger) t).getTime()));
		} else if (t instanceof EventTrigger) {
			j.put(DESCRIPTION_KEY, ((EventTrigger) t).getDescription());
		} else if (t instanceof LocationTrigger) {
			try {
				j.put(LOCATION_KEY,
						getGeometryEncoder().encodeGeometry(
								((LocationTrigger) t).getLocation()));
			} catch (Exception e) {
				throw ServiceError.internal(e);
			}
		}
		return j;
	}

	@Override
	public JSONObject encodeAsRef(Trigger t, UriInfo uriInfo)
			throws JSONException {
		
		// TODO other uri's
		return new JSONObject().put(ID_KEY, t.getId()).put(HREF_KEY,
				uriInfo.getBaseUriBuilder().path(Paths.TRIGGER_OF_FLASHMOB).build(t.getFlashmob(), t));
	}

}
