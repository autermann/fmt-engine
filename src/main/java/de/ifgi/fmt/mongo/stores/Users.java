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

import static de.ifgi.fmt.mongo.DaoFactory.getUserDao;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.mapping.Mapper;
import com.google.code.morphia.query.Query;
import com.mongodb.MongoException;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.mongo.DaoFactory;
import de.ifgi.fmt.mongo.ExtendedDao;
import de.ifgi.fmt.mongo.Store;
import de.ifgi.fmt.mongo.Store.Queries;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
public class Users implements ExtendedDao<User>{
	private static final Logger log = LoggerFactory.getLogger(Users.class);
	private final Store store;

	/**
	 * 
	 * @param store
	 */
	public Users(Store store) {
		this.store = store;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public User get(ObjectId id) {
		log.debug("Getting User {}", id);
		User u = getUserDao().get(id);
		if (u == null) {
			throw ServiceError.userNotFound();
		}
		return u;
	}

	/**
	 * 
	 * @param limit
	 * @param search 
	 * @return
	 */
	public List<User> get(int limit) {
		return getUserDao().find(getUserDao().createQuery().limit(limit)).asList();
	}

	/**
	 * 
	 * @param limit
	 * @param search 
	 * @return
	 */
	public List<User> search(String search) {
		return getUserDao().find(
				getUserDao().createQuery().field(User.USERNAME)
						.containsIgnoreCase(search)).asList();
	}

	/**
	 * 
	 * @param u
	 * @return
	 */
	public User save(User u) {
		try {
			log.debug("Saving User {}", JSONFactory.getEncoder(User.class).encode(u, null).toString(4));
		} catch (JSONException e) {
			throw ServiceError.internal(e);
		}
		try {
			getUserDao().save(u);
		} catch (MongoException.DuplicateKey e) {
			throw ServiceError.badRequest("Duplicate username or email address");
		}
		return u;
	}
	/**
	 * 
	 * @param u
	 */
	@Override
	public void save(Collection<User> u) {
		log.debug("Saving {} Users", u.size());
		getUserDao().saveAll(u);
	}
	/**
	 * 
	 * @param u
	 */
	@Override
	public void delete(User u) {
		log.debug("Deleting User {}", u);
		this.store.flashmobs().delete(Queries.flashmobsByUser(u));
		deleteFromRoles(u, this.store.roles().get(u));
		deleteFromComments(u, this.store.comments().get(u));
		DaoFactory.getUserDao().delete(u);
	}
	/**
	 * 
	 * @param users
	 */
	@Override
	public void delete(Collection<User> users) {
		log.debug("Deleting {} Users", users.size());
		for (User u : users) {
			delete(u);
		}
	}

	/**
	 * 
	 * @param u
	 * @param r
	 */
	public void deleteFromRole(User u, Role r) {
		log.debug("Deleting User {} from Role {}", u, r);
		this.store.roles().save(r.removeUser(u));
	}

	/**
	 * 
	 * @param u
	 * @param roles
	 */
	public void deleteFromRoles(User u, List<Role> roles) {
		log.debug("Deleting User {} from Roles", u);
		for (Role r : roles) {
			deleteFromRole(u, r);
		}
	}

	/**
	 * 
	 * @param u
	 * @param c
	 */
	public void deleteFromComment(User u, Comment c) {
		log.debug("Deleting User {} from Comment {}", u, c);
		this.store.comments().save(c.setUser(null));
	}

	/**
	 * 
	 * @param u
	 * @param comments
	 */
	public void deleteFromComments(User u, List<Comment> comments) {
		log.debug("Deleting User {} from Comments", u);
		for (Comment c : comments) {
			deleteFromComment(u, c);
		}
	}

	/**
	 * 
	 * @param q
	 * @return
	 */
	@Override
	public List<User> get(Query<User> q) {
		return getUserDao().find(Store.g(q)).asList();
	}
	/**
	 * 
	 * @param q
	 * @return
	 */
	@Override
	public User getOne(Query<User> q) {
		return getUserDao().find(Store.g(q)).get();
	}

	/**
	 * 
	 * @param q
	 */
	@Override
	public void delete(Query<User> q) {
		getUserDao().deleteByQuery(Store.r(q));
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Query<User> all() {
		return getUserDao().createQuery();
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	public User getByAuthToken(String token) {
		return getOne(Queries.userByAuthToken(token));
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	public User get(String username) {
		return getOne(Queries.userByName(username));
	}

	/**
	 * 
	 * @param u
	 * @param token
	 */
	public void setAuthToken(User u, String token) {
		getUserDao().update(
				getUserDao().createQuery().field(Mapper.ID_KEY).equal(u.getUsername()),
				getUserDao().createUpdateOperations().set(User.AUTH_TOKEN, token));
	}
}