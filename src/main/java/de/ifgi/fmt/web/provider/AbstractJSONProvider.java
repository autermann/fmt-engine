package de.ifgi.fmt.web.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.core.util.ReaderWriter;

import de.ifgi.fmt.json.JSONDecoder;
import de.ifgi.fmt.json.JSONEncoder;
import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.utils.Utils;

public class AbstractJSONProvider<T> extends AbstractReaderWriterProvider<T> {

	private JSONEncoder<T> enc;
	private JSONDecoder<T> dec;

	public AbstractJSONProvider(Class<T> itemClass) {
		super(itemClass, MediaType.APPLICATION_JSON_TYPE);
		this.enc = JSONFactory.getEncoder(itemClass);
		this.dec = JSONFactory.getDecoder(itemClass);
	}
	
	@Override
	public T readFrom(Class<T> t, Type gt, Annotation[] a, MediaType mt,
			MultivaluedMap<String, String> h, InputStream is)
			throws IOException, WebApplicationException {
		try {
			return dec.decode(new JSONObject(IOUtils.toString(is)));
		} catch (JSONException e) {
			throw new WebApplicationException(e, Status.BAD_REQUEST);
		}
	}

	@Override
	public void writeTo(T o, Class<?> t, Type gt, Annotation[] a, MediaType mt,
			MultivaluedMap<String, Object> h, OutputStream os)
			throws IOException, WebApplicationException {
		try {
			JSONObject j = enc.encode(o, getUriInfo());
			ReaderWriter.writeToAsString(Utils.toString(j), os, mt);
		} catch (JSONException e) {
			throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
		}
	}


}
