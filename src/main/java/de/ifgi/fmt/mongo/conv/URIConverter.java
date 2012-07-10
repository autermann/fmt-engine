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

import java.net.URI;
import java.net.URISyntaxException;

import com.google.code.morphia.converters.SimpleValueConverter;
import com.google.code.morphia.converters.TypeConverter;
import com.google.code.morphia.mapping.MappedField;
import com.google.code.morphia.mapping.MappingException;

import de.ifgi.fmt.ServiceError;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@SuppressWarnings("rawtypes")
public class URIConverter extends TypeConverter implements SimpleValueConverter {
	/**
     * 
     */
	public URIConverter() {
		super(URI.class);
	}

	/**
	 * 
	 * @param value
	 * @param optionalExtraInfo
	 * @return
	 */
	@Override
	public Object encode(Object value, MappedField optionalExtraInfo) {
		if (value == null)
			return null;
		return ((URI) value).toString();
	}

	/**
	 * 
	 * @param c
	 * @param o
	 * @param i
	 * @return
	 * @throws MappingException
	 */
	@Override
	public Object decode(Class c, Object o, MappedField i)
			throws MappingException {
		if (o == null)
			return null;
		try {
			return new URI((String) o);
		} catch (URISyntaxException e) {
			throw ServiceError.internal(e);
		}
	}

}
