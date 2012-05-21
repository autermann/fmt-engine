package de.ifgi.fmt.mongo.stores;

import static de.ifgi.fmt.mongo.DaoFactory.getTriggerDao;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.query.Query;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.mongo.ExtendedDao;
import de.ifgi.fmt.mongo.Store;
import de.ifgi.fmt.mongo.Store.Queries;

public class Triggers implements ExtendedDao<Trigger> {
	private static final Logger log = LoggerFactory.getLogger(Triggers.class);
	private final Store store;

	public Triggers(Store store) {
		this.store = store;
	}

	public Trigger get(ObjectId id) {
		log.debug("Getting Trigger {}", id);
		Trigger t = getTriggerDao().get(id);
		if (t == null) {
			throw ServiceError.triggerNotFound();
		}
		return t;
	}

	public Trigger save(Trigger t) {
		log.debug("Saving Trigger {}", t);
		getTriggerDao().save(t);
		return t;
	}

	public void save(Iterable<Trigger> triggers) {
		log.debug("Saving Triggers");
		for (Trigger t : triggers) {
			save(t);
		}
	}

	public void delete(Trigger t) {
		log.debug("Deleting Trigger {}", t);
		for (Activity a : this.store.activities().get(t)) {
			this.store.activities().save(a.setTrigger(null));
		}
		getTriggerDao().delete(t);
	}

	public void delete(List<Trigger> triggers) {
		log.debug("Deleting Triggers");
		for (Trigger t : triggers) {
			delete(t);
		}
	}

	public List<Trigger> get(Flashmob f) {
		log.debug("Getting Triggers of Flashmob {}", f);
		return get(Queries.triggersOfFlashmob(f));
	}

	public void delete(Flashmob f) {
		log.debug("Getting Triggers of Flashmob {}", f);
		delete(Queries.triggersOfFlashmob(f));
	}

	@Override
	public List<Trigger> get(Query<Trigger> q) {
		return getTriggerDao().find(Store.g(q)).asList();
	}

	@Override
	public void delete(Query<Trigger> q) {
		getTriggerDao().deleteByQuery(Store.r(q));
	}

	@Override
	public Trigger getOne(Query<Trigger> q) {
		return getTriggerDao().find(Store.g(q)).get();
	}

	@Override
	public void delete(Iterable<Trigger> ts) {
		log.debug("Deleting Triggers");
		for (Trigger t : ts) {
			delete(t);
		}
	}

	@Override
	public List<Trigger> get(int limit) {
		return get(all().limit(limit));
	}

	@Override
	public Query<Trigger> all() {
		return getTriggerDao().createQuery();
	}
}