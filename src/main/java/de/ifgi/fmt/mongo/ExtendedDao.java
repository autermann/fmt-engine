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
package de.ifgi.fmt.mongo;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.query.Query;

/**
 * 
 * @author Autermann, Demuth, Radtke
 * @param <T>
 */
public interface ExtendedDao<T> {
	/**
	 * 
	 * @param t
	 */
	void delete(T t);

	/**
	 * 
	 * @param ts
	 */
	void delete(Collection<T> ts);

	/**
	 * 
	 * @param t
	 * @return
	 */
	T save(T t);

	/**
	 * 
	 * @param ts
	 */
	void save(Collection<T> ts);

	/**
	 * 
	 * @param limit
	 * @return
	 */
	List<T> get(int limit);

	/**
	 * 
	 * @param q
	 * @return
	 */
	T getOne(Query<T> q);

	/**
	 * 
	 * @param id
	 * @return
	 */
	T get(ObjectId id);

	/**
	 * 
	 * @param q
	 * @return
	 */
	List<T> get(Query<T> q);

	/**
	 * 
	 * @param q
	 */
	void delete(Query<T> q);

	/**
	 * 
	 * @return
	 */
	Query<T> all();

}