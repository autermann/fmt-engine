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

import static de.ifgi.fmt.utils.constants.JSONConstants.EMAIL_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.FLASHMOBS_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.PASSWORD_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.USERNAME_KEY;

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.json.JSONHandler.DefaultView;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.utils.constants.RESTConstants.View;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Encodes(User.class)
@Decodes(User.class)
@DefaultView(View.USER)
public class UserHandler extends JSONHandler<User> {

	@Override
	public User decode(JSONObject j) throws JSONException {
		User u = new User().setEmail(j.optString(EMAIL_KEY, null))
				.setPassword(j.optString(PASSWORD_KEY, null))
				.setUsername(j.optString(USERNAME_KEY, null));
		return u;
	}

	@Override
	protected void encodeObject(JSONObject j, User t, UriInfo uri)
			throws JSONException {
		j.put(USERNAME_KEY, t.getUsername());
		if (t.getView() == View.USER) {
			j.put(EMAIL_KEY, t.getEmail());
		} else if (uri != null) {
			j.put(JSONConstants.HREF_KEY,
					uri.getBaseUriBuilder().path(Paths.USER).build(t));
		}
	}

	@Override
	protected void encodeUris(JSONObject j, User t, UriInfo uri)
			throws JSONException {
		if (t.getView() == View.USER) {
			j.put(FLASHMOBS_KEY,
					uri.getBaseUriBuilder().path(Paths.FLASHMOBS_OF_USER)
							.build(t.getUsername()));
		}
	}
}
