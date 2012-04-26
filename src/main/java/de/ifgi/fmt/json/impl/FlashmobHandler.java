package de.ifgi.fmt.json.impl;

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.Flashmob;

public class FlashmobHandler implements JSONHandler<Flashmob> {

	@Override
	public Flashmob decode(JSONObject j) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject encode(Flashmob t, UriInfo uri) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject encodeAsReference(Flashmob t, UriInfo uriInfo)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

}
