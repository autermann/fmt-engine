package de.ifgi.fmt.json;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.utils.Implementations;
import de.ifgi.fmt.utils.Stringifier;
import de.ifgi.fmt.utils.Utils;

@SuppressWarnings("unchecked")
public class JSONFactory {

	@Documented
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Decodes {
		public Class<?> value();
	}

	@Documented
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Encodes {
		public Class<?> value();
	}

	protected static final Logger log = LoggerFactory.getLogger(JSONFactory.class);
	
	private static Map<Class<?>, JSONEncoder<?>> encoders = Utils.map();
	private static Map<Class<?>, JSONDecoder<?>> decoders = Utils.map();

	static {
		for (Class<?> c : Implementations.getAnnotatedSubclasses(JSONEncoder.class, Encodes.class)) {
			try {
				JSONEncoder<?> enc = (JSONEncoder<?>) c.newInstance();
				encoders.put(c.getAnnotation(Encodes.class).value(), enc);
			} catch (Exception e) {
				log.error("Can not instantiate encoder!", e);
			}
		}
		for (Class<?> c : Implementations.getAnnotatedSubclasses(JSONDecoder.class, Decodes.class)) {
			try {
				JSONDecoder<?> dec = (JSONDecoder<?>) c.newInstance();
				decoders.put(c.getAnnotation(Decodes.class).value(), dec);
			} catch (Exception e) {
				log.error("Can not instantiate decoder!", e);
			}
		}
		if (log.isDebugEnabled()) {
			if (log.isDebugEnabled()) {
				log.debug("JSON decoder classes found:\n{}", Utils.join(new Stringifier() {
					public String toString(Object t) { return "  " + t.getClass().toString(); }
				},"\n", decoders.values()));
				log.debug("JSON encoder classes found:\n{}", Utils.join(new Stringifier() {
					public String toString(Object t) { return "  " + t.getClass().toString(); }
				},"\n", encoders.values()));
			}
		}
	}
	
	public static <T> JSONEncoder<T> getEncoder(Class<? extends T> c) {
		JSONEncoder<?> enc = encoders.get(c);
		if (enc == null) {
			throw ServiceError.internal("No JSON encoder for " + c + " found.");
		}
		return (JSONEncoder<T>) enc;
	}

	public static <T> JSONDecoder<T> getDecoder(Class<? extends T> c) {
		JSONDecoder<?> dec = decoders.get(c);
		if (dec == null) {
			throw ServiceError.internal("No JSON decoder for " + c + " found.");
		}
		return (JSONDecoder<T>) dec;
	}

}