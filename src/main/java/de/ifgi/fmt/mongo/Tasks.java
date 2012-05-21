package de.ifgi.fmt.mongo;

import static de.ifgi.fmt.mongo.DaoFactory.getTaskDao;

import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.query.Query;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.task.Task;

public class Tasks implements ExtendedDao<Task>{
	@SuppressWarnings("unused")
	private final Store store;

	Tasks(Store store) {
		this.store = store;
	}

	public Task get(ObjectId id) {
		Store.log.debug("Getting Task {}", id);
		Task t = getTaskDao().get(id);
		if (t == null) {
			throw ServiceError.taskNotFound();
		}
		return t;
	}

	public Task save(Task t) {
		Store.log.debug("Saving Task {}", t);
		getTaskDao().save(t);
		return t;
	}

	public void save(Iterable<Task> tasks) {
		Store.log.debug("Saving Tasks");
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
		Store.log.debug("Deleting Tasks");
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