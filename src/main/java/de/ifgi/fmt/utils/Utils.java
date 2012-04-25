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

	public static <T> Set<T> set() {
		return new HashSet<T>();
	}

	public static <T> Set<T> set(final T t) {
		return new HashSet<T>(1) {
			{
				add(t);
			}
		};
	}

	public static <T> Set<T> set(final T... ts) {
		return new HashSet<T>(ts.length) {
			{
				for (T t : ts)
					add(t);
			}
		};
	}

	public static <T, V> Map<T, V> map() {
		return new HashMap<T, V>();
	}

	public static <T, V> Map<T, V> map(final T t, final V v) {
		return new HashMap<T, V>() {
			{
				put(t, v);
			}
		};
	}

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

	public static <T> List<T> list() {
		return new LinkedList<T>();
	}

	public static <T> List<T> list(final T t) {
		return new ArrayList<T>(1) {
			{
				add(t);
			}
		};
	}

	public static <T> List<T> list(final T... ts) {
		return new ArrayList<T>(ts.length) {
			{
				for (T t : ts)
					add(t);
			}
		};
	}

	public static <T> Collection<T> collection(final T... ts) {
		return list(ts);
	}

	public static String toString(JSONObject j) {
		try {
			return FORMAT_JSON ? j.toString(4) : j.toString();
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> Set<T> filter(Set<? extends T> s, Filter<? super T> filter) {
		return filter(s, filter, Utils.<T> set());
	}

	public static <T> List<T> filter(List<? extends T> s,
			Filter<? super T> filter) {
		return filter(s, filter, Utils.<T> list());
	}

	public static <T> Collection<T> filter(Collection<? extends T> s,
			Filter<? super T> filter) {
		return filter(s, filter, Utils.<T> list());
	}

	public static <T, V extends Collection<T>> V filter(
			Iterable<? extends T> source, Filter<? super T> filter, V target) {
		for (T t : source)
			if (filter.test(t))
				target.add(t);
		return target;
	}

	public static String join(String sep, Object... col) {
		return join(null, col);
	}

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

	public static String join(String sep, Iterable<? extends Object> col) {
		return join(null, sep, col);
	}

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

	public static <T> List<T> castToSuperType(final List<? extends T> list) {
		return new ArrayList<T>(list.size()) {
			{
				addAll(list);
			}
		};
	}

	public static <T> Set<T> castToSuperType(final Set<? extends T> list) {
		return new HashSet<T>(list.size()) {
			{
				addAll(list);
			}
		};
	}

	public static <T> Collection<T> castToSuperType(
			final Collection<? extends T> list) {
		return new ArrayList<T>(list.size()) {
			{
				addAll(list);
			}
		};
	}

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

}
