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
import de.ifgi.fmt.json.JSONEncoder;
import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.task.LineTask;
import de.ifgi.fmt.model.task.LinkTask;
import de.ifgi.fmt.model.task.Task;

public class TaskHandler extends JSONHandler<Task> {

	@Override
	public Task decode(JSONObject j) throws JSONException {
		// TODO
		throw new UnsupportedOperationException("Not yet implemented");
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
				j.put(ACTIVITY_KEY, aenc.encodeAsReference(t.getActivity(), uri));
			}
			if (t.getRole() != null) {
				JSONEncoder<Role> renc = JSONFactory.getEncoder(Role.class);
				j.put(ROLE_KEY, renc.encodeAsReference(t.getRole(), uri));
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
	public JSONObject encodeAsReference(Task t, UriInfo uriInfo)
			throws JSONException {
		/* i don't think we need this one */
		// TODO
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
