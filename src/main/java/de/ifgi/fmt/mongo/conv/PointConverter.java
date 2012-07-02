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

import java.awt.geom.Point2D;

import com.google.code.morphia.converters.SimpleValueConverter;
import com.google.code.morphia.converters.TypeConverter;
import com.google.code.morphia.mapping.MappedField;
import com.google.code.morphia.mapping.MappingException;
import com.mongodb.BasicDBList;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@SuppressWarnings("rawtypes")
public class PointConverter extends TypeConverter implements
		SimpleValueConverter {
	private GeometryFactory f = new GeometryFactory();

	/**
	 * 
	 */
	public PointConverter() {
		super(Point.class);
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
		Point p = (Point) value;
		BasicDBList l = new BasicDBList();
		l.add(p.getCoordinate().x);
		l.add(p.getCoordinate().y);
		return l;
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
	public Point decode(Class c, Object o, MappedField i)
			throws MappingException {
		Point p = null;
		if (o == null) {
			return null;
		} else if (o instanceof Point) {
			return (Point) o;
		} else if (o instanceof Point2D) {
			Point2D a = (Point2D) o;
			p = f.createPoint(new Coordinate(a.getX(), a.getY()));
		} else if (o instanceof double[]) {
			double[] a = (double[]) o;
			p =  f.createPoint(new Coordinate(a[0], a[1]));
		} else if (o instanceof BasicDBList) {
			BasicDBList list = (BasicDBList) o;
			p = f.createPoint(new Coordinate((Double) list.get(0),
					(Double) list.get(1)));
		} else {
			throw new RuntimeException("Can not decode "
					+ o.getClass().toString());
		}
		p.setSRID(4326);
		return p;
	}

}