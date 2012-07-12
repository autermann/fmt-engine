/*
 * Copyright (C) 2012  Christian Autermann, Dustin Demuth, Maurin Radtke
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package de.ifgi.fmt.json;

import static de.ifgi.fmt.utils.constants.JSONConstants.ID_KEY;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriInfo;

import org.apache.xmlbeans.XmlException;
import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncertweb.api.gml.io.JSONGeometryDecoder;
import org.uncertweb.api.gml.io.JSONGeometryEncoder;

import com.vividsolutions.jts.geom.Geometry;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.Signal;
import de.ifgi.fmt.model.Signal.Type;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.model.Viewable;
import de.ifgi.fmt.utils.constants.RESTConstants.View;

/**
 * 
 * @author Autermann, Demuth, Radtke
 * @param <T>
 */
public abstract class JSONHandler<T extends Viewable<T>> implements
		JSONDecoder<T>, JSONEncoder<T> {

	@Documented
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface DefaultView {
		public View value();
	}

	protected static final Logger log = LoggerFactory
			.getLogger(JSONHandler.class);
	private final DateTimeFormatter dtf = ISODateTimeFormat.dateTime();
	private final JSONGeometryEncoder geomenc = new JSONGeometryEncoder();
	private final JSONGeometryDecoder geomdec = new JSONGeometryDecoder();

	/**
	 * 
	 * @param j
	 * @return
	 * @throws JSONException
	 */
	protected ObjectId parseId(JSONObject j) throws JSONException {
		if (hasKeyNotNull(j, ID_KEY)) {
			return new ObjectId(j.getString(ID_KEY));
		}
		return new ObjectId();
	}

	/**
	 * 
	 * @param j
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public boolean isNull(JSONObject j, String key) throws JSONException {
		return j.has(key) && j.get(key) == JSONObject.NULL;
	}

	/**
	 * 
	 * @param j
	 * @param key
	 * @return
	 */
	public boolean hasKeyNotNull(JSONObject j, String key) {
		return !JSONObject.NULL.equals(j.opt(key));
	}

	/**
	 * 
	 * @param j
	 * @param key
	 * @return
	 */
	protected DateTime parseTime(JSONObject j, String key) {
		String time = j.optString(key, null);
		if (time != null) {
			return getDateTimeFormat().parseDateTime(time);
		}
		return null;
	}

	protected String encodeTime(DateTime dt) {
		if (dt == null) {
			return null;
		}
		return getDateTimeFormat().print(dt);
	}

	protected Boolean parseBoolean(JSONObject j, String key) {
		String bool = j.optString(key, null);
		if (bool == null)
			return null;
		return Boolean.valueOf(bool);
	}

	/**
	 * 
	 * @param j
	 * @param key
	 * @return
	 */
	protected User parseUser(JSONObject j, String key) {
		String user = j.optString(key, null);
		if (user != null) {
			return new User().setUsername(user);
		}
		return null;
	}

	/**
	 * 
	 * @param j
	 * @param e
	 * @param key
	 * @return
	 */
	protected <E extends Enum<E>> E parseEnum(JSONObject j, Class<E> e,
			String key) {
		String s = j.optString(key, null);
		if (s == null)
			return null;
		try {
			return Enum.valueOf(e, s.toUpperCase());
		} catch (IllegalArgumentException x) {
			throw ServiceError.badRequest(x);
		}
	}

	/**
	 * 
	 * @param j
	 * @param gt
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <G extends Geometry> G parseGeometry(JSONObject j, Class<G> gt,
			String key) {
		String geom = j.optString(key, null);
		if (geom == null)
			return null;
		try {
			Geometry g = getGeometryDecoder().parseUwGeometry(geom);
			if (gt.isAssignableFrom(g.getClass()))
				return (G) g;
		} catch (Exception e) {
			throw ServiceError.badRequest(e);
		}
		throw ServiceError.badRequest("only " + gt.getCanonicalName()
				+ " are allowed");
	}

	protected JSONObject encodeGeometry(Geometry g) throws JSONException {
		if (g == null) {
			return null;
		}
		try {
			return new JSONObject(getGeometryEncoder().encodeGeometry(g));
		} catch (XmlException e) {
			throw ServiceError.internal(e);
		} catch (org.json.JSONException e) {
			throw ServiceError.internal(e);
		}
	}

	/**
	 * 
	 * @param j
	 * @param key
	 * @return
	 */
	protected URI parseURI(JSONObject j, String key) {
		String s = j.optString(key);
		if (s != null) {
			try {
				return new URI(s);
			} catch (URISyntaxException e) {
				throw ServiceError.badRequest(e);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected <X extends Viewable<X>> JSONObject encode(Viewable<?> parent,
			X t, UriInfo uri) throws JSONException {
		if (t == null || parent == null) {
			return null;
		}
		JSONEncoder<X> enc = JSONFactory.getEncoder(t.getClass());
		return enc.encode(t.setView(parent.getView()), uri);
	}

	private DateTimeFormatter getDateTimeFormat() {
		return dtf;
	}

	private JSONGeometryDecoder getGeometryDecoder() {
		return geomdec;
	}

	private JSONGeometryEncoder getGeometryEncoder() {
		return geomenc;
	}

	private View getDefaultView() {
		DefaultView df = getClass().getAnnotation(DefaultView.class);
		if (df == null)
			return null;
		return df.value();
	}

	@Override
	public JSONObject encode(T t, UriInfo uri) throws JSONException {
		JSONObject j = new JSONObject();
		t.optSetView(getDefaultView());
		log.debug("Encoding {} for View {}.", t.getClass(), t.getView());
		encodeObject(j, t, uri);
		if (uri != null) {
			encodeUris(j, t, uri);
		}
		return j;
	}

	protected abstract void encodeObject(JSONObject j, T t, UriInfo uri)
			throws JSONException;

	protected abstract void encodeUris(JSONObject j, T t, UriInfo uri)
			throws JSONException;

	@Override
	public abstract T decode(JSONObject j) throws JSONException;
	
	
	public static void main(String[] args) throws MalformedURLException, JSONException {
		Signal s = new Signal().setType(Type.SOUND).setView(View.SIGNAL_OF_ACTIVITY);
		System.out.println(JSONFactory.getEncoder(s.getClass()).encode(s, null).toString(4));
	}
}
