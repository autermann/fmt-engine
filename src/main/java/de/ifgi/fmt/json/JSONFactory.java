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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.Viewable;
import de.ifgi.fmt.utils.Implementations;
import de.ifgi.fmt.utils.Stringifier;
import de.ifgi.fmt.utils.Utils;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@SuppressWarnings("unchecked")
public class JSONFactory {

    /**
     * 
     */
    @Documented
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Decodes {
	    /**
	     * 
	     * @return
	     */
	    public Class<?> value();
	}

	/**
	 * 
	 */
	@Documented
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Encodes {
	    /**
	     * 
	     * @return
	     */
	    public Class<?> value();
	}

	/**
	 * 
	 */
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
	
	/**
	 * 
	 * @param <T>
	 * @param c
	 * @return
	 */
	public static <T extends Viewable<T>> JSONEncoder<T> getEncoder(Class<? extends T> c) {
		JSONEncoder<?> enc = encoders.get(c);
		if (enc == null) {
			for (Entry<Class<?>, JSONEncoder<?>> e : encoders.entrySet()) {
				if (e.getKey().isAssignableFrom(c))  {
					return  (JSONEncoder<T>) e.getValue();
				}
			}
		}
		if (enc == null) {
			throw ServiceError.internal("No JSON encoder for " + c + " found.");
		}
		return (JSONEncoder<T>) enc;
	}

	/**
	 * 
	 * @param <T>
	 * @param c
	 * @return
	 */
	public static <T> JSONDecoder<T> getDecoder(Class<? extends T> c) {
		JSONDecoder<?> dec = decoders.get(c);
		if (dec == null) {
			for (Entry<Class<?>, JSONDecoder<?>> e : decoders.entrySet()) {
				if (e.getKey().isAssignableFrom(c))  {
					return  (JSONDecoder<T>) e.getValue();
				}
			}
		}
		if (dec == null) {
			throw ServiceError.internal("No JSON decoder for " + c + " found.");
		}
		return (JSONDecoder<T>) dec;
	}

}
