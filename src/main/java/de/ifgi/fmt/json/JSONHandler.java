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

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.uncertweb.api.gml.io.JSONGeometryDecoder;
import org.uncertweb.api.gml.io.JSONGeometryEncoder;

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
