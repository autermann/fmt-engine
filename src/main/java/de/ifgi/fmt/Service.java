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
package de.ifgi.fmt;

import static de.ifgi.fmt.update.UpdateFactory.update;

import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.BoundingBox;
import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.mongo.Store;
import de.ifgi.fmt.utils.Utils;
import de.ifgi.fmt.utils.constants.RESTConstants.ShowStatus;
import de.ifgi.fmt.utils.constants.RESTConstants.Sorting;

public class Service {

	private static Service service;

	public static Service getInstance() {
		return (service == null) ? service = new Service() : service;
	}

	private static boolean validateFlashmob(Flashmob f) {

		boolean valid;
		valid = false;

		if (f.getEnd() != null) {
			if (!f.getEnd().isAfter(f.getCreationTime())) {
				return false;
			}
			if (!f.getEnd().isAfter(f.getStart())) {
				return false;
			}
		}

		if (f.getStart().isAfter(f.getCreationTime()) && valid) {
			return false;
		}

		if (f.getActivities().isEmpty() || f.getRoles().isEmpty()
				|| f.getTriggers().isEmpty()) {
			return false;
		}
		
		//TODO hier fehlt noch einiges

		return true;
	}

	private Store store = new Store();

	public Activity addActivity(Activity a, ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		getStore().flashmobs().save(f.addActivity(a));
		return a;
	}

	public Comment addComment(ObjectId flashmob, Comment comment) {
		getStore().comments().save(comment.setFlashmob(getFlashmob(flashmob)));
		return comment;
	}

	public Role addRole(ObjectId flashmob, Role r) {
		Flashmob f = getFlashmob(flashmob);
		getStore().flashmobs().save(f.addRole(r));
		return r;
	}

	public Activity addRoleToActivity(Activity activity, ObjectId role,
			ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		Role r = getRole(f, role);
		Activity a = getActivity(f, activity.getId());
		addRoleToActivity(a, r, f);
		return a;
	}

	public void addRoleToActivity(Activity activity, Role role,
			Flashmob flashmob) {
		activity.addRole(role);
		getStore().roles().save(role);
		getStore().activities().save(activity);
	}

	public Role addRoleToActivity(ObjectId activity, Role role,
			ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		Activity a = getActivity(f, activity);
		Role r = getRole(f, role.getId());
		addRoleToActivity(a, r, f);
		return r;
	}

	public Signal addSignal(Signal s, ObjectId activity, ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		Activity a = getActivity(f, activity);
		getStore().activities().save(a.setSignal(s));
		return s;
	}

	public Task addTask(Task t, ObjectId role, ObjectId activity,
			ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		return addTask(t, getRole(f, role), getActivity(f, activity));
	}

	public Task addTask(Task t, Role role, Activity activity) {
		getStore().activities().save(activity.addTask(role, t));
		return t;
	}

	public Trigger addTrigger(Trigger t, Flashmob flashmob) {
		getStore().flashmobs().save(flashmob.addTrigger(t));
		return t;
	}

	public Trigger addTrigger(Trigger t, ObjectId flashmob) {
		return addTrigger(t, getFlashmob(flashmob));
	}

	public Flashmob createFlashmob(Flashmob f) {
		return getStore().flashmobs().save(f);
	}

	public User createUser(User u) {
		User u2 = getUser(u.getUsername());
		if (u2 != null) {
			throw ServiceError.badRequest("username already used");
		}
		return getStore().users().save(u);
	}

	public void deleteActivity(Flashmob flashmob, Activity activity) {
		getStore().activities().delete(activity);
	}

	public void deleteActivity(Flashmob flashmob, ObjectId activity) {
		deleteActivity(flashmob, getActivity(flashmob, activity));
	}

	public void deleteActivity(ObjectId flashmob, ObjectId activity) {
		deleteActivity(getFlashmob(flashmob), activity);
	}

	public void deleteFlashmob(Flashmob flashmob) {
		getStore().flashmobs().delete(flashmob);
	}

	public void deleteFlashmob(ObjectId flashmob) {
		deleteFlashmob(getFlashmob(flashmob));
	}

	public void deleteTrigger(Flashmob flashmob, ObjectId trigger) {
		deleteTrigger(flashmob, getTrigger(flashmob, trigger));
	}

	public void deleteTrigger(Flashmob flashmob, Trigger trigger) {
		getStore().triggers().delete(trigger);
	}

	public void deleteTrigger(ObjectId flashmob, ObjectId trigger) {
		deleteTrigger(getFlashmob(flashmob), trigger);
	}

	public void deleteUser(String user) {
		deleteUser(getUser(user));
	}

	public void deleteUser(User user) {
		getStore().users().delete(user);
	}

	public List<Activity> getActivities(ObjectId flashmob) {
		return getFlashmob(flashmob).getActivities();
	}

	public List<Activity> getActivitiesForRole(ObjectId role, ObjectId flashmob) {
		return Utils.asList(getRole(getFlashmob(flashmob), role)
				.getActivities());
	}

	public List<Activity> getActivitiesForUser(String user, ObjectId flashmob) {
		return getStore().activities()
				.get(getFlashmob(flashmob), getUser(user));
	}

	public Activity getActivity(Flashmob flashmob, ObjectId activity) {
		Activity a = getStore().activities().get(activity);
		if (!flashmob.getActivities().contains(a)
				|| !a.getFlashmob().equals(flashmob)) {
			throw ServiceError.activityNotFound();
		}
		return a;
	}

	public Activity getActivity(ObjectId flashmob, ObjectId activity) {
		return getActivity(getFlashmob(flashmob), activity);
	}

	public List<Comment> getCommentsForFlashmob(ObjectId flashmob) {
		return getStore().comments().get(getFlashmob(flashmob));
	}

	public Flashmob getFlashmob(ObjectId flashmob) {
		Flashmob f = getStore().flashmobs().get(flashmob);
		if (f.isNotChecked()) {
			if (validateFlashmob(f)) {
				f.setValid();
			} else {
				f.setNotValid();
			}
			getStore().flashmobs().save(f);
		}
		return f;
	}

	public Flashmob getFlashmob(String user, ObjectId flashmob) {
		return getFlashmob(getUser(user), flashmob);
	}

	public Flashmob getFlashmob(User user, ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		if (!f.hasUser(user)) {
			throw ServiceError.flashmobNotFound();
		}
		return f;
	}

	public List<Flashmob> getFlashmobs(int limit, Point near, String user,
			BoundingBox bbox, DateTime from, DateTime to, Sorting sorting,
			boolean descending, ShowStatus show, String search,
			String participant, int minParticipants, int maxParticipants) {
		return getStore().flashmobs().get(limit, near, getUser(user), bbox,
				from, to, sorting, descending, show, search,
				getUser(participant), minParticipants, maxParticipants);
	}

	public List<Flashmob> getFlashmobsFromUser(String user) {
		return getStore().flashmobs().get(getUser(user));
	}

	public Role getRole(Flashmob f, ObjectId role) {
		Role r = getStore().roles().get(role);
		if (!r.getFlashmob().equals(f) || !f.getRoles().contains(r)) {
			throw ServiceError.roleNotFound();
		}
		return r;
	}

	public Role getRole(ObjectId flashmob, ObjectId role) {
		Flashmob f = getFlashmob(flashmob);
		return getRole(f, role);
	}

	public List<Role> getRoles(ObjectId flashmob) {
		return getFlashmob(flashmob).getRoles();
	}

	public List<Role> getRoles(ObjectId activity, ObjectId flashmob) {
		return getActivity(getFlashmob(flashmob), activity).getRoles();
	}

	public Signal getSignal(ObjectId flashmob, ObjectId activity) {
		return getStore().activities().getSignalOfActivity(
				getActivity(getFlashmob(flashmob), activity));
	}

	public Store getStore() {
		return this.store;
	}

	public Task getTask(ObjectId flashmob, ObjectId role, ObjectId activity) {
		Flashmob f = getFlashmob(flashmob);
		Task t = getActivity(f, activity).getTask(getRole(f, role));
		if (t == null) {
			throw ServiceError.taskNotFound();
		}
		return t;
	}

	public Task getTaskForActivity(ObjectId activity, ObjectId flashmob,
			String user) {
		User u = getUser(user);
		Flashmob f = getFlashmob(flashmob);
		Activity a = getActivity(f, activity);
		Role r = getStore().roles().get(f, u);
		if (r == null) {
			throw ServiceError.roleNotFound();
		}
		return a.getTask(r);
	}

	public Trigger getTrigger(Flashmob flashmob, ObjectId trigger) {
		Trigger t = getStore().triggers().get(trigger);
		if (!flashmob.getTriggers().contains(t)
				|| !t.getFlashmob().equals(flashmob)) {
			throw ServiceError.triggerNotFound();
		}
		return t;
	}

	public Trigger getTrigger(ObjectId trigger, ObjectId flashmob) {
		return getTrigger(getFlashmob(flashmob), trigger);
	}

	public Trigger getTriggerOfActivity(ObjectId flashmob, ObjectId activity) {
		return getActivity(getFlashmob(flashmob), activity).getTrigger();
	}

	public List<Trigger> getTriggers(ObjectId flashmob) {
		return getStore().triggers().get(getFlashmob(flashmob));
	}

	public User getUser(String user) {
		if (user == null) {
			return null;
		}
		return getStore().users().get(user);
	}

	public List<User> getUsers(int limit) {
		return getStore().users().get(limit);
	}

	public List<User> getUsersForRole(ObjectId flashmob, ObjectId role,
			int limit) {
		Role r = getRole(flashmob, role);
		return Utils.sublist(r.getUsers(), 0, limit + 1);
	}

	public List<User> getUsersOfFlashmob(ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		Set<User> users = Utils.set();
		for (Role r : f.getRoles()) {
			users.addAll(r.getUsers());
		}
		return Utils.asList(users);
	}

	public User registerUser(User u, ObjectId role, ObjectId flashmob) {
		Role r = getRole(flashmob, role);
		getStore().roles().save(r.addUser(u));
		return u;
	}

	public void removeTriggerFromActivity(ObjectId flashmob, ObjectId activity) {
		Flashmob f = getFlashmob(flashmob);
		Activity a = getActivity(f, activity);
		a.setTrigger(null);
		getStore().activities().save(a);
	}

	public Trigger setTriggerForActivity(ObjectId flashmob, ObjectId activity,
			Trigger t) {
		Flashmob f = getFlashmob(flashmob);
		Activity a = getActivity(f, activity);
		t = getTrigger(f, t.getId());
		a.setTrigger(t);
		getStore().activities().save(a);
		return t;
	}

	public Activity updateActivity(Activity changes, ObjectId activity,
			ObjectId flashmob) {
		return getStore().activities().save(
				update(getActivity(flashmob, activity), changes));
	}

	public Flashmob updateFlashmob(ObjectId id, Flashmob flashmob) {
		return getStore().flashmobs().save(update(getFlashmob(id), flashmob));
	}

	public Role updateRole(Role changes, ObjectId role, ObjectId flashmob) {
		return getStore().roles()
				.save(update(getRole(flashmob, role), changes));
	}

	public Signal updateSignal(Signal signal, ObjectId activity,
			ObjectId flashmob) {
		Activity a = getActivity(flashmob, activity);
		Signal s = getStore().activities().getSignalOfActivity(a);
		update(s, signal);
		getStore().activities().save(a);
		return s;
	}

	public Task updateTask(Task task, ObjectId role, ObjectId activity,
			ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		Activity a = getActivity(f, activity);
		Role r = getRole(f, role);
		Task t = a.getTask(r);
		if (t == null) {
			throw ServiceError.taskNotFound();
		}
		update(t, task);
		getStore().activities().save(a);
		return t;
	}

	public User updateUser(User updates, String user) {
		User old = getUser(user);
		User changed = update(old, updates);
		getStore().users().save(changed);
		return changed;
	}

	public Activity getActivityForUser(String user, ObjectId flashmob,
			ObjectId activity) {
		User u = getUser(user);
		getFlashmob(u, flashmob);
		List<Activity> ac = getActivitiesForUser(user, flashmob);
		Activity fa = null;
		for (Activity a : ac) {
			if (a.getId().equals(activity)) {
				fa = a;
				break;
			}
		}
		if (fa == null) {
			throw ServiceError.activityNotFound();
		}
		return fa;
	}

	public Comment getCommentForFlashmob(ObjectId flashmob, ObjectId comment) {
		Flashmob f = getFlashmob(flashmob);
		Comment c = getStore().comments().get(comment);
		if (!c.getFlashmob().equals(f)) {
			throw ServiceError.commentNotFound();
		}
		return c;
	}

	public Comment updateComment(ObjectId flashmob, ObjectId comment,
			Comment changes) {
		return getStore().comments().save(
				update(getCommentForFlashmob(flashmob, comment), changes));
	}

	public Role getRoleForActivity(ObjectId flashmob, ObjectId activity, ObjectId role) {
		Flashmob f = getFlashmob(flashmob);
		Activity a = getActivity(f, activity);
		Role r =  getRole(f, role);
		if (!a.getRoles().contains(r) || !r.getActivities().contains(a)) {
			throw ServiceError.roleNotFound();
		}
		return r;
	}

	public void removeRoleFromActivity(ObjectId flashmob, ObjectId activity,
			ObjectId role) {
		Flashmob f = getFlashmob(flashmob);
		Role r =  getRole(f, role);
		Activity a = getActivity(f, activity);
		
		r.getActivities().remove(a);
		a.getRoles().remove(r);
		
		getStore().activities().save(a);
		getStore().roles().save(r);
		
	}

	public Activity getActivityForRole(ObjectId flashmob, ObjectId role,
			ObjectId activity) {
		Flashmob f = getFlashmob(flashmob);
		Role r =  getRole(f, role);
		Activity a = getActivity(f, activity);
		if (!a.getRoles().contains(r) || !r.getActivities().contains(a)) {
			throw ServiceError.activityNotFound();
		}
		return a;
	}
}
