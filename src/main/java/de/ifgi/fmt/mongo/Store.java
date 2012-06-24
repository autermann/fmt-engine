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
import static de.ifgi.fmt.mongo.DaoFactory.getTriggerDao;
import static de.ifgi.fmt.mongo.DaoFactory.getUserDao;

import java.util.List;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.mapping.Mapper;
import com.google.code.morphia.query.Query;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.BoundingBox;
import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.Role.Category;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.model.signal.VibrationSignal;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.model.trigger.TimeTrigger;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.mongo.stores.Activities;
import de.ifgi.fmt.mongo.stores.Comments;
import de.ifgi.fmt.mongo.stores.Flashmobs;
import de.ifgi.fmt.mongo.stores.Roles;
import de.ifgi.fmt.mongo.stores.Tasks;
import de.ifgi.fmt.mongo.stores.Triggers;
import de.ifgi.fmt.mongo.stores.Users;
import de.ifgi.fmt.utils.Utils;
import de.ifgi.fmt.utils.constants.RESTConstants.ShowStatus;

public class Store {
	private static final String DB_REF_ID = "$id";

	private static final Logger log = LoggerFactory.getLogger(Store.class);
	
	public static final class Queries {

		public static Query<Trigger> allTriggers() {
			return getTriggerDao().createQuery();
		}

		public static Query<Trigger> triggersOfFlashmob(Flashmob f) {
			return getTriggerDao().createQuery().field(Trigger.FLASHMOB).equal(f);
		}

		public static Query<Activity> activitiesOfFlashmob(Flashmob f) {
			return getActivityDao().createQuery().field(Activity.FLASHMOB).equal(f);
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
			return hasUser(getFlashmobDao().createQuery(), u);
		}

		public static Query<Flashmob> flashmobsByUser(User u) {
			return coordinatedBy(getFlashmobDao().createQuery(), u);
		}

		public static Query<Role> rolesOfUser(User u) {
			return getRoleDao().createQuery().field(Role.USERS).hasThisElement(u);
		}

		public static Query<Role> rolesOfUserInFlashmob(User u, Flashmob f) {
			return Queries.rolesOfUser(u).field(Role.FLASHMOB).equal(f);
		}

		public static Query<Flashmob> near(Query<Flashmob> q, Point p) {
			return q.field(Flashmob.LOCATION).near(p.getX(), p.getY(), true);
		}

		public static Query<Flashmob> in(Query<Flashmob> q, BoundingBox bbox) {
			return q.field(Flashmob.LOCATION).within(bbox.getLeft(),
					bbox.getBottom(), bbox.getRight(), bbox.getTop());
		}

		public static Query<Flashmob> coordinatedBy(Query<Flashmob> q, User u) {
			return q.field(Flashmob.COORDINATOR).equal(u);
		}

		public static Query<Flashmob> after(Query<Flashmob> q, DateTime dt) {
			return q.field(Flashmob.START).greaterThanOrEq(dt);
		}

		public static Query<Flashmob> before(Query<Flashmob> q, DateTime dt) {
			q.or(q.and(q.criteria(Flashmob.END).equal(null),
					   q.criteria(Flashmob.START).lessThanOrEq(dt)),
			     q.criteria(Flashmob.END).lessThanOrEq(dt));
			return q;
		}

		public static Query<Flashmob> search(Query<Flashmob> q, String search) {
			q.or(q.criteria(Flashmob.TITLE).containsIgnoreCase(search),
					q.criteria(Flashmob.DESCRIPTION).containsIgnoreCase(search));
			return q;
		}

		public static Query<Flashmob> hasUser(Query<Flashmob> q, User u) {
			DBObject keys = new BasicDBObject(Role.FLASHMOB + "." + DB_REF_ID, true);
			DBObject query  = new BasicDBObject(Role.USERS + "." + DB_REF_ID, u.getUsername());
			List<ObjectId> fids = Utils.list();
			for (DBObject r : getRoleDao().getCollection().find(query, keys)) {
				fids.add((ObjectId) ((DBObject) r.get(Role.FLASHMOB)).get(DB_REF_ID));
			}
			return q.field(Mapper.ID_KEY).hasAnyOf(fids);
		}

		public static Query<Flashmob> isPublic(Query<Flashmob> q,
				ShowStatus showStatus) {
			switch (showStatus) {
			case PUBLIC:
				q.field(Flashmob.PUBLIC).equal(true);
				break;
			case PRIVATE:
				q.field(Flashmob.PUBLIC).equal(false);
				break;
			}
			return q;
		}

		public static Query<User> userByAuthToken(String token) {
			return getUserDao().createQuery().field(User.AUTH_TOKEN).equal(token);
		}

		public static Query<User> userByName(String username) {
			return getUserDao().createQuery().field(User.USERNAME).equal(username);
		}

	}

	/*
	 * saving order and responsibilities: Flashmob -> Triggers -> Activities ->
	 * Signal -> Task -> Comments -> Roles User
	 */

	public static <T> Query<T> g(Query<T> q) {
		if (log.isDebugEnabled()) {
			log.debug("Querying for {}: {}", q.getEntityClass(), q.toString());
		}
		return q;
	}

	public static <T> Query<T> r(Query<T> q) {
		if (log.isDebugEnabled()) {
			log.debug("Deleting results for query for {}: {}",
					q.getEntityClass(), q.toString());
		}
		return q;
	}
	
	private Flashmobs flashmobs;
	private Triggers triggers;
	private Users users;
	private Activities activities;
	private Roles roles;
	private Tasks tasks;
	private Comments comments;
	
	public Flashmobs flashmobs() {
		return (flashmobs == null) ? flashmobs = new Flashmobs(this) : flashmobs;
	}

	public Triggers triggers() {
		return (triggers == null) ? triggers = new Triggers(this) : triggers;
	}

	public Activities activities() {
		return (activities == null) ? activities = new Activities(this) : activities;
	}

	public Users users() {
		return (users == null) ? users = new Users(this) : users;
	}

	public Roles roles() {
		return (roles == null) ? roles = new Roles(this) : roles;
	}

	public Tasks tasks() {
		return (tasks == null) ? tasks = new Tasks(this) : tasks;
	}

	public Comments comments() {
		return (comments == null) ? comments = new Comments(this) : comments;
	}
	
	public static void main(String[] args) {

		MongoDB db = MongoDB.getInstance();
		db.getMongo().dropDatabase(db.getDatabase());

		GeometryFactory gf = new GeometryFactory();
		DateTime begin = new DateTime();

		Point p = gf.createPoint(new Coordinate(52.0, 7.0));
		p.setSRID(4326);
		User user1 = new User().setUsername("user1").setEmail("user1@fmt.de")
				.setPassword("password1");
		User user2 = new User().setUsername("user2").setEmail("user2@fmt.de")
				.setPassword("password2");
		User user3 = new User().setUsername("user3").setEmail("user3@fmt.de")
				.setPassword("password3");
		;

		Role role1 = new Role().setCategory(Category.EASY)
				.setDescription("Rolle 1").setMaxCount(-1).setMinCount(50)
				.setStartPoint(p).setUsers(Utils.set(user1, user2));

		Role role2 = new Role().setCategory(Category.EASY)
				.setDescription("Rolle 2").setMaxCount(-1).setMinCount(40)
				.setStartPoint(p).setUsers(Utils.set(user3));

		Trigger t = new TimeTrigger().setTime(begin.plusMinutes(5));

		Activity activity = new Activity().setDescription("Activity 1")
				.setTrigger(t).setSignal(new VibrationSignal())
				.addTask(role1, new Task().setDescription("Geh nach links"))
				.addTask(role2, new Task().setDescription("Geh nach rechts"));

		Flashmob f = new Flashmob()
				.addRole(role1)
				.setCoordinator(user2)
				.addRole(role2)
				.addTrigger(t)
				.addActivity(activity).setDescription("Was weiß ich")
				.setEnd(begin.plusHours(2)).setStart(begin).setPublic(false)
				.setKey("geheim").setTitle("Ein FlashMob").setLocation(p);

		Store s = new Store();
		s.users().save(Utils.list(user1, user2, user3));
		s.flashmobs().save(f);
		s.comments().save(new Comment().setText("war ganz dolle").setUser(user1)
				.setTime(begin.plusHours(20)));

		ObjectId oid = f.getId();

		s.users().delete(user1);

		f = s.flashmobs().get(oid);

		try {
			System.err.println(JSONFactory.getEncoder(Flashmob.class)
					.encode(f, null).toString(4));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}