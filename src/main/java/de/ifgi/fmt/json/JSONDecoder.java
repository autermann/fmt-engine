package de.ifgi.fmt.json;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public interface JSONDecoder<T> {

	public T decode(JSONObject j) throws JSONException;
	
}
