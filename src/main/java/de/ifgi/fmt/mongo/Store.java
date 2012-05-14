package de.ifgi.fmt.mongo;

import static de.ifgi.fmt.mongo.DaoFactory.getActivityDao;
import static de.ifgi.fmt.mongo.DaoFactory.getCommentDao;
import static de.ifgi.fmt.mongo.DaoFactory.getFlashmobDao;
import static de.ifgi.fmt.mongo.DaoFactory.getRoleDao;
import static de.ifgi.fmt.mongo.DaoFactory.getSignalDao;
import static de.ifgi.fmt.mongo.DaoFactory.getTaskDao;
import static de.ifgi.fmt.mongo.DaoFactory.getTriggerDao;
import static de.ifgi.fmt.mongo.DaoFactory.getUserDao;

import java.util.List;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.query.Query;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.Role.Category;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.model.signal.VibrationSignal;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.model.trigger.TimeTrigger;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.utils.Utils;

public class Store {
	
	private static final Logger log = LoggerFactory.getLogger(Store.class);
	
	public Flashmob getFlashmob(ObjectId id) {
		log.debug("Getting Flashmob {}", id);
		return getFlashmobDao().get(id);
	}

	public Flashmob saveFlashmob(Flashmob f) {
		getTriggerDao().saveAll(f.getTriggers());
		getCommentDao().saveAll(f.getComments());
		getRoleDao().saveAll(f.getRoles());
		getActivityDao().saveAll(f.getActivities());
		for (Activity a : f.getActivities()) {
			getSignalDao().save(a.getSignal());
			getTaskDao().saveAll(a.getTasks().values());
		}
		getFlashmobDao().save(f);
		return f;
	}
	
	public List<Trigger> getTriggersOfFlashmob(Flashmob f) {
		log.debug("Getting Triggers of Flashmob {}", f);
		return getTriggers(Q.triggersOfFlashmob(f));
	}
	
	public void deleteTriggersOfFlashmob(Flashmob f) {
		log.debug("Getting Triggers of Flashmob {}", f);
		getTriggerDao().deleteByQuery(Q.triggersOfFlashmob(f));
	}
	
	public List<Activity> getActivitiesOfTrigger(Trigger t) {
		return getActivities(Q.activitiesOfTrigger(t));
	}
	
	public void deleteTrigger(Trigger t) {
		for (Activity a : getActivitiesOfTrigger(t)) {
			getActivityDao().save(a.setTrigger(null));
		}
		getTriggerDao().delete(t);
	}
	
	public void deleteTriggers(List<Trigger> triggers) {
		for (Trigger t : triggers) {
			deleteTrigger(t);
		}
	}
	
	public List<Comment> getCommentsForFlashmob(Flashmob f) {
		return getComments(Q.commentsOfFlashmob(f));
	}
	
	public void deleteCommentsForFlashmob(Flashmob f) {
		getCommentDao().deleteByQuery(Q.commentsOfFlashmob(f));
	}
	
	public void deleteComment(Comment c){
		getCommentDao().delete(c);
	}
	
	public void deleteComments(List<Comment> c){
		getCommentDao().deleteAll(c);
	}
	
	public Flashmob deleteFlashmob(Flashmob f) {
		
		deleteTriggers(getTriggersOfFlashmob(f));
		deleteCommentsForFlashmob(f);
		getCommentDao().saveAll(f.getComments());
		getRoleDao().saveAll(f.getRoles());
		getActivityDao().saveAll(f.getActivities());
		for (Activity a : f.getActivities()) {
			getSignalDao().save(a.getSignal());
			getTaskDao().saveAll(a.getTasks().values());
		}
		getFlashmobDao().save(f);
		return f;
	}
	
	public User saveUser(User u) {
		log.debug("Saving User {}", u);
		getUserDao().save(u);
		return u;
	}

	public void saveUsers(Iterable<User> u) {
		log.debug("Saving Users");
		getUserDao().saveAll(u);
	}
	
	public User getUser(ObjectId id) {
		log.debug("Getting User {}", id);
		return getUserDao().get(id);
	}
	
	public void deleteUserFromRole(User u, Role r) {
		log.debug("Deleting User {} from Role {}", u, r);
		getRoleDao().save(r.removeUser(u));
	}
	
	public void deleteUserFromRoles(User u, List<Role> roles) {
		log.debug("Deleting User {} from Roles", u);
		for (Role r : roles) {
			deleteUserFromRole(u, r);
		}
	}
	
	public List<Flashmob> getFlashmobsOfUser(User u) {
		log.debug("Getting Flashmobs of User {}", u);
		return getFlashmobs(Q.flashmobsOfUser(u));
	}
	
	public void saveComment(Comment c) {
		log.debug("Saving comment {}", c);
		getCommentDao().save(c);
		
	}
	
	public void deleteUserFromComment(User u, Comment c) {
		log.debug("Deleting User {} from Comment {}", u, c);
		saveComment(c.setUser(null));
	}
	
	public void deleteUserFromComments(User u, List<Comment> comments) {
		log.debug("Deleting User {} from Comments", u);
		for (Comment c : comments) {
			deleteUserFromComment(u, c);
		}
	}
	
	public List<Role> getRolesOfUser(User u) {
		log.debug("Getting Roles of User {}", u);
		return getRoles(Q.rolesOfUser(u));
	}
	
	public List<Comment> getCommentsOfUser(User u) {
		log.debug("Getting Comments of User {}", u);
		return getComments(Q.commentsOfUser(u));
	}
	
	public void deleteUser(User u) {
		log.debug("Deleting User {}", u);
		deleteUserFromRoles(u, getRolesOfUser(u));
		deleteUserFromComments(u, getCommentsOfUser(u));
		DaoFactory.getUserDao().delete(u);
	}
	
	
	protected static List<Flashmob> getFlashmobs(Query<Flashmob> q) {
		return getFlashmobDao().find(l(q)).asList();
	}
	
	protected static List<Trigger> getTriggers(Query<Trigger> q) {
		return getTriggerDao().find(l(q)).asList();
	}
	
	protected static List<User> getUsers(Query<User> q) {
		return getUserDao().find(l(q)).asList();
	}
	
	protected static List<Role> getRoles(Query<Role> q) {
		return getRoleDao().find(l(q)).asList();
	}
	
	protected static List<Comment> getComments(Query<Comment> q) {
		return getCommentDao().find(l(q)).asList();
	}

	protected static List<Activity> getActivities(Query<Activity> q) {
		return getActivityDao().find(l(q)).asList();
	}

	protected static <T> Query<T> l(Query<T> q) {
		if (log.isDebugEnabled())
			log.debug("Mongo query: {}", q.toString());
		return q;
	}
	
	private static final class Q {
		
		public static Query<Trigger> triggersOfFlashmob(Flashmob f) {
			return getTriggerDao().createQuery().field(Trigger.FLASHMOB).equal(f);
		}
		
		public static Query<Activity> activitiesOfTrigger(Trigger t) {
			return getActivityDao().createQuery().field(Activity.TRIGGER).equal(t);
		}

		public static Query<Comment> commentsOfFlashmob(Flashmob f) {
			return getCommentDao().createQuery().field(Comment.FLASHMOB).equal(f);
		}
		
		public static Query<Comment> commentsOfUser(User u) {
			return getCommentDao().createQuery().field(Comment.USER).equal(u);
		}
		
		public static Query<Flashmob> flashmobsOfUser(User u) {
			return getFlashmobDao().createQuery().field(Flashmob.ROLES + "." + Role.USERS).hasThisElement(u);
		}
		
		public static Query<Role> rolesOfUser(User u) {
			return getRoleDao().createQuery().field(Role.USERS).hasThisElement(u);
		}
	}
	
	
	
	
	
	public static void main(String[] args) {
		
		MongoDB db = MongoDB.getInstance();
		db.getMongo().dropDatabase(db.getDatabase());
		
		
		GeometryFactory gf = new GeometryFactory();
		DateTime begin = new DateTime();
		
		Point p = gf.createPoint(new Coordinate(52.0, 7.0));
		
		User user1 = new User().setUsername("user1").setEmail("user1@fmt.de").setPassword("password1");
		User user2 = new User().setUsername("user2").setEmail("user2@fmt.de").setPassword("password2");
		User user3 = new User().setUsername("user3").setEmail("user3@fmt.de").setPassword("password3");;
		
		
		Role role1 = new Role()
			.setCategory(Category.EASY)
			.setDescription("Rolle 1")
			.setMaxCount(-1)
			.setMinCount(50)
			.setStartPoint(p)
			.setUsers(Utils.set(user1,user2));
		
		Role role2 = new Role()
			.setCategory(Category.EASY)
			.setDescription("Rolle 2")
			.setMaxCount(-1)
			.setMinCount(40)
			.setStartPoint(p)
			.setUsers(Utils.set(user3));
		
		Trigger t = new TimeTrigger()
			.setTime(begin.plusMinutes(5));
		
		Activity activity = new Activity()
			.setDescription("Activity 1")
			.setTrigger(t)
			.setSignal(new VibrationSignal())
			.addTask(role1, new Task().setDescription("Geh nach links"))
			.addTask(role2, new Task().setDescription("Geh nach rechts"));

		
		Flashmob f = new Flashmob()
			.addRole(role1)
			.addRole(role2)
			.addTrigger(t)
			.addComment(new Comment()
				.setText("war ganz dolle")
				.setUser(user1)
				.setTime(begin.plusHours(20)))
			.addActivity(activity)
			.setDescription("Was weiß ich")
			.setEnd(begin.plusHours(2))
			.setStart(begin)
			.setPublic(false)
			.setKey("geheim")
			.setTitle("Ein FlashMob")
			.setLocation(p);
		
		
		Store s = new Store();
		s.saveUsers(Utils.list(user1,user2,user3));
		s.saveFlashmob(f);
		
		ObjectId oid  = f.getId();
		
		s.deleteUser(user1);
		
		f = s.getFlashmob(oid);
	}
	
	
}
