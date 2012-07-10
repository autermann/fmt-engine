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

import com.google.code.morphia.converters.SimpleValueConverter;
import com.google.code.morphia.converters.TypeConverter;
import com.google.code.morphia.mapping.MappedField;
import com.google.code.morphia.mapping.MappingException;
import com.mongodb.BasicDBList;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@SuppressWarnings("rawtypes")
public class LineStringConverter extends TypeConverter implements
		SimpleValueConverter {
	private GeometryFactory f = new GeometryFactory();
	private PointConverter pc = new PointConverter();

	/**
	 * 
	 */
	public LineStringConverter() {
		super(LineString.class);
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
		LineString ls = (LineString) value;
		BasicDBList line = new BasicDBList();
		for (Coordinate c : ls.getCoordinates()) {
			BasicDBList l = new BasicDBList();
			l.add(c.x);
			l.add(c.y);
			line.add(l);
		}
		return line;
	}

	/**
	 * 
	 * @param mc
	 * @param o
	 * @param m
	 * @return
	 * @throws MappingException
	 */
	@Override
	public LineString decode(Class mc, Object o, MappedField m)
			throws MappingException {
		if (o == null) {
			return null;
		} else if (o instanceof LineString) {
			return (LineString) o;
		} else if (o instanceof double[][]) {
			double[][] a = (double[][]) o;
			Coordinate[] c = new Coordinate[a.length];
			for (int i = 0; i < c.length; ++i)
				c[i] = new Coordinate(a[i][0], a[i][1]);
			return f.createLineString(c);
		} else if (o instanceof BasicDBList) {
			BasicDBList list = (BasicDBList) o;
			Coordinate[] c = new Coordinate[list.size()];
			for (int i = 0; i < c.length; ++i)
				c[i] = pc.decode(Point.class, list.get(i), null)
						.getCoordinate();
			return f.createLineString(c);
		} else {
			throw new RuntimeException("Can not decode "
					+ o.getClass().toString());
		}
	}
}
