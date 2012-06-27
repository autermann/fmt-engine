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

import static de.ifgi.fmt.mongo.DaoFactory.getRoleDao;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.query.Query;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.mongo.ExtendedDao;
import de.ifgi.fmt.mongo.Store;
import de.ifgi.fmt.mongo.Store.Queries;

public class Roles implements ExtendedDao<Role> {
	private static final Logger log = LoggerFactory.getLogger(Roles.class);
	@SuppressWarnings("unused")
	private final Store store;

	public Roles(Store store) {
		this.store = store;
	}

	public Role get(ObjectId id) {
		log.debug("Getting Role {}", id);
		Role r = getRoleDao().get(id);
		if (r == null) {
			throw ServiceError.roleNotFound();
		}
		return r;
	}

	public void delete(Flashmob f) {
		delete(f.getRoles());
	}

	public Role save(Role role) {
		log.debug("Saving Role {}", role);
		getRoleDao().save(role);
		return role;
	}

	@Override
	public void save(Collection<Role> roles) {
		log.debug("Saving {} Roles", roles.size());
		for (Role r : roles) {
			save(r);
		}
	}

	public List<Role> get(User u) {
		log.debug("Getting Roles of User {}", u);
		return get(Queries.rolesOfUser(u));
	}

	public List<Role> get(Query<Role> q) {
		return getRoleDao().find(Store.g(q)).asList();
	}

	public Role getOne(Query<Role> q) {
		return getRoleDao().find(Store.g(q)).get();
	}

	@Override
	public void delete(Role t) {
		log.debug("Deleting Role {}", t);
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void delete(Collection<Role> ts) {
		log.debug("Deleting {} Roles", ts.size());
		for (Role r : ts) {
			delete(r);
		}
	}

	@Override
	public List<Role> get(int limit) {
		return get(all().limit(limit));
	}

	@Override
	public void delete(Query<Role> q) {
		getRoleDao().deleteByQuery(Store.r(q));
	}

	@Override
	public Query<Role> all() {
		return getRoleDao().createQuery();
	}

	public Role get(Flashmob f, User u) {
		log.debug("Getting Role of User {} in Flashmob", f);
		return getOne(Queries.rolesOfUserInFlashmob(u, f));
	}
}