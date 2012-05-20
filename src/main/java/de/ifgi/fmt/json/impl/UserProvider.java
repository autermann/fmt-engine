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

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;

@Encodes(User.class)
@Decodes(User.class)
public class UserProvider implements JSONHandler<User> {

	@Override
	public User decode(JSONObject j) throws JSONException {
		// TODO
		throw new UnsupportedOperationException("Not yet implemented");

	}

	@Override
	public JSONObject encode(User t, UriInfo uri) throws JSONException {
		// TODO
		throw new UnsupportedOperationException("Not yet implemented");

	}

	@Override
	public JSONObject encodeAsReference(User t, UriInfo uriInfo)
			throws JSONException {

		return new JSONObject().put(JSONConstants.ID_KEY, t.getId()).put(
				JSONConstants.HREF_KEY,
				uriInfo.getBaseUriBuilder().path(Paths.USER).build(t));
	}
}
