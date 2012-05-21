package de.ifgi.fmt.json.impl;

import static de.ifgi.fmt.utils.constants.JSONConstants.DESCRIPTION_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.FLASHMOB_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.HREF_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ID_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.LOCATION_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TIME_KEY;

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.trigger.EventTrigger;
import de.ifgi.fmt.model.trigger.LocationTrigger;
import de.ifgi.fmt.model.trigger.TimeTrigger;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;

public class TriggerHandler extends JSONHandler<Trigger> {

	@Override
	public Trigger decode(JSONObject j) throws JSONException {
		// TODO
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public JSONObject encode(Trigger t, UriInfo uri) throws JSONException {
		JSONObject j = new JSONObject().put(ID_KEY, t.getId());
		if (uri != null) {
			if (t.getFlashmob() != null) {
				j.put(FLASHMOB_KEY, JSONFactory.getEncoder(Flashmob.class)
						.encodeAsReference(t.getFlashmob(), uri));
			}
		}
		if (t instanceof TimeTrigger) {
			j.put(TIME_KEY,
					getDateTimeFormat().print(((TimeTrigger) t).getTime()));
		} else if (t instanceof EventTrigger) {
			j.put(DESCRIPTION_KEY, ((EventTrigger) t).getDescription());
		} else if (t instanceof LocationTrigger) {
			try {
				j.put(LOCATION_KEY,
						getGeometryEncoder().encodeGeometry(
								((LocationTrigger) t).getLocation()));
			} catch (Exception e) {
				throw ServiceError.internal(e);
			}
		}
		return j;
	}

	@Override
	public JSONObject encodeAsReference(Trigger t, UriInfo uriInfo)
			throws JSONException {
		
		// TODO other uri's
		return new JSONObject().put(ID_KEY, t.getId()).put(HREF_KEY,
				uriInfo.getBaseUriBuilder().path(Paths.TRIGGER_OF_FLASHMOB).build(t.getFlashmob(), t));
	}

}
