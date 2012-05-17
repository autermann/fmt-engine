/*
 * Copyright (C) 2012  Christian Autermann, Dustin Demuth, Maurin Radtke
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
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
import org.codehaus.jettison.json.JSONException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.query.Query;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.json.JSONFactory;
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
	
	/* saving order and responsibilities:
		Flashmob 
			-> Triggers
			-> Activities
				-> Signal
				-> Task
			-> Comments
			-> Roles
		User
	*/
	
	public Flashmob getFlashmob(ObjectId id) {
		log.debug("Getting Flashmob {}", id);
		Flashmob f = getFlashmobDao().get(id);
		if (f == null) {
			throw ServiceError.flashmobNotFound();
		}
		return f;
	}
	
	public Flashmob saveFlashmob(Flashmob f) {
		log.debug("Saving Flashmob {}", f);
		getTriggerDao().saveAll(f.getTriggers());
		getCommentDao().saveAll(f.getComments());
		getRoleDao().saveAll(f.getRoles());
		saveActivities(f.getActivities());
		getFlashmobDao().save(f);
		return f;
	}
	
	
	public void saveFlashmobs(Iterable<Flashmob> flashmobs) {
		log.debug("Saving Flashmobs");
		for (Flashmob f : flashmobs) {
			saveFlashmob(f);
		}
	}

	public Activity getActivity(ObjectId id) {
		log.debug("Getting Activity {}", id);
		Activity a = getActivityDao().get(id);
		if (a == null) {
			throw ServiceError.activityNotFound();
		}
		return a;
	}
	
	public Activity saveActivity(Activity a) {
		log.debug("Saving Acitivity {}", a);
		getSignalDao().save(a.getSignal());
		saveTasks(a.getTasks().values());
		getActivityDao().save(a);
		return a;
	}
	
	public void saveActivities(Iterable<Activity> activities) {
		log.debug("Saving Activities");
		for (Activity a : activities) {
			saveActivity(a);
		}
	}

	public User getUser(ObjectId id) {
		log.debug("Getting User {}", id);
		User u = getUserDao().get(id);
		if (u == null) {
			throw ServiceError.userNotFound();
		}
		return u;
	}
	
	public List<User> getUsers(int limit) {
		return getUserDao().find(getUserDao().createQuery().limit(limit)).asList();
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
	
	public void deleteUser(User u) {
		log.debug("Deleting User {}", u);
		//TODO coordinator
		
		
		deleteFlashmobs(getFlashmobs(getFlashmobDao().createQuery().field(Flashmob.COORDINATOR).equal(u)));
		
		deleteUserFromRoles(u, getRolesOfUser(u));
		deleteUserFromComments(u, getCommentsOfUser(u));
		DaoFactory.getUserDao().delete(u);
	}
	
	public void deleteUsers(Iterable<User> users) {
		log.debug("Deleting Users");
		for (User u : users) {
			deleteUser(u);
		}
	}

	public Role getRole(ObjectId id) {
		log.debug("Getting Role {}", id);
		Role r = getRoleDao().get(id);
		if (r == null) {
			throw ServiceError.roleNotFound();
		}
		return r;
	}
	
	public Role saveRole(Role role) {
		log.debug("Saving Role {}", role);
		getRoleDao().save(role);
		return role;
	}
	
	public void saveRoles(Iterable<Role> roles) {
		log.debug("Saving Roles");
		for (Role r : roles) {
			saveRole(r);
		}
	}

	public Trigger getTrigger(ObjectId id) {
		log.debug("Getting Trigger {}", id);
		Trigger t = getTriggerDao().get(id);
		if (t == null) {
			throw ServiceError.triggerNotFound();
		}
		return t;
	}
	
	public Trigger saveTrigger(Trigger t) {
		log.debug("Saving Trigger {}", t);
		getTriggerDao().save(t);
		return t;
	}
	
	public void saveTriggers(Iterable<Trigger> triggers) {
		log.debug("Saving Triggers");
		for (Trigger t : triggers) {
			saveTrigger(t);
		}
	}
	
	public void deleteTrigger(Trigger t) {
		log.debug("Deleting Trigger {}", t);
		for (Activity a : getActivitiesOfTrigger(t)) {
			saveActivity(a.setTrigger(null));
		}
		getTriggerDao().delete(t);
	}
	
	public void deleteTriggers(List<Trigger> triggers) {
		log.debug("Deleting Triggers");
		for (Trigger t : triggers) {
			deleteTrigger(t);
		}
	}
	
	public Task getTask(ObjectId id) {
		log.debug("Getting Task {}", id);
		Task t = getTaskDao().get(id);
		if (t == null) {
			throw ServiceError.taskNotFound();
		}
		return t;
	}
	
	public Task saveTask(Task t) {
		log.debug("Saving Task {}", t);
		getTaskDao().save(t);
		return t;
	}
	
	public void saveTasks(Iterable<Task> tasks) {
		log.debug("Saving Tasks");
		for (Task t : tasks) {
			saveTask(t);
		}
	}
	
	public Comment getComment(ObjectId id) {
		log.debug("Getting Comment {}", id);
		Comment c = getCommentDao().get(id);
		if (c == null) {
			throw ServiceError.commentNotFound();
		}
		return c;
	}
	
	public Comment saveComment(Comment c) {
		log.debug("Saving comment {}", c);
		getCommentDao().save(c);
		return c;
	}
	
	public void saveComments(Iterable<Comment> comments) {
		log.debug("Saving comments");
		for (Comment c : comments) {
			saveComment(c);
		}
	}
	
	public void deleteComment(Comment c){
		log.debug("Deleting Comment {}", c);
		getCommentDao().delete(c);
	}
	
	public void deleteComments(List<Comment> c){
		log.debug("Deleting Comments");
		getCommentDao().deleteAll(c);
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
	
	public List<Comment> getCommentsForFlashmob(Flashmob f) {
		return getComments(Q.commentsOfFlashmob(f));
	}
	
	public void deleteCommentsForFlashmob(Flashmob f) {
		getCommentDao().deleteByQuery(Q.commentsOfFlashmob(f));
	}
	
	public void deleteFlashmob(Flashmob f) {
		
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
	}
	
	public void deleteFlashmobs(Iterable<Flashmob> flashmobs) {
		for (Flashmob f : flashmobs) {
			deleteFlashmob(f);
		}
	}
	
	public void deleteUserFromRole(User u, Role r) {
		log.debug("Deleting User {} from Role {}", u, r);
		saveRole(r.removeUser(u));
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
	
	
	
	protected static List<Flashmob> getFlashmobs(Query<Flashmob> q) {
		return getFlashmobDao().find(l(q)).asList();
	}
	
	protected static Flashmob getFlashmob(Query<Flashmob> q) {
		return getFlashmobDao().find(l(q)).get();
	}
	
	protected static List<Trigger> getTriggers(Query<Trigger> q) {
		return getTriggerDao().find(l(q)).asList();
	}
	
	protected static Trigger getTrigger(Query<Trigger> q) {
		return getTriggerDao().find(l(q)).get();
	}
	
	protected static List<User> getUsers(Query<User> q) {
		return getUserDao().find(l(q)).asList();
	}
	
	protected static User getUser(Query<User> q) {
		return getUserDao().find(l(q)).get();
	}
	
	protected static List<Role> getRoles(Query<Role> q) {
		return getRoleDao().find(l(q)).asList();
	}
	
	protected static Role getRole(Query<Role> q) {
		return getRoleDao().find(l(q)).get();
	}
	
	protected static List<Comment> getComments(Query<Comment> q) {
		return getCommentDao().find(l(q)).asList();
	}
	
	protected static Comment getComment(Query<Comment> q) {
		return getCommentDao().find(l(q)).get();
	}

	protected static List<Activity> getActivities(Query<Activity> q) {
		return getActivityDao().find(l(q)).asList();
	}
	
	protected static Activity getActivity(Query<Activity> q) {
		return getActivityDao().find(l(q)).get();
	}

	protected static <T> Query<T> l(Query<T> q) {
		if (log.isDebugEnabled())
			log.debug("Mongo query: {}", q.toString());
		return q;
	}
	
	public static final class Q {
		
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
			.setCoordinator(user2)
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
		
		try {
			System.err.println(JSONFactory.getEncoder(Flashmob.class).encode(f, null).toString(4));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public List<User> getUsersForRole(Role r, int limit) {
		
		
		// TODO Auto-generated method stub
		return null;
	}
	
}
