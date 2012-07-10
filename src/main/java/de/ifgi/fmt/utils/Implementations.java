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
package de.ifgi.fmt.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
public class Implementations {
	/**
     * 
     */
	protected static class InstantiableFilter implements Filter<Class<?>> {
		/**
		 * 
		 * @param t
		 * @return
		 */
		public boolean test(Class<?> t) {
			int mod = t.getModifiers();
			return !(Modifier.isInterface(mod) && Modifier.isAbstract(mod) && Modifier
					.isPublic(mod));
		}
	};

	/**
	 * 
	 */
	protected static class AnnotationFilter implements Filter<Class<?>> {
		private Class<? extends Annotation>[] a;

		/**
		 * 
		 * @param a
		 */
		public AnnotationFilter(Class<? extends Annotation>... a) {
			this.a = a;
		}

		/**
		 * 
		 * @param t
		 * @return
		 */
		public boolean test(Class<?> t) {
			for (Class<? extends Annotation> c : a) {
				if (t.getAnnotation(c) == null)
					return false;
			}
			return true;
		}
	}

	/**
	 * 
	 */
	protected static final Logger log = LoggerFactory
			.getLogger(Implementations.class);
	private static final String PROPERTIES_FILE = "/provider.properties";

	private static Map<String, String> implementations = Utils.map();
	private static final Reflections r = new Reflections("de.ifgi.fmt",
			new SubTypesScanner());

	static {
		InputStream is = Implementations.class
				.getResourceAsStream(PROPERTIES_FILE);
		if (is != null) {
			try {
				Properties p = new Properties();
				p.load(is);
				for (Entry<Object, Object> e : p.entrySet()) {
					implementations.put((String) e.getKey(),
							(String) e.getValue());
				}
			} catch (IOException e) {
				throw new RuntimeException("can not load properties file: "
						+ PROPERTIES_FILE, e);
			}
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<? extends T> getSubclass(Class<T> t) {

		Set<Class<? extends T>> classes = getSubclasses(t);
		if (classes.isEmpty()) {
			String impl = implementations.get(t.getCanonicalName());
			if (impl == null) {
				throw new NullPointerException(
						"can not find implementation for "
								+ t.getCanonicalName());
			}
			try {
				return (Class<T>) Class.forName(impl);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}

		Class<? extends T> c = classes.iterator().next();
		if (classes.size() > 1 && log.isWarnEnabled()) {
			StringBuffer buf = new StringBuffer();
			buf.append("There is more than one implementation of ");
			buf.append(t.toString()).append("\n");
			for (Class<? extends T> cl : classes) {
				buf.append("\t").append(cl.toString()).append("\n");
			}
			buf.append("Continuing with ").append(c.getName());
			log.warn(buf.toString());
		}
		return c;

	}

	/**
	 * 
	 * @param <T>
	 * @param t
	 * @return
	 */
	public static <T> Set<Class<? extends T>> getSubclasses(Class<T> t) {
		return Utils.filter(r.getSubTypesOf(t), new InstantiableFilter());
	}

	/**
	 * 
	 * @param <T>
	 * @param clazz
	 * @param a
	 * @return
	 */
	public static <T> Set<Class<? extends T>> getAnnotatedSubclasses(
			Class<T> clazz, final Class<? extends Annotation>... a) {
		return Utils.filter(getSubclasses(clazz), new AnnotationFilter(a));
	}
}
