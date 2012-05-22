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

import static de.ifgi.fmt.utils.constants.JSONConstants.ACTIVITY_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.DESCRIPTION_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.HREF_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ID_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.LINE_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ROLE_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TYPE_KEY;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriInfo;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.vividsolutions.jts.geom.LineString;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.json.JSONEncoder;
import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.task.LineTask;
import de.ifgi.fmt.model.task.LinkTask;
import de.ifgi.fmt.model.task.LinkTask.Type;
import de.ifgi.fmt.model.task.Task;

@Encodes(Task.class)
@Decodes(Task.class)
public class TaskHandler extends JSONHandler<Task> {

	@Override
	public Task decode(JSONObject j) throws JSONException {
		Task t = null;
		String href = j.optString(HREF_KEY, null);
		String line = j.optString(LINE_KEY, null);
		String type = j.optString(TYPE_KEY, null);
		if (line != null && (href != null || type != null)) {
			throw ServiceError.badRequest("tasks can only hold a link or a line");
		}
		if (line != null) {
			LineTask lt = new LineTask();
			try {
				lt.setLine((LineString) getGeometryDecoder().parseUwGeometry(line));
			} catch (Exception e) {
				throw ServiceError.badRequest(e);
			}
			t = lt;
		} else if (href != null || type != null) {
			LinkTask lt = new LinkTask();
			if (href != null) {
				try {
					lt.setLink(new URI(href));
				} catch (URISyntaxException e) {
					throw ServiceError.badRequest(e);
				}
			}
			if (type != null) {
				try {
					lt.setType(Type.valueOf(type));
				} catch(IllegalArgumentException e) {
					throw ServiceError.badRequest(e);
				}
			}
			t = lt;
		} else {
			t = new Task();
		}
		
		
		String id = j.optString(ID_KEY);
		if (id != null) {
			t.setId(new ObjectId(id));
		}
		t.setDescription(j.optString(DESCRIPTION_KEY, null));
		
		return t;
	}

	@Override
	public JSONObject encode(Task t, UriInfo uri) throws JSONException {
		JSONObject j = new JSONObject().put(ID_KEY, t.getId());
		if (t.getDescription() != null) {
			j.put(DESCRIPTION_KEY, t.getDescription());
		}
		if (uri != null) {
			if (t.getActivity() != null) {
				JSONEncoder<Activity> aenc = JSONFactory.getEncoder(Activity.class);
				j.put(ACTIVITY_KEY, aenc.encodeAsRef(t.getActivity(), uri));
			}
			if (t.getRole() != null) {
				JSONEncoder<Role> renc = JSONFactory.getEncoder(Role.class);
				j.put(ROLE_KEY, renc.encodeAsRef(t.getRole(), uri));
			}
		}
		
		if (t instanceof LineTask) {
			LineString ls = ((LineTask) t).getLine();
			if (ls != null) {
				try {
					j.put(LINE_KEY, new JSONObject(getGeometryEncoder().encodeGeometry(ls)));
				} catch (Exception e) {
					throw ServiceError.internal(e);
				}
			}
		} else if (t instanceof LinkTask) {
			LinkTask lt = (LinkTask) t;
			if (lt.getType() != null) {
				j.put(TYPE_KEY, lt.getType());
			}
			if (lt.getLink() != null) {
				j.put(HREF_KEY, lt.getLink());
			}
		}
		
		return j;
	}

	@Override
	public JSONObject encodeAsRef(Task t, UriInfo uriInfo)
			throws JSONException {
		/* i don't think we need this one */
		// TODO task as ref encoding
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
