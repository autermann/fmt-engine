package de.ifgi.fmt.json;

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public interface JSONEncoder<T> {

	public JSONObject encode(T t, UriInfo uri) throws JSONException;
	public JSONObject encodeAsReference(T t, UriInfo uriInfo) throws JSONException;
}
