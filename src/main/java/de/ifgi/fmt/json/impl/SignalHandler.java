package de.ifgi.fmt.json.impl;

import static de.ifgi.fmt.utils.constants.JSONConstants.ID_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TYPE_KEY;

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.model.signal.SoundSignal;
import de.ifgi.fmt.model.signal.TextSignal;
import de.ifgi.fmt.model.signal.VibrationSignal;

public class SignalHandler extends JSONHandler<Signal> {

	@Override
	public Signal decode(JSONObject j) throws JSONException {
		String type = j.getString(TYPE_KEY);
		Signal s = null;
		if (type.equalsIgnoreCase("sound"))
			s = new SoundSignal();
		else if (type.equalsIgnoreCase("text"))
			s = new TextSignal();
		else if (type.equalsIgnoreCase("vibration"))
			s = new VibrationSignal();
		else
			throw ServiceError.badRequest(String.format("Signal type %s is unknown.", type));
		return s.decode(j);
	}

	@Override
	public JSONObject encode(Signal t, UriInfo uri) throws JSONException {
		JSONObject j = new JSONObject()
			.put(ID_KEY, t.getId())
			.put(TYPE_KEY, t.getType());
		t.encode(j);
		return j;
	}

	@Override
	public JSONObject encodeAsReference(Signal t, UriInfo uriInfo)
			throws JSONException {
		/* do we really need this one? */
		// TODO
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
