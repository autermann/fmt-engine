package de.ifgi.fmt.json.impl;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;

@Decodes(Flashmob.class)
@Encodes(Flashmob.class)
public class FlashmobHandler implements JSONHandler<Flashmob> {

	@Override
	public Flashmob decode(JSONObject j) throws JSONException {
		// TODO Auto-generated method stub
		Flashmob f = new Flashmob();
		f.setDescription(j.optString(JSONConstants.DESCRIPTION_KEY));
		return null;
	}

	@Override
	public JSONObject encode(Flashmob t, UriInfo uri) throws JSONException {
		// TODO Auto-generated method stub
		URI activities = uri.getBaseUriBuilder().path(Paths.ACTIVITIES_OF_FLASHMOB).build(t.getId());
		JSONObject j = new JSONObject()
			.put(JSONConstants.ACTIVITIES_KEY, activities);
		return j;
	}

	@Override
	public JSONObject encodeAsReference(Flashmob t, UriInfo uriInfo)
			throws JSONException {
		return new JSONObject()
			.put(JSONConstants.HREF_KEY, uriInfo.getBaseUriBuilder().path(Paths.FLASHMOB).build(t.getId()))
			.put(JSONConstants.ID_KEY, t.getId());
	}

}
