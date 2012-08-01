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

import static de.ifgi.fmt.mongo.DaoFactory.getFlashmobDao;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.query.Query;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.BoundingBox;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.mongo.ExtendedDao;
import de.ifgi.fmt.mongo.Store;
import de.ifgi.fmt.mongo.Store.Queries;
import de.ifgi.fmt.utils.Filter;
import de.ifgi.fmt.utils.Utils;
import de.ifgi.fmt.utils.constants.RESTConstants.ShowStatus;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
public class Flashmobs implements ExtendedDao<Flashmob> {
	private static final Logger log = LoggerFactory.getLogger(Flashmobs.class);
	private final Store store;

	/**
	 * 
	 * @param store
	 */
	public Flashmobs(Store store) {
		this.store = store;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Flashmob get(ObjectId id) {
		log.debug("Getting Flashmob {}", id);
		Flashmob f = getFlashmobDao().get(id);
		if (f == null) { throw ServiceError.flashmobNotFound(); }
		return f;
	}

	/**
	 * 
	 * @param f
	 * @return
	 */
	@Override
	public Flashmob save(Flashmob f) {
		log.debug("Saving Flashmob {}", f);
		this.store.triggers().save(f.getTriggers());
		this.store.roles().save(f.getRoles());
		this.store.activities().save(f.getActivities());
		getFlashmobDao().save(f);
		return f;
	}

	/**
	 * 
	 * @param flashmobs
	 */
	@Override
	public void save(Collection<Flashmob> flashmobs) {
		log.debug("Saving {} Flashmobs", flashmobs.size());
		for (Flashmob f : flashmobs) {
			save(f);
		}
	}

	/**
	 * 
	 * @param flashmobs
	 */
	@Override
	public void delete(Collection<Flashmob> flashmobs) {
		log.debug("Deleting {} Flashmobs", flashmobs.size());
		for (Flashmob f : flashmobs) {
			delete(f);
		}
	}

	/**
	 * 
	 * @param f
	 */
	@Override
	public void delete(Flashmob f) {
		this.store.triggers().delete(f);
		this.store.comments().delete(f);
		this.store.roles().delete(f);
		this.store.activities().delete(f);
		getFlashmobDao().delete(f);
	}

	/**
	 * 
	 * @param u
	 * @return
	 */
	public List<Flashmob> get(User u) {
		log.debug("Getting Flashmobs of User {}", u);
		return get(Queries.flashmobsOfUser(u));
	}

	/**
	 * 
	 * @param limit
	 * @param near
	 * @param user
	 * @param bbox
	 * @param from
	 * @param to
	 * @param sorting
	 * @param descending
	 * @param show
	 * @param search
	 * @param participant
	 * @param minParticipants
	 * @param maxParticipants
	 * @return
	 */
	public List<Flashmob> get(int limit, Point near, User user,
			BoundingBox bbox, DateTime from, DateTime to, ShowStatus show,
			String search, User participant, final int minParticipants,
			final int maxParticipants) {

		Query<Flashmob> q = getFlashmobDao().createQuery();
		if (bbox != null) {
			Queries.in(q, bbox);
		}
		if (near != null) {
			Queries.near(q, near);
		}
		if (user != null) {
			Queries.coordinatedBy(q, user);
		}
		if (from != null) {
			Queries.after(q, from);
		}
		if (to != null) {
			Queries.before(q, to);
		}

		if (show != null) {
			Queries.isPublic(q, show);
		}

		if (search != null) {
			Queries.search(q, search);
		}

		if (participant != null) {
			Queries.hasUser(q, participant);
		}

		q.limit(limit);

		return (minParticipants <= 0 && maxParticipants <= 0) ? get(q) : Utils
				.filter(get(q), new Filter<Flashmob>() {
					public boolean test(Flashmob t) {
						int users = t.getRegisteredUsers();
						return (minParticipants <= 0 || users >= minParticipants)
								&& (maxParticipants <= 0 || users <= maxParticipants);
					}
				});

	}

	/**
	 * 
	 * @param q
	 * @return
	 */
	@Override
	public List<Flashmob> get(Query<Flashmob> q) {
		return getFlashmobDao().find(Store.g(q)).asList();
	}

	/**
	 * 
	 * @param q
	 * @return
	 */
	@Override
	public Flashmob getOne(Query<Flashmob> q) {
		return getFlashmobDao().find(Store.g(q)).get();
	}

	/**
	 * 
	 * @param limit
	 * @return
	 */
	@Override
	public List<Flashmob> get(int limit) {
		return get(all().limit(limit));
	}

	/**
	 * 
	 * @param q
	 */
	@Override
	public void delete(Query<Flashmob> q) {
		getFlashmobDao().deleteByQuery(Store.r(q));
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Query<Flashmob> all() {
		return getFlashmobDao().createQuery();
	}
}