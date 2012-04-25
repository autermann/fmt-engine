package de.ifgi.fmt.mongo.conv;

import org.joda.time.Interval;

import com.google.code.morphia.converters.SimpleValueConverter;
import com.google.code.morphia.converters.TypeConverter;
import com.google.code.morphia.mapping.MappedField;
import com.google.code.morphia.mapping.MappingException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@SuppressWarnings("rawtypes")
public class IntervalConverter extends TypeConverter implements
    SimpleValueConverter {

	public IntervalConverter() {
		super(Interval.class);
	}

	@Override
	public Object encode(Object value, MappedField optionalExtraInfo) {
		if (value == null)
			return null;
		Interval i = (Interval) value;
		DBObject dbo = new BasicDBObject();
		dbo.put("start", i.getStart().getMillis());
		dbo.put("end", i.getEnd().getMillis());
		return dbo;
	}

	@Override
	public Interval decode(Class c, Object o, MappedField i)
	    throws MappingException {
		if (o == null) {
			return null;
		} else if (o instanceof DBObject) {
			DBObject dbo = (DBObject) o;
			return new Interval(getLong(dbo.get("start")), getLong(dbo.get("end")));
		}
		return new Interval(o);
	}

	private long getLong(Object o) {
		if (o instanceof Number) {
			return ((Number) o).longValue();
		} else if (o instanceof String) {
			return Long.valueOf((String) o);
		} else {
			return ((Long) o).longValue();
		}
	}
}