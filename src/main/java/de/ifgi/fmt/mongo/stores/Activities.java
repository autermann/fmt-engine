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

import static de.ifgi.fmt.mongo.DaoFactory.getActivityDao;
import static de.ifgi.fmt.mongo.DaoFactory.getSignalDao;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.query.Query;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.mongo.ExtendedDao;
import de.ifgi.fmt.mongo.Store;
import de.ifgi.fmt.mongo.Store.Queries;
import de.ifgi.fmt.utils.Utils;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
public class Activities implements ExtendedDao<Activity> {
	private static final Logger log = LoggerFactory.getLogger(Activities.class);
	private final Store store;

	/**
	 * 
	 * @param store
	 */
	public Activities(Store store) {
		this.store = store;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Activity get(ObjectId id) {
		log.debug("Getting Activity {}", id);
		Activity a = getActivityDao().get(id);
		if (a == null) {
			throw ServiceError.activityNotFound();
		}
		return a;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public Activity save(Activity a) {
		log.debug("Saving Acitivity {}", a);
		if (a.getSignal() != null) {
			getSignalDao().save(a.getSignal());
		}
		this.store.tasks().save(a.getTasks().values());
		getActivityDao().save(a);
		return a;
	}

	/**
	 * 
	 * @param activities
	 */
	@Override
	public void save(Collection<Activity> activities) {
		log.debug("Saving Activities");
		for (Activity a : activities) {
			save(a);
		}
	}

	/**
	 * 
	 * @param f
	 */
	public void delete(Flashmob f) {
		log.debug("Deleting Actitvties of Flashmob {}", f);
		delete(Queries.activitiesOfFlashmob(f));
	}

	/**
	 * 
	 * @param t
	 * @return
	 */
	public List<Activity> get(Trigger t) {
		log.debug("Getting Actitvties of Trigger {}", t);
		return get(Queries.activitiesOfTrigger(t));
	}

	/**
	 * 
	 * @param flashmob
	 * @param user
	 * @return
	 */
	public List<Activity> get(Flashmob flashmob, User user) {
		log.debug("Getting Actitvties of User {} in Flashmob {}", user, flashmob);
		Role r = Queries.rolesOfUserInFlashmob(user, flashmob).get();
		if (r == null) {
			return Utils.list();
		}
		return Utils.asList(r.getActivities());
	}

	/**
	 * 
	 * @param activity
	 * @return
	 */
	public Signal getSignalOfActivity(Activity activity) {
		log.debug("Getting Signal of Activity {}", activity);
		return activity.getSignal(); //TODO get the signal from the DB
	}

	/**
	 * 
	 * @param q
	 * @return
	 */
	public List<Activity> get(Query<Activity> q) {
		return getActivityDao().find(Store.g(q)).asList();
	}

	/**
	 * 
	 * @param q
	 * @return
	 */
	public Activity getOne(Query<Activity> q) {
		return getActivityDao().find(Store.g(q)).get();
	}

	/**
	 * 
	 * @param t
	 */
	@Override
	public void delete(Activity t) {
		log.debug("Deleting Activity {}", t);
		store.tasks().delete(t.getTasks().values());
		getActivityDao().delete(t);
		
		// TODO activity deletion
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * 
	 * @param ts
	 */
	@Override
	public void delete(Collection<Activity> ts) {
		log.debug("Deleting Activities");
		for (Activity a : ts) {
			delete(a);
		}
	}

	/**
	 * 
	 * @param limit
	 * @return
	 */
	@Override
	public List<Activity> get(int limit) {
		return get(all().limit(limit));
	}

	/**
	 * 
	 * @param q
	 */
	@Override
	public void delete(Query<Activity> q) {
		getActivityDao().deleteByQuery(Store.r(q));
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Query<Activity> all() {
		return getActivityDao().createQuery();
	}
}