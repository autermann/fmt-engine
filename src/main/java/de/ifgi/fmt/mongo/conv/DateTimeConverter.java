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

import java.util.Date;

import org.joda.time.DateTime;

import com.google.code.morphia.converters.SimpleValueConverter;
import com.google.code.morphia.converters.TypeConverter;
import com.google.code.morphia.mapping.MappedField;
import com.google.code.morphia.mapping.MappingException;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@SuppressWarnings("rawtypes")
public class DateTimeConverter extends TypeConverter implements
		SimpleValueConverter {
	/**
     * 
     */
	public DateTimeConverter() {
		super(DateTime.class);
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
		DateTime dt = (DateTime) value;
		return new Date(dt.getMillis());
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
		if (o == null) {
			return null;
		} else if (o instanceof DateTime) {
			return o;
		} else if (o instanceof Number) {
			return new DateTime(((Number) o).longValue());
		} else if (o instanceof Date) {
			return new DateTime(((Date) o).getTime());
		} else {
			return new DateTime(o);
		}
	}
}