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
package de.ifgi.fmt.mongo.stores;

import static de.ifgi.fmt.mongo.DaoFactory.getTaskDao;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.query.Query;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.mongo.ExtendedDao;
import de.ifgi.fmt.mongo.Store;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
public class Tasks implements ExtendedDao<Task> {
	private static final Logger log = LoggerFactory.getLogger(Tasks.class);

	private final Store store;

	/**
	 * 
	 * @param store
	 */
	public Tasks(Store store) {
		this.store = store;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Task get(ObjectId id) {
		log.debug("Getting Task {}", id);
		Task t = getTaskDao().get(id);
		if (t == null) {
			throw ServiceError.taskNotFound();
		}
		return t;
	}

	/**
	 * 
	 * @param t
	 * @return
	 */
	public Task save(Task t) {
		try {
			log.debug("Saving Task: {}", JSONFactory.getEncoder(Task.class)
					.encode(t, null));
		} catch (JSONException e) {
			log.error("Error: ", e);
		}
		getTaskDao().save(t);
		return t;
	}

	/**
	 * 
	 * @param tasks
	 */
	public void save(Collection<Task> tasks) {
		log.debug("Saving {} Tasks", tasks.size());
		for (Task t : tasks) {
			if (t != null) {
				save(t);
			}
		}
	}

	/**
	 * 
	 * @param t
	 */
	@Override
	public void delete(Task t) {
		log.debug("Deleting Task {}", t);
		store.activities().save(t.getActivity().removeTask(t));
		getTaskDao().delete(t);
	}

	/**
	 * 
	 * @param ts
	 */
	@Override
	public void delete(Collection<Task> ts) {
		log.debug("Deleting {} Tasks", ts.size());
		for (Task t : ts) {
			delete(t);
		}
	}

	/**
	 * 
	 * @param limit
	 * @return
	 */
	@Override
	public List<Task> get(int limit) {
		log.debug("Getting {} taks.", limit);
		return get(all().limit(limit));
	}

	/**
	 * 
	 * @param q
	 * @return
	 */
	@Override
	public Task getOne(Query<Task> q) {
		return getTaskDao().find(Store.g(q)).get();
	}

	/**
	 * 
	 * @param q
	 * @return
	 */
	@Override
	public List<Task> get(Query<Task> q) {
		return getTaskDao().find(Store.g(q)).asList();
	}

	/**
	 * 
	 * @param q
	 */
	@Override
	public void delete(Query<Task> q) {
		getTaskDao().deleteByQuery(Store.r(q));
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Query<Task> all() {
		return getTaskDao().createQuery();
	}

}