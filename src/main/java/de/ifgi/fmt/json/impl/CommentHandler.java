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

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.json.JSONHandler.DefaultView;
import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.utils.constants.RESTConstants.PathParams;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.utils.constants.RESTConstants.View;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Encodes(Comment.class)
@Decodes(Comment.class)
@DefaultView(View.COMMENT_FOR_FLASHMOB)
public class CommentHandler extends JSONHandler<Comment> {

	@Override
	public Comment decode(JSONObject j) throws JSONException {
		Comment c = new Comment();
		c.setId(parseId(j));
		c.setText(j.optString(TEXT_KEY, null));
		c.setTime(parseTime(j, TIME_KEY));
		c.setUser(parseUser(j, USER_KEY));
		return c;
	}

	@Override
	protected void encodeObject(JSONObject j, Comment t, UriInfo uri)
			throws JSONException {
		j.put(ID_KEY, t.getId());
		if (t.getView() == View.COMMENT_FOR_FLASHMOB) {
			j.put(TIME_KEY, encodeTime(t.getTime()));
			j.put(TEXT_KEY, t.getText());
			j.put(FLASHMOB_KEY, encode(t, t.getFlashmob(), uri));
			j.put(USER_KEY, encode(t, t.getUser(), uri));
		}
	}

	@Override
	protected void encodeUris(JSONObject j, Comment t, UriInfo uri)
			throws JSONException {
		if (t.getView() != View.COMMENT_FOR_FLASHMOB) {
			MultivaluedMap<String, String> map = uri.getPathParameters();
			j.put(HREF_KEY,
					uri.getBaseUriBuilder()
							.path(Paths.COMMENT_FOR_FLASHMOB)
							.build(map.getFirst(PathParams.FLASHMOB), t.getId()));
		}
	}
}
