package de.ifgi.fmt.update;

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
public class UpdateFactory {

	@Documented
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Updates {
		public Class<?> value();
	}

	protected static final Logger log = LoggerFactory.getLogger(UpdateFactory.class);
	
	private static Map<Class<?>, EntityUpdater<?>> updaters = Utils.map();

	static {
		for (Class<?> c : Implementations.getAnnotatedSubclasses(EntityUpdater.class, Updates.class)) {
			try {
				EntityUpdater<?> enc = (EntityUpdater<?>) c.newInstance();
				updaters.put(c.getAnnotation(Updates.class).value(), enc);
			} catch (Exception e) {
				log.error("Can not instantiate encoder!", e);
			}
		}
		if (log.isDebugEnabled()) {
			if (log.isDebugEnabled()) {
				log.debug("EntityUpdater classes found:\n{}", Utils.join(new Stringifier() {
					public String toString(Object t) { return "  " + t.getClass().toString(); }
				},"\n", updaters.values()));
			}
		}
	}
	
	public static <T> EntityUpdater<T> getUpdater(Class<? extends T> c) {
		EntityUpdater<?> enc = updaters.get(c);
		if (enc == null) {
			throw ServiceError.internal("No JSON encoder for " + c + " found.");
		}
		return (EntityUpdater<T>) enc;
	}

}
