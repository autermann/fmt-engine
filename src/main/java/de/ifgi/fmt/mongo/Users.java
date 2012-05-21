package de.ifgi.fmt.mongo;

import static de.ifgi.fmt.mongo.DaoFactory.getUserDao;

import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.query.Query;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.mongo.Store.Queries;

public class Users implements ExtendedDao<User>{

	/**
	 * 
	 */
	private final Store store;

	/**
	 * @param store
	 */
	Users(Store store) {
		this.store = store;
	}

	public User get(ObjectId id) {
		Store.log.debug("Getting User {}", id);
		User u = getUserDao().get(id);
		if (u == null) {
			throw ServiceError.userNotFound();
		}
		return u;
	}

	public List<User> get(int limit) {
		return getUserDao().find(getUserDao().createQuery().limit(limit))
				.asList();
	}

	public User save(User u) {
		Store.log.debug("Saving User {}", u);
		getUserDao().save(u);
		return u;
	}

	public void save(Iterable<User> u) {
		Store.log.debug("Saving Users");
		getUserDao().saveAll(u);
	}

	public void delete(User u) {
		Store.log.debug("Deleting User {}", u);
		this.store.flashmobs().delete(Queries.flashmobsByUser(u));
		deleteFromRoles(u, this.store.roles().get(u));
		deleteFromComments(u, this.store.comments().get(u));
		DaoFactory.getUserDao().delete(u);
	}

	public void delete(Iterable<User> users) {
		Store.log.debug("Deleting Users");
		for (User u : users) {
			delete(u);
		}
	}

	public void deleteFromRole(User u, Role r) {
		Store.log.debug("Deleting User {} from Role {}", u, r);
		this.store.roles().save(r.removeUser(u));
	}

	public void deleteFromRoles(User u, List<Role> roles) {
		Store.log.debug("Deleting User {} from Roles", u);
		for (Role r : roles) {
			deleteFromRole(u, r);
		}
	}

	public void deleteFromComment(User u, Comment c) {
		Store.log.debug("Deleting User {} from Comment {}", u, c);
		this.store.comments().save(c.setUser(null));
	}

	public void deleteFromComments(User u, List<Comment> comments) {
		Store.log.debug("Deleting User {} from Comments", u);
		for (Comment c : comments) {
			deleteFromComment(u, c);
		}
	}

	public List<User> get(Query<User> q) {
		return getUserDao().find(Store.g(q)).asList();
	}

	public User getOne(Query<User> q) {
		return getUserDao().find(Store.g(q)).get();
	}

	@Override
	public void delete(Query<User> q) {
		getUserDao().deleteByQuery(Store.r(q));
	}

	@Override
	public Query<User> all() {
		return getUserDao().createQuery();
	}

}