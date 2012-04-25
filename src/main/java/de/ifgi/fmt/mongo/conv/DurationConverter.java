package de.ifgi.fmt.mongo.conv;

import org.joda.time.Duration;

import com.google.code.morphia.converters.SimpleValueConverter;
import com.google.code.morphia.converters.TypeConverter;
import com.google.code.morphia.mapping.MappedField;
import com.google.code.morphia.mapping.MappingException;

@SuppressWarnings("rawtypes")
public class DurationConverter extends TypeConverter implements
    SimpleValueConverter {

	public DurationConverter() {
		super(Duration.class);
	}

	@Override
	public Object encode(Object value, MappedField optionalExtraInfo) {
		if (value == null)
			return null;
		Duration dt = (Duration) value;
		return dt.getMillis();
	}

	@Override
	public Object decode(Class c, Object o, MappedField i)
	    throws MappingException {
		if (o == null) {
			return null;
		} else if (o instanceof Duration) {
			return o;
		} else if (o instanceof Number) {
			return new Duration(((Number) o).longValue());
		} else {
			return new Duration(o);
		}
	}
}