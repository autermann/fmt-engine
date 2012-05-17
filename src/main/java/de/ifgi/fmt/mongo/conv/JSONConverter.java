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
package de.ifgi.fmt.mongo.conv;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.converters.SimpleValueConverter;
import com.google.code.morphia.converters.TypeConverter;
import com.google.code.morphia.mapping.MappedField;
import com.google.code.morphia.mapping.MappingException;
import com.mongodb.util.JSON;

@SuppressWarnings("rawtypes")
public class JSONConverter extends TypeConverter implements
    SimpleValueConverter {
	private static final Logger log = LoggerFactory.getLogger(JSONObject.class);

	public JSONConverter() {
		super(JSONObject.class);
		log.info("Creating JSONConverter");
	}

	@Override
	public Object encode(Object value, MappedField optionalExtraInfo) {
		if (value == null)
			return null;
		String s = ((JSONObject) value).toString();
		log.debug("Encoding JSON: {}", s);
		return JSON.parse(s);
	}

	@Override
	public Object decode(Class c, Object o, MappedField i)
	    throws MappingException {
		if (o == null)
			return null;
		try {
			String s = JSON.serialize(o);
			log.debug("Decoded JSON: {}", s);
			return new JSONObject(s);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}