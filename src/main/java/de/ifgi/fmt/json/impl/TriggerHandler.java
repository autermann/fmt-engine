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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.json.JSONHandler.DefaultView;
import de.ifgi.fmt.model.Trigger;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.utils.constants.RESTConstants.View;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Encodes(Trigger.class)
@Decodes(Trigger.class)
@DefaultView(View.TRIGGER_OF_FLASHMOB)
public class TriggerHandler extends JSONHandler<Trigger> {

    @Override
	public Trigger decode(JSONObject j) throws JSONException {
		final Trigger t = new Trigger();
		t.setDescription(j.optString(DESCRIPTION_KEY, null));
		t.setLocation(parseGeometry(j, Point.class, LOCATION_KEY));
		t.setTime(parseTime(j, TIME_KEY));
		return t.setId(parseId(j));
	}

	@Override
	protected void encodeObject(JSONObject j, Trigger t, UriInfo uri) throws JSONException {
		j.put(ID_KEY, t.getId());
		switch(t.getView()) {
		case TRIGGER_OF_FLASHMOB:
			j.put(FLASHMOB_KEY, encode(t, t.getFlashmob(), uri));
			j.put(TIME_KEY, encodeTime(t.getTime()));
			j.put(DESCRIPTION_KEY, t.getDescription());
			j.put(LOCATION_KEY, encodeGeometry(t.getLocation()));
			break;
		default:
			if (uri != null) {
				j.put(HREF_KEY, uri.getBaseUriBuilder().path(Paths.TRIGGER_OF_FLASHMOB).build(t.getFlashmob(), t.getId()));
			}
		}
	}

	@Override
	protected void encodeUris(JSONObject j, Trigger t, UriInfo uri) throws JSONException {/* empty */}

}
