package de.ifgi.fmt.mongo.stores;

import static de.ifgi.fmt.mongo.DaoFactory.getActivityDao;
import static de.ifgi.fmt.mongo.DaoFactory.getSignalDao;

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
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.mongo.ExtendedDao;
import de.ifgi.fmt.mongo.Store;
import de.ifgi.fmt.mongo.Store.Queries;

public class Activities implements ExtendedDao<Activity> {
	private static final Logger log = LoggerFactory.getLogger(Activities.class);
	private final Store store;

	public Activities(Store store) {
		this.store = store;
	}

	public Activity get(ObjectId id) {
		log.debug("Getting Activity {}", id);
		Activity a = getActivityDao().get(id);
		if (a == null) {
			throw ServiceError.activityNotFound();
		}
		return a;
	}

	public Activity save(Activity a) {
		log.debug("Saving Acitivity {}", a);
		getSignalDao().save(a.getSignal());
		this.store.tasks().save(a.getTasks().values());
		getActivityDao().save(a);
		return a;
	}

	public void save(Iterable<Activity> activities) {
		log.debug("Saving Activities");
		for (Activity a : activities) {
			save(a);
		}
	}

	public void delete(Flashmob f) {
		delete(Queries.activitiesOfFlashmob(f));
	}

	public List<Activity> get(Trigger t) {
		return get(Queries.activitiesOfTrigger(t));
	}

	public List<Activity> get(Flashmob flashmob, User user) {
		return get(all()
				.field(Activity.FLASHMOB).equal(flashmob)
				.field(Activity.TASKS + "." + Task.ROLE + "." + Role.USERS)
				.hasThisElement(user));
	}

	public Signal getSignalOfActivity(Activity activity) {
		return activity.getSignal(); //TODO get the signal from the DB
	}

	public List<Activity> get(Query<Activity> q) {
		return getActivityDao().find(Store.g(q)).asList();
	}

	public Activity getOne(Query<Activity> q) {
		return getActivityDao().find(Store.g(q)).get();
	}

	@Override
	public void delete(Activity t) {
		log.debug("Deleting Activity {}", t);
		// TODO activity deletion
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void delete(Iterable<Activity> ts) {
		log.debug("Deleting Activities");
		for (Activity a : ts) {
			delete(a);
		}

	}

	@Override
	public List<Activity> get(int limit) {
		return get(all().limit(limit));
	}

	@Override
	public void delete(Query<Activity> q) {
		getActivityDao().deleteByQuery(Store.r(q));
	}

	@Override
	public Query<Activity> all() {
		return getActivityDao().createQuery();
	}
}