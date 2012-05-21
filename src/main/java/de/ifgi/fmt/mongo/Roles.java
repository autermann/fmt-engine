package de.ifgi.fmt.mongo;

import static de.ifgi.fmt.mongo.DaoFactory.getRoleDao;

import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.query.Query;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.mongo.Store.Queries;

public class Roles implements ExtendedDao<Role>{
	@SuppressWarnings("unused")
	private final Store store;

	Roles(Store store) {
		this.store = store;
	}

	public Role get(ObjectId id) {
		Store.log.debug("Getting Role {}", id);
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
		Store.log.debug("Saving Role {}", role);
		getRoleDao().save(role);
		return role;
	}

	public void save(Iterable<Role> roles) {
		Store.log.debug("Saving Roles");
		for (Role r : roles) {
			save(r);
		}
	}

	public List<Role> get(User u) {
		Store.log.debug("Getting Roles of User {}", u);
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
		Store.log.debug("Deleting Role {}", t);
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void delete(Iterable<Role> ts) {
		Store.log.debug("Deleting Roles");
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
		Store.log.debug("Getting Role of User {} in Flashmob", f);
		return getOne(Queries.rolesOfUserInFlashmob(u, f));
	}
}