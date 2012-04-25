package de.ifgi.fmt.utils;

public interface Filter<T> {
	public boolean test(T t);
}