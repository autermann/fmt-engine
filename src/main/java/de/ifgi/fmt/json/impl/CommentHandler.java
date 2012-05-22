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

import static de.ifgi.fmt.utils.constants.JSONConstants.FLASHMOB_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.HREF_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ID_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TEXT_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TIME_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.USER_KEY;

import javax.ws.rs.core.UriInfo;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;

@Encodes(Comment.class)
@Decodes(Comment.class)
public class CommentHandler extends JSONHandler<Comment> {

	@Override
	public Comment decode(JSONObject j) throws JSONException {
		Comment c = new Comment();
		String id = j.optString(ID_KEY, null);
		if (id != null) {
			c.setId(new ObjectId(id));
		}
		c.setText(j.optString(TEXT_KEY, null));

		String flashmob = j.optString(FLASHMOB_KEY, null);
		if (flashmob != null) {
			c.setFlashmob(new Flashmob(flashmob));
		}

		String time = j.optString(TIME_KEY, null);
		if (time != null) {
			c.setTime(getDateTimeFormat().parseDateTime(time));
		}
		
		String user = j.optString(USER_KEY, null);
		if (user != null) {
			c.setUser(new User(user));
		}
		return c;
	}

	@Override
	public JSONObject encode(Comment t, UriInfo uri) throws JSONException {
		JSONObject j = new JSONObject().put(ID_KEY, t.getId());
		if (t.getTime() != null) {
			j.put(TIME_KEY, getDateTimeFormat().print(t.getTime()));
		}
		if (t.getText() != null) {
			j.put(TEXT_KEY, t.getText());
		}
		if (uri != null) {
			if (t.getFlashmob() != null) {
				j.put(FLASHMOB_KEY, JSONFactory.getEncoder(Flashmob.class).encodeAsRef(t.getFlashmob(), uri));
			}
			if (t.getUser() != null) {
				j.put(USER_KEY, JSONFactory.getEncoder(User.class).encodeAsRef(t.getUser(), uri));
			}
		}			

		return j;
	}

	@Override
	public JSONObject encodeAsRef(Comment t, UriInfo uriInfo)
			throws JSONException {
		return new JSONObject()
			.put(ID_KEY, t.getId())
			.put(HREF_KEY, uriInfo.getAbsolutePathBuilder().path(Paths.COMMENT).build(t));
	}

}
