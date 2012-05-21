package de.ifgi.fmt.mongo;

import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.query.Query;

public interface ExtendedDao<T> {
	void delete(T t);

	void delete(Iterable<T> ts);

	T save(T t);

	void save(Iterable<T> ts);

	List<T> get(int limit);

	T getOne(Query<T> q);

	T get(ObjectId id);

	List<T> get(Query<T> q);

	void delete(Query<T> q);

	Query<T> all();

}