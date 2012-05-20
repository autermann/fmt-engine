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
import de.ifgi.fmt.model.Comment;

@Encodes(Comment.class)
@Decodes(Comment.class)
public class CommentHandler implements JSONHandler<Comment> {

	@Override
	public Comment decode(JSONObject j) throws JSONException {
		//TODO
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public JSONObject encode(Comment t, UriInfo uri) throws JSONException {
		//TODO
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public JSONObject encodeAsReference(Comment t, UriInfo uriInfo)
			throws JSONException {
		//TODO
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
