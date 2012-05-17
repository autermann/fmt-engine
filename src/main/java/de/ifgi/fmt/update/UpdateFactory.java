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
	
	public static <T> T update(T original, T changes) {
		return getUpdater((Class<T>) changes.getClass()).update(original, changes);
	}

}
