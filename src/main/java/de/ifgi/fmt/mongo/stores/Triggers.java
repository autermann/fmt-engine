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

import static de.ifgi.fmt.mongo.DaoFactory.getTriggerDao;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.query.Query;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Trigger;
import de.ifgi.fmt.mongo.ExtendedDao;
import de.ifgi.fmt.mongo.Store;
import de.ifgi.fmt.mongo.Store.Queries;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
public class Triggers implements ExtendedDao<Trigger> {
	private static final Logger log = LoggerFactory.getLogger(Triggers.class);
	private final Store store;

	/**
	 * 
	 * @param store
	 */
	public Triggers(Store store) {
		this.store = store;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Trigger get(ObjectId id) {
		log.debug("Getting Trigger {}", id);
		Trigger t = getTriggerDao().get(id);
		if (t == null) {
			throw ServiceError.triggerNotFound();
		}
		return t;
	}

	/**
	 * 
	 * @param t
	 * @return
	 */
	@Override
	public Trigger save(Trigger t) {
		log.debug("Saving Trigger {}", t);
		getTriggerDao().save(t);
		return t;
	}

	/**
	 * 
	 * @param triggers
	 */
	@Override
	public void save(Collection<Trigger> triggers) {
		log.debug("Saving {} Triggers", triggers.size());
		for (Trigger t : triggers) {
			save(t);
		}
	}

	/**
	 * 
	 * @param t
	 */
	@Override
	public void delete(Trigger t) {
		log.debug("Deleting Trigger {}", t);
		for (Activity a : this.store.activities().get(t)) {
			this.store.activities().save(a.setTrigger(null));
		}
		getTriggerDao().delete(t);
	}

	/**
	 * 
	 * @param triggers
	 */
	@Override
	public void delete(Collection<Trigger> triggers) {
		log.debug("Deleting {} Triggers", triggers.size());
		for (Trigger t : triggers) {
			delete(t);
		}
	}

	/**
	 * 
	 * @param f
	 * @return
	 */
	public List<Trigger> get(Flashmob f) {
		log.debug("Getting Triggers of Flashmob {}", f);
		return get(Queries.triggersOfFlashmob(f));
	}

	/**
	 * 
	 * @param f
	 */
	public void delete(Flashmob f) {
		log.debug("Getting Triggers of Flashmob {}", f);
		delete(Queries.triggersOfFlashmob(f));
	}

	/**
	 * 
	 * @param q
	 * @return
	 */
	@Override
	public List<Trigger> get(Query<Trigger> q) {
		return getTriggerDao().find(Store.g(q)).asList();
	}

	/**
	 * 
	 * @param q
	 */
	@Override
	public void delete(Query<Trigger> q) {
		getTriggerDao().deleteByQuery(Store.r(q));
	}

	/**
	 * 
	 * @param q
	 * @return
	 */
	@Override
	public Trigger getOne(Query<Trigger> q) {
		return getTriggerDao().find(Store.g(q)).get();
	}

	/**
	 * 
	 * @param limit
	 * @return
	 */
	@Override
	public List<Trigger> get(int limit) {
		return get(all().limit(limit));
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Query<Trigger> all() {
		return getTriggerDao().createQuery();
	}
}