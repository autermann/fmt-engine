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

	private Store store = new Store();

	public Store getStore() {
		return this.store;
	}

	public Flashmob getFlashmob(ObjectId flashmob) {
		Flashmob f = getStore().getFlashmob(flashmob);
		if (f.isNotChecked()) {
			if (validateFlashmob(f)) {
				f.setValid();
			} else {
				f.setNotValid();
			}
			getStore().saveFlashmob(f);
		}
		return f;
	}

	private static boolean validateFlashmob(Flashmob f) {
		return false; // TODO validity check
	}

	public Flashmob createFlashmob(Flashmob f) {
		return getStore().saveFlashmob(f);
	}

	public Flashmob updateFlashmob(ObjectId id, Flashmob flashmob) {
		return getStore().saveFlashmob(update(getFlashmob(id), flashmob));
	}

	public Trigger addTrigger(Trigger t, ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		f.addTrigger(t);
		getStore().saveFlashmob(f);
		return t;
	}

	public Role getRole(Flashmob f, ObjectId role) {
		Role r = getStore().getRole(role);
		if (!r.getFlashmob().equals(f) || !f.getRoles().contains(r)) {
			throw ServiceError.roleNotFound();
		}
		return r;
	}

	public Role getRole(ObjectId role, ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		return getRole(f, role);
	}

	public Role updateRole(Role changes, ObjectId role, ObjectId flashmob) {
		return getStore().saveRole(update(getRole(flashmob, role), changes));
	}

	public User registerUser(User u, ObjectId role, ObjectId flashmob) {
		Role r = getRole(flashmob, role);
		getStore().saveRole(r.addUser(u));
		return u;
	}

	public Activity getActivity(Flashmob flashmob, ObjectId activity) {
		Activity a = getStore().getActivity(activity);
		if (!flashmob.getActivities().contains(a)
				|| !a.getFlashmob().equals(flashmob)) {
			throw ServiceError.activityNotFound();
		}
		return a;
	}

	public Activity getActivity(ObjectId flashmob, ObjectId activity) {
		return getActivity(getFlashmob(flashmob), activity);
	}

	public Task addTask(Task t, ObjectId role, ObjectId activity,
			ObjectId flashmob) {
		Role r = getRole(flashmob,role);
		Activity a = getActivity(flashmob, activity);
		getStore().saveActivity(a.addTask(r, t));
		return t;
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
		getStore().saveActivity(a);
		return t;
	}

	public Activity addActivity(Activity a, ObjectId role, ObjectId flashmob) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public Role addRole(Role r, ObjectId activity, ObjectId flashmob) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public Signal addSignal(Signal s, ObjectId activity, ObjectId flashmob) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public Signal updateSignal(Signal s, ObjectId activity, ObjectId flashmob) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public Activity updateActivity(Activity a, ObjectId activity,
			ObjectId flashmob) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public Trigger addTrigger(Trigger t, ObjectId activity, ObjectId flashmob) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public Activity addActivity(Activity a, ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		getStore().saveFlashmob(f.addActivity(a));
		return a;
	}

	public Role addRole(ObjectId flashmob, Role r) {
		Flashmob f = getFlashmob(flashmob);
		getStore().saveFlashmob(f.addRole(r));
		return r;
	}

	public Flashmob getFlashmob(ObjectId user, ObjectId flashmob) {
		User u = getUser(user);
		Flashmob f = getFlashmob(flashmob);
		if (!f.hasUser(u)) {
			throw ServiceError.flashmobNotFound();
		}
		return f;
	}

	public User updateUser(User updates, ObjectId user) {
		return getStore().saveUser(update(getUser(user), updates));
	}

	public User getUser(ObjectId user) {
		if (user == null) {
			return null;
		}
		return getStore().getUser(user);
	}

	public void deleteUser(ObjectId user) {
		getStore().deleteUser(getUser(user));
	}

	public User createUser(User u) {
		return getStore().saveUser(u);
	}

	public List<User> getUsers(int limit) {
		return getStore().getUsers(limit);
	}

	public List<User> getUsersForRole(ObjectId flashmob, ObjectId role,
			int limit) {
		Role r = getRole(flashmob, role);
		return Utils.sublist(r.getUsers(), 0, limit + 1);
	}

	public List<Role> getRoles(ObjectId flashmob) {
		return getFlashmob(flashmob).getRoles();
	}

	public Task getTask(ObjectId flashmob, ObjectId role, ObjectId activity) {
		Flashmob f = getFlashmob(flashmob);
		Task t = getActivity(f, activity).getTask(getRole(f, role));
		if (t == null) {
			throw ServiceError.taskNotFound();
		}
		return t;
	}

	public List<Role> getRoles(ObjectId activity, ObjectId flashmob) {
		return getActivity(getFlashmob(flashmob), activity).getRoles();
	}

	public List<Activity> getActivitiesForRole(ObjectId role, ObjectId flashmob) {
		return Utils.asList(getRole(getFlashmob(flashmob), role).getActivities());
	}

	public Trigger getTrigger(ObjectId trigger, ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		Trigger t = getStore().getTrigger(trigger);
		if (!f.getTriggers().contains(t) || !t.getFlashmob().equals(flashmob)) {
			throw ServiceError.triggerNotFound();
		}
		return t;
	}

	public List<Trigger> getTriggers(ObjectId flashmob) {
		return getStore().getTriggersOfFlashmob(getFlashmob(flashmob));
	}

	public List<User> getUsersOfFlashmob(ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		Set<User> users = Utils.set();
		for (Role r : f.getRoles()) {
			users.addAll(r.getUsers());
		}
		return Utils.asList(users);
	}

	public List<Flashmob> getFlashmobsFromUser(ObjectId user) {
		return getStore().getFlashmobsOfUser(getUser(user));
	}

	public List<Activity> getActivitiesForUser(ObjectId user, ObjectId flashmob) {
		return getStore().getActivitiesForUser(getFlashmob(flashmob), getUser(user));
	}

	public Task getTaskForActivity(ObjectId activity, ObjectId flashmob, ObjectId user) {
		User u = getUser(user);
		Flashmob f = getFlashmob(flashmob);
		Activity a = getActivity(f, activity);
		Role r = getStore().getRoleOfUserInFlashmob(f, u);
		if (r == null) {
			throw ServiceError.roleNotFound();
		}
		return a.getTask(r);
	}

	public List<Activity> getActivities(ObjectId flashmob) {
		return getFlashmob(flashmob).getActivities();
	}

	public List<Flashmob> getFlashmobs(int limit, Point near, ObjectId user,
			BoundingBox bbox, DateTime from, DateTime to, Sorting sorting,
			boolean descending, ShowStatus show, String search,
			ObjectId participant) {
		return getStore().getFlashmobs(limit, near, getUser(user), bbox, from,
				to, sorting, descending, show, search, getUser(participant));
	}

	public Signal getSignal(ObjectId flashmob, ObjectId activity) {
		return getActivity(getFlashmob(flashmob), activity).getSignal();
	}

}
