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

import static de.ifgi.fmt.utils.constants.JSONConstants.HREF_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ID_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TEXT_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TYPE_KEY;

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.json.JSONHandler.DefaultView;
import de.ifgi.fmt.model.Signal;
import de.ifgi.fmt.utils.constants.RESTConstants;
import de.ifgi.fmt.utils.constants.RESTConstants.View;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Encodes(Signal.class)
@Decodes(Signal.class)
@DefaultView(View.SIGNAL_OF_ACTIVITY)
public class SignalHandler extends JSONHandler<Signal> {

	@Override
	public Signal decode(JSONObject j) throws JSONException {
		Signal s = new Signal();
		s.setText(j.optString(TEXT_KEY, null));
		s.setType(parseEnum(j, Signal.Type.class, TYPE_KEY));
		return s.setId(parseId(j));
	}

	@Override
	protected void encodeObject(JSONObject j, Signal t, UriInfo uri)
			throws JSONException {
		j.put(ID_KEY, t.getId());

		switch (t.getView()) {
		case SIGNAL_OF_ACTIVITY:
		case SIGNAL_OF_ACTIVITY_OF_ROLE_OF_FLASHMOB:
		case SIGNAL_OF_ACTIVITY_OF_FLASHMOB_OF_USER:
			j.put(TYPE_KEY, t.getType());
			j.put(TEXT_KEY, t.getText());
			break;
		case ACTIVITY_OF_FLASHMOB:
		case ACTIVITY_OF_FLASHMOB_OF_USER:
		case ACTIVITY_OF_ROLE_OF_FLASHMOB:
		default:
			if (uri != null) {
				j.put(HREF_KEY,
						uri.getAbsolutePathBuilder()
								.path(RESTConstants.Paths.SIGNAL).build());
			}
		}
	}

	@Override
	protected void encodeUris(JSONObject j, Signal t, UriInfo uri)
			throws JSONException {
	}

}
