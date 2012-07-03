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

import java.net.URI;
import java.net.URISyntaxException;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.uncertweb.api.gml.io.JSONGeometryDecoder;
import org.uncertweb.api.gml.io.JSONGeometryEncoder;

import com.vividsolutions.jts.geom.Geometry;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.User;

/**
 * 
 * @author Autermann, Demuth, Radtke
 * @param <T>
 */
public abstract class JSONHandler<T> implements JSONDecoder<T>, JSONEncoder<T> {

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
		return null;
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
	protected <E extends Enum<E>> E parseEnum(JSONObject j, Class<E> e, String key) {
		String s = j.optString(key);
		if (s == null) 
			return null;
		try {
			return Enum.valueOf(e, s);
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
	protected <G extends Geometry> G parseGeometry(JSONObject j, Class<G> gt, String key) {
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
		throw ServiceError.badRequest("only "+gt.getCanonicalName()+" are allowed");
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
	
	/**
	 * 
	 * @return
	 */
	protected DateTimeFormatter getDateTimeFormat() {
		return dtf;
	}

	/**
	 * 
	 * @return
	 */
	protected JSONGeometryDecoder getGeometryDecoder() {
		return geomdec;
	}

	/**
	 * 
	 * @return
	 */
	protected JSONGeometryEncoder getGeometryEncoder() {
		return geomenc;
	}
}
