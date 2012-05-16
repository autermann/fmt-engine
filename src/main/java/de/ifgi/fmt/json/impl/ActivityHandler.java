package de.ifgi.fmt.json.impl;

import static de.ifgi.fmt.utils.constants.JSONConstants.DESCRIPTION_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.FLASHMOB_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.HREF_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ID_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ROLES_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.SIGNAL_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TITLE_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TRIGGER_KEY;

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.json.JSONEncoder;
import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;

@Decodes(Activity.class)
@Encodes(Activity.class)
public class ActivityHandler implements JSONHandler<Activity> {

	@Override
	public Activity decode(JSONObject j) throws JSONException {
		// TODO decoding
		throw ServiceError.internal("Not yet implemented");
	}

	@Override
	public JSONObject encode(Activity t, UriInfo uri) throws JSONException {
		JSONEncoder<Role> renc = JSONFactory.getEncoder(Role.class);
		JSONObject j = new JSONObject().put(ID_KEY, t.getId());
		if (uri != null) {
			j.put(FLASHMOB_KEY, uri.getBaseUriBuilder().path(Paths.FLASHMOB).build(t.getFlashmob()));
			if (t.getTrigger() != null) {
				j.put(TRIGGER_KEY, uri.getBaseUriBuilder().path(Paths.TRIGGER_OF_FLASHMOB).build(t.getFlashmob(),t.getId()));
			}
			if (t.getSignal() != null) {
				j.put(SIGNAL_KEY, uri.getBaseUriBuilder().path(Paths.SIGNALS_OF_ACTIVITY).build(t.getFlashmob(),t));
			}
			
			JSONArray roles = new JSONArray();
			for (Role r : t.getRoles()) {
				roles.put(renc.encodeAsReference(r, uri));
			}
			j.put(ROLES_KEY,roles);
		}
		if (t.getTitle() != null) {
			j.put(TITLE_KEY, t.getTitle());
		}
		if (t.getDescription() != null) {
			j.put(DESCRIPTION_KEY, t.getDescription());
		}
		return j;
	}

	@Override
	public JSONObject encodeAsReference(Activity t, UriInfo uriInfo) throws JSONException {
		//TODO check if its an activity of an user (different urls)
		return new JSONObject().put(HREF_KEY, uriInfo.getBaseUriBuilder().path(Paths.ACTIVITY_OF_FLASHMOB).build(t.getFlashmob(),t)).put(ID_KEY, t);
	}


}
