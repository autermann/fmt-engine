package de.ifgi.fmt.mongo.store;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.mongo.MongoDB.MongoDao;

public class DaoFactory {
	public static class ThreadLocalDao<T> extends ThreadLocal<MongoDao<T>> {
		private final Class<T> clazz;

		public ThreadLocalDao(Class<T> c) {
			this.clazz = c;
		}

		protected MongoDao<T> initialValue() {
			return MongoDao.get(clazz);
		}

		public static <T> ThreadLocalDao<T> get(Class<T> c) {
			return new ThreadLocalDao<T>(c);
		}
	}

	protected static ThreadLocalDao<Flashmob> flashmobThreadLocal = ThreadLocalDao.get(Flashmob.class);
	public static MongoDao<Flashmob> getFlashmobDao() { return flashmobThreadLocal.get(); }

	protected static ThreadLocalDao<Activity> activityThreadLocal = ThreadLocalDao.get(Activity.class);
	public static MongoDao<Activity> getActivityDao() { return activityThreadLocal.get(); }
	
	protected static ThreadLocalDao<Role> roleThreadLocal = ThreadLocalDao.get(Role.class);
	public static MongoDao<Role> getRoleDao() { return roleThreadLocal.get(); }
	
	protected static ThreadLocalDao<Trigger> triggerThreadLocal = ThreadLocalDao.get(Trigger.class);
	public static MongoDao<Trigger> getTriggerDao() { return triggerThreadLocal.get(); }
	
	protected static ThreadLocalDao<Comment> commentThreadLocal = ThreadLocalDao.get(Comment.class);
	public static MongoDao<Comment> getCommentDao() { return commentThreadLocal.get(); } 
	
	protected static ThreadLocalDao<Signal> signalThreadLocal = ThreadLocalDao.get(Signal.class);
	public static MongoDao<Signal> getSignalDao() { return signalThreadLocal.get(); }
	
	protected static ThreadLocalDao<Task> taskThreadLocal = ThreadLocalDao.get(Task.class);
	public static MongoDao<Task> getTaskDao() { return taskThreadLocal.get(); }
	
	protected static ThreadLocalDao<User> userThreadLocal = ThreadLocalDao.get(User.class);
	public static MongoDao<User> getUserDao() { return userThreadLocal.get(); }
}
