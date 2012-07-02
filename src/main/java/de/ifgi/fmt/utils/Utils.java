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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@SuppressWarnings("serial")
public class Utils {
	private static final boolean FORMAT_JSON = true;
	
	private static Stringifier DEFAULT_STRINGIFIER = new Stringifier() {
		public String toString(Object t) {
			if (t == null)
				return "null";
			else
				return t.toString();
		}
	};

	/**
	 * 
	 * @param <T>
	 * @return
	 */
	public static <T> Set<T> set() {
		return new HashSet<T>();
	}

	/**
	 * 
	 * @param <T>
	 * @param t
	 * @return
	 */
	public static <T> Set<T> set(final T t) {
		return new HashSet<T>(1) {
			{
				add(t);
			}
		};
	}

	/**
	 * 
	 * @param <T>
	 * @param ts
	 * @return
	 */
	public static <T> Set<T> set(final T... ts) {
		return new HashSet<T>(ts.length) {
			{
				for (T t : ts)
					add(t);
			}
		};
	}

	/**
	 * 
	 * @param <T>
	 * @param <V>
	 * @return
	 */
	public static <T, V> Map<T, V> map() {
		return new HashMap<T, V>();
	}

	/**
	 * 
	 * @param <T>
	 * @param <V>
	 * @param t
	 * @param v
	 * @return
	 */
	public static <T, V> Map<T, V> map(final T t, final V v) {
		return new HashMap<T, V>() {
			{
				put(t, v);
			}
		};
	}

	/**
	 * 
	 * @param <T>
	 * @param <V>
	 * @param t
	 * @param v
	 * @return
	 */
	public static <T, V> Map<T, V> map(final T[] t, final V[] v) {
		return new HashMap<T, V>() {
			{
				int l = t.length > v.length ? v.length : t.length;
				for (int i = 0; i < l; ++i) {
					put(t[i], v[i]);
				}
			}
		};
	}

	/**
	 * 
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> list() {
		return new LinkedList<T>();
	}

	/**
	 * 
	 * @param <T>
	 * @param t
	 * @return
	 */
	public static <T> List<T> list(final T t) {
		return new ArrayList<T>(1) {
			{
				add(t);
			}
		};
	}

	/**
	 * 
	 * @param <T>
	 * @param ts
	 * @return
	 */
	public static <T> List<T> list(final T... ts) {
		return new ArrayList<T>(ts.length) {
			{
				for (T t : ts)
					add(t);
			}
		};
	}

	/**
	 * 
	 * @param <T>
	 * @param ts
	 * @return
	 */
	public static <T> Collection<T> collection(final T... ts) {
		return list(ts);
	}

	/**
	 * 
	 * @param j
	 * @return
	 */
	public static String toString(JSONObject j) {
		try {
			return FORMAT_JSON ? j.toString(4) : j.toString();
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @param <T>
	 * @param s
	 * @param filter
	 * @return
	 */
	public static <T> List<T> filter(List<? extends T> s,
			Filter<? super T> filter) {
		return filter(s, filter, Utils.<T> list());
	}
	
	/**
	 * 
	 * @param <T>
	 * @param s
	 * @param filter
	 * @return
	 */
	public static <T> Set<T> filter(Set<? extends T> s,
			Filter<? super T> filter) {
		return filter(s, filter, Utils.<T> set());
	}
	
	/**
	 * 
	 * @param <T>
	 * @param s
	 * @param filter
	 * @return
	 */
	public static <T> Collection<T> filter(Collection<? extends T> s,
			Filter<? super T> filter) {
		return filter(s, filter, Utils.<T> list());
	}

	/**
	 * 
	 * @param <T>
	 * @param <V>
	 * @param source
	 * @param filter
	 * @param target
	 * @return
	 */
	public static <T, V extends Collection<T>> V filter(
			Iterable<? extends T> source, Filter<? super T> filter, V target) {
		for (T t : source)
			if (filter.test(t))
				target.add(t);
		return target;
	}

	/**
	 * 
	 * @param sep
	 * @param col
	 * @return
	 */
	public static String join(String sep, Object... col) {
		return join(null, sep, col);
	}

	/**
	 * 
	 * @param s
	 * @param sep
	 * @param col
	 * @return
	 */
	public static String join(Stringifier s, String sep, Object... col) {
		if (col == null || col.length == 0)
			return "";
		if (col.length == 1)
			return String.valueOf(col[0]);
		StringBuilder sb = new StringBuilder(col[0].toString());
		if (s == null)
			s = DEFAULT_STRINGIFIER;
		for (int i = 1; i < col.length; ++i)
			sb.append(sep).append(s.toString(col[i]));
		return sb.toString();
	}

	/**
	 * 
	 * @param sep
	 * @param col
	 * @return
	 */
	public static String join(String sep, Iterable<? extends Object> col) {
		return join(null, sep, col);
	}

	/**
	 * 
	 * @param s
	 * @param sep
	 * @param col
	 * @return
	 */
	public static String join(Stringifier s, String sep,
			Iterable<? extends Object> col) {
		Iterator<? extends Object> i;
		if (col == null || (!(i = col.iterator()).hasNext()))
			return "";
		if (s == null)
			s = DEFAULT_STRINGIFIER;
		StringBuilder sb = new StringBuilder(s.toString(i.next()));
		while (i.hasNext())
			sb.append(sep).append(s.toString(i.next()));
		return sb.toString();
	}

	/**
	 * 
	 * @param t
	 * @param collClass
	 * @param itemClass
	 * @return
	 */
	public static boolean isParameterizedWith(Type t, Class<?> collClass,
			Class<?> itemClass) {
		if (t instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) t;
			if (collClass.isAssignableFrom((Class<?>) pt.getRawType())) {
				Type argT = pt.getActualTypeArguments()[0];
				Class<?> tV = null;
				if (argT instanceof ParameterizedType) {
					tV = (Class<?>) ((ParameterizedType) argT).getRawType();
				} else if (argT instanceof Class) {
					tV = (Class<?>) argT;
				} else {
					return false;
				}
				return itemClass.isAssignableFrom(tV);
			}
		}
		return false;
	}

	/**
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> List<T> castToSuperType(final List<? extends T> list) {
		return new ArrayList<T>(list.size()) {
			{
				addAll(list);
			}
		};
	}

	/**
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> Set<T> castToSuperType(final Set<? extends T> list) {
		return new HashSet<T>(list.size()) {
			{
				addAll(list);
			}
		};
	}

	/**
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> Collection<T> castToSuperType(
			final Collection<? extends T> list) {
		return new ArrayList<T>(list.size()) {
			{
				addAll(list);
			}
		};
	}

	/**
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			IOUtils.copy(is, os);
			return os.toByteArray();
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param set
	 * @return
	 */
	public static <T> List<T> asList(final Iterable<T> set) {
		return new LinkedList<T>() {{ for (T t : set) { add(t); } }};
	}
	
	/**
	 * 
	 * @param <T>
	 * @param list
	 * @param begin
	 * @param end
	 * @return
	 */
	public static <T> List<T> sublist(Iterable<T> list, int begin, int end) {
		int i = 0;
		List<T> n = list();
		for (T t : list) {
			if (end == i)
				break;
			if (i >= begin)
				n.add(t);
			++i;
		}
		return n;
	}

	/**
	 * 
	 * @param os
	 * @return
	 */
	public static boolean moreThanOneNotNull(Object... os) {
		boolean nulled = false;
		for (Object o : os) {
			if (o != null) {
				if (nulled) {
					return true;
				} else {
					nulled = true;
				}
			}
		}
		return false;
	}
}
