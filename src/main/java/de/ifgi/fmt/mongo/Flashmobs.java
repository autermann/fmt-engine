package de.ifgi.fmt.mongo;

import static de.ifgi.fmt.mongo.DaoFactory.getFlashmobDao;

import java.util.List;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import com.google.code.morphia.query.Query;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.BoundingBox;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.mongo.Store.Queries;
import de.ifgi.fmt.utils.constants.RESTConstants.ShowStatus;
import de.ifgi.fmt.utils.constants.RESTConstants.Sorting;

public class Flashmobs implements ExtendedDao<Flashmob> {

	/**
	 * 
	 */
	private final Store store;

	/**
	 * @param store
	 */
	Flashmobs(Store store) {
		this.store = store;
	}

	@Override
	public Flashmob get(ObjectId id) {
		Store.log.debug("Getting Flashmob {}", id);
		Flashmob f = getFlashmobDao().get(id);
		if (f == null) {
			throw ServiceError.flashmobNotFound();
		}
		return f;
	}

	@Override
	public Flashmob save(Flashmob f) {
		Store.log.debug("Saving Flashmob {}", f);
		this.store.triggers().save(f.getTriggers());
		this.store.roles().save(f.getRoles());
		this.store.activities().save(f.getActivities());
		getFlashmobDao().save(f);
		return f;
	}

	@Override
	public void save(Iterable<Flashmob> flashmobs) {
		Store.log.debug("Saving Flashmobs");
		for (Flashmob f : flashmobs) {
			save(f);
		}
	}

	@Override
	public void delete(Iterable<Flashmob> flashmobs) {
		for (Flashmob f : flashmobs) {
			delete(f);
		}
	}

	@Override
	public void delete(Flashmob f) {
		this.store.triggers().delete(f);
		this.store.comments().delete(f);
		this.store.roles().delete(f);
		this.store.activities().delete(f);
		getFlashmobDao().delete(f);
	}

	public List<Flashmob> get(User u) {
		Store.log.debug("Getting Flashmobs of User {}", u);
		return get(Queries.flashmobsOfUser(u));
	}

	public List<Flashmob> get(int limit, Point near, User user,
			BoundingBox bbox, DateTime from, DateTime to, Sorting sorting,
			boolean descending, ShowStatus show, String search,
			User participant) {

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

		if (sorting != null) {
			// TODO sorting
			switch (sorting) {
			case CREATION_TIME:
			case START_TIME:
			case TITLE:
			case PARTICIPANTS:
			case DISTANCE:
			}
		}

		if (participant != null) {
			Queries.hasUser(q, participant);
		}
		if (descending) {
			// TODO sorting/descending
		}
		q.limit(limit);
		return get(q);
	}

	@Override
	public List<Flashmob> get(Query<Flashmob> q) {
		return getFlashmobDao().find(Store.g(q)).asList();
	}

	@Override
	public Flashmob getOne(Query<Flashmob> q) {
		return getFlashmobDao().find(Store.g(q)).get();
	}

	@Override
	public List<Flashmob> get(int limit) {
		return get(all().limit(limit));
	}

	@Override
	public void delete(Query<Flashmob> q) {
		getFlashmobDao().deleteByQuery(Store.r(q));
	}

	@Override
	public Query<Flashmob> all() {
		return getFlashmobDao().createQuery();
	}
}