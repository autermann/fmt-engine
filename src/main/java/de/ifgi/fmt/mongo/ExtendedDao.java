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

public interface ExtendedDao<T> {
	void delete(T t);

	void delete(Collection<T> ts);

	T save(T t);

	void save(Collection<T> ts);

	List<T> get(int limit);

	T getOne(Query<T> q);

	T get(ObjectId id);

	List<T> get(Query<T> q);

	void delete(Query<T> q);

	Query<T> all();

}