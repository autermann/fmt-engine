package de.ifgi.fmt.mongo.stores;

import static de.ifgi.fmt.mongo.DaoFactory.getTaskDao;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.query.Query;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.mongo.ExtendedDao;
import de.ifgi.fmt.mongo.Store;

public class Tasks implements ExtendedDao<Task>{
	private static final Logger log = LoggerFactory.getLogger(Tasks.class);
	@SuppressWarnings("unused")
	private final Store store;

	public Tasks(Store store) {
		this.store = store;
	}

	public Task get(ObjectId id) {
		log.debug("Getting Task {}", id);
		Task t = getTaskDao().get(id);
		if (t == null) {
			throw ServiceError.taskNotFound();
		}
		return t;
	}

	public Task save(Task t) {
		log.debug("Saving Task {}", t);
		getTaskDao().save(t);
		return t;
	}

	public void save(Iterable<Task> tasks) {
		log.debug("Saving Tasks");
		for (Task t : tasks) {
			save(t);
		}
	}

	@Override
	public void delete(Task t) {
		// TODO task deletion
		throw new UnsupportedOperationException("Not yet implemented");
		
	}

	@Override
	public void delete(Iterable<Task> ts) {
		log.debug("Deleting Tasks");
		for (Task t : ts) {
			delete(t);
		}
	}

	@Override
	public List<Task> get(int limit) {
		return get(all().limit(limit));
	}

	@Override
	public Task getOne(Query<Task> q) {
		return getTaskDao().find(Store.g(q)).get();
	}

	@Override
	public List<Task> get(Query<Task> q) {
		return getTaskDao().find(Store.g(q)).asList();
	}

	@Override
	public void delete(Query<Task> q) {
		getTaskDao().deleteByQuery(Store.r(q));
	}

	@Override
	public Query<Task> all() {
		return getTaskDao().createQuery();
	}

}