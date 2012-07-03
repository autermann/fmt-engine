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
import static de.ifgi.fmt.utils.constants.JSONConstants.TYPE_KEY;

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.model.signal.SoundSignal;
import de.ifgi.fmt.model.signal.TextSignal;
import de.ifgi.fmt.model.signal.VibrationSignal;
import de.ifgi.fmt.utils.constants.RESTConstants;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Encodes(Signal.class)
@Decodes(Signal.class)
public class SignalHandler extends JSONHandler<Signal> {

    /**
     * 
     * @param j
     * @return
     * @throws JSONException
     */
    @Override
	public Signal decode(JSONObject j) throws JSONException {
		String type = j.getString(TYPE_KEY);
		
		Signal s = null;
		if (type.equalsIgnoreCase("sound"))
			s = new SoundSignal();
		else if (type.equalsIgnoreCase("text"))
			s = new TextSignal();
		else if (type.equalsIgnoreCase("vibration"))
			s = new VibrationSignal();
		else
			throw ServiceError.badRequest(String.format("Signal type %s is unknown.", type));
		return s.setId(parseId(j)).decode(j);
	}

    /**
     * 
     * @param t
     * @param uri
     * @return
     * @throws JSONException
     */
    @Override
	public JSONObject encode(Signal t, UriInfo uri) throws JSONException {
		JSONObject j = new JSONObject()
			.put(ID_KEY, t.getId())
			.put(TYPE_KEY, t.getType());
		t.encode(j);
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
	public JSONObject encodeAsRef(Signal t, UriInfo uriInfo) throws JSONException {
		return new JSONObject()
			.put(ID_KEY, t.getId())
			.put(HREF_KEY, uriInfo.getAbsolutePathBuilder().path(RESTConstants.Paths.SIGNAL).build());
	}

}
