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

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.vividsolutions.jts.geom.LineString;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.json.JSONHandler.DefaultView;
import de.ifgi.fmt.model.task.LineTask;
import de.ifgi.fmt.model.task.LinkTask;
import de.ifgi.fmt.model.task.LinkTask.Type;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.utils.constants.RESTConstants.View;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Encodes(Task.class)
@Decodes(Task.class)
@DefaultView(View.TASK_OF_ROLE_OF_ACTIVITY_OF_FLASHMOB)
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
			t = new LineTask().setLine(parseGeometry(j, LineString.class, LINE_KEY));
		} else if (href != null || type != null) {
			if (href != null && type == null) {
				throw ServiceError.badRequest("property 'type' is missing");
			}
			t = new LinkTask()
				.setLink(parseURI(j, HREF_KEY))
				.setType(parseEnum(j, Type.class, TYPE_KEY));
		} else {
			t = new Task();
		}
		
		t.setId(parseId(j));
		t.setDescription(j.optString(DESCRIPTION_KEY, null));
		
		return t;
	}

	@Override
	protected void encodeObject(JSONObject j, Task t, UriInfo uri)
			throws JSONException {
		j.put(ID_KEY, t.getId());
		j.put(DESCRIPTION_KEY, t.getDescription());
		j.put(ACTIVITY_KEY, encode(t, t.getActivity(), uri));
		j.put(ROLE_KEY, encode(t, t.getRole(), uri));
		
		if (t instanceof LineTask) {
			j.put(LINE_KEY, encodeGeometry(((LineTask) t).getLine()));
		} else if (t instanceof LinkTask) {
			LinkTask lt = (LinkTask) t;
			j.put(TYPE_KEY, lt.getType());
			j.put(HREF_KEY, lt.getLink());
		}
	}

	@Override
	protected void encodeUris(JSONObject j, Task t, UriInfo uri)
			throws JSONException {/* empty */}

}
