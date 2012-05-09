package de.ifgi.fmt.json.impl;

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.Activity;

@Decodes(Activity.class)
@Encodes(Activity.class)
public class ActivityHandler implements JSONHandler<Activity> {

	@Override
	public Activity decode(JSONObject j) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject encode(Activity t, UriInfo uri) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject encodeAsReference(Activity t, UriInfo uriInfo)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}


}
