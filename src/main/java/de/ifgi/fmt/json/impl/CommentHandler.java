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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.json.JSONEncoder;
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
		// TODO
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public JSONObject encode(Comment t, UriInfo uri) throws JSONException {
		JSONObject j = new JSONObject().put(ID_KEY, t.getId());
		if (uri != null) {
			if (t.getFlashmob() != null) {
				JSONEncoder<Flashmob> fenc = JSONFactory
						.getEncoder(Flashmob.class);
				j.put(FLASHMOB_KEY,
						fenc.encodeAsReference(t.getFlashmob(), uri));
			}
			if (t.getUser() != null) {
				JSONEncoder<User> uenc = JSONFactory.getEncoder(User.class);
				j.put(USER_KEY, uenc.encodeAsReference(t.getUser(), uri));
			}
		}

		j.put(TIME_KEY, getDateTimeFormat().print(t.getTime()));
		j.put(TEXT_KEY, t.getText());

		return j;
	}

	@Override
	public JSONObject encodeAsReference(Comment t, UriInfo uriInfo)
			throws JSONException {
		return new JSONObject().put(ID_KEY, t.getId()).put(HREF_KEY,
				uriInfo.getAbsolutePathBuilder().path(Paths.COMMENT).build(t));
	}

}
