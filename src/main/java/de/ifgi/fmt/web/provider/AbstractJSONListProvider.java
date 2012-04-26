package de.ifgi.fmt.web.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.core.util.ReaderWriter;

import de.ifgi.fmt.json.JSONEncoder;
import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.utils.Utils;

public abstract class AbstractJSONListProvider<T> extends
		AbstractWriterProvider<Iterable<T>> {

	private String collectionName;
	private JSONEncoder<T> enc;

	public AbstractJSONListProvider(Class<T> clazz, String collectionName) {
		super(clazz, true, MediaType.APPLICATION_JSON_TYPE);
		this.collectionName = collectionName;
		this.enc = JSONFactory.getEncoder(clazz);
	}

	@Override
	public void writeTo(Iterable<T> list, Class<?> t, Type gt, Annotation[] a,
			MediaType mt, MultivaluedMap<String, Object> h, OutputStream os)
			throws IOException, WebApplicationException {
		try {
			JSONArray array = new JSONArray();
			for (T o : list) {
				array.put(enc.encodeAsReference(o, getUriInfo()));
			}
			JSONObject j = new JSONObject().put(collectionName, array);
			ReaderWriter.writeToAsString(Utils.toString(j), os, mt);
		} catch (JSONException e) {
			throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
		}

	}


}
