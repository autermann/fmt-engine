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

@SuppressWarnings("rawtypes")
public class PointConverter extends TypeConverter implements
		SimpleValueConverter {
	private GeometryFactory f = new GeometryFactory();

	public PointConverter() {
		super(Point.class);
	}

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

	@Override
	public Point decode(Class c, Object o, MappedField i)
			throws MappingException {
		if (o == null) {
			return null;
		} else if (o instanceof Point) {
			return (Point) o;
		} else if (o instanceof Point2D) {
			Point2D p = (Point2D) o;
			return f.createPoint(new Coordinate(p.getX(), p.getY()));
		} else if (o instanceof double[]) {
			double[] a = (double[]) o;
			return f.createPoint(new Coordinate(a[0], a[1]));
		} else if (o instanceof BasicDBList) {
			BasicDBList list = (BasicDBList) o;
			return f.createPoint(new Coordinate((Double) list.get(0),
					(Double) list.get(1)));
		} else {
			throw new RuntimeException("Can not decode "
					+ o.getClass().toString());
		}
	}

}