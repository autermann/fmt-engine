package de.ifgi.fmt.mongo.store;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
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
	protected static ThreadLocalDao<Activity> activityThreadLocal = ThreadLocalDao.get(Activity.class);
	protected static ThreadLocalDao<Role> roleThreadLocal = ThreadLocalDao.get(Role.class);
	protected static ThreadLocalDao<Trigger> triggerThreadLocal = ThreadLocalDao.get(Trigger.class);
	protected static ThreadLocalDao<Comment> commentThreadLocal = ThreadLocalDao.get(Comment.class);
	
	public static MongoDao<Flashmob> getFlashmobDao() {
		return flashmobThreadLocal.get();
	}
	
	public static MongoDao<Activity> getActivityDao() {
		return activityThreadLocal.get();
	}
	
	public static MongoDao<Role> getRoleDao() {
		return roleThreadLocal.get();
	}
	
	public static MongoDao<Trigger> getTriggerDao() {
		return triggerThreadLocal.get();
	}
	
	public static MongoDao<Comment> getCommentDao() {
		return commentThreadLocal.get();
	}
}
