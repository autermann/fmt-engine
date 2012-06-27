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
	
	/**
	 * 
	 * @return an instance of the running service
	 */
	public static Service getInstance() {
		return (service == null) ? service = new Service() : service;
	}
	
	/**
	 * Decides whether a flashmob is valid
	 * @param f a flashmob which has to be validated
	 */
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

	/**
	 * Adds an ACTIVITY to a FLASHMOB
	 * @param a the activity which shall be added
	 * @param flashmob the id of the flashmob which shall receive the activity
	 * @return the added activity
	 */
	public Activity addActivity(Activity a, ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		getStore().flashmobs().save(f.addActivity(a));
		return a;
	}

	/**
	 * Adds a COMMENT tot a FLASHMOB
	 * @param flashmob the id of a flashmob which shall receive a comment
	 * @param comment the comment which should be added to the flashmob
	 * @return a comment object
	 */
	public Comment addComment(ObjectId flashmob, Comment comment) {
		getStore().comments().save(comment.setFlashmob(getFlashmob(flashmob)));
		return comment;
	}

	/**
	 * Adds a ROLE to a FLASHMOB
	 * @param flashmob the flashmob which shall recieve a role
	 * @param r the role which shall be added to the flashmob
	 * @return the added role 
	 */
	public Role addRole(ObjectId flashmob, Role r) {
		Flashmob f = getFlashmob(flashmob);
		getStore().flashmobs().save(f.addRole(r));
		return r;
	}

	/**
	 * Adds a ROLE to an ACTIVITY
	 * @param activity an activity which shall receive the role
	 * @param role the id of an role which shall be added to the activity
	 * @param flashmob the id of a flashmob which is associated to the activity
	 * @return the added activity
	 */
	public Activity addRoleToActivity(Activity activity, ObjectId role,
			ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		Role r = getRole(f, role);
		Activity a = getActivity(f, activity.getId());
		addRoleToActivity(a, r, f);
		return a;
	}

	/**
	 * Adds a ROLE to an ACTIVITY
	 * @param activity an activity which shall receive the role
	 * @param role the role which shall be added to the activity
	 * @param flashmob a flashmob which is associated to the activity
	 * @return the added activity
	 */
	public void addRoleToActivity(Activity activity, Role role,
			Flashmob flashmob) {
		activity.addRole(role);
		getStore().roles().save(role);
		getStore().activities().save(activity);
	}

	/**
	 * Adds a ROLE to an ACTIVITY
	 * @param activity the ID of an activity which shall receive the role
	 * @param role the role which shall be added to the activity
	 * @param flashmob th id of a flashmob which is associated to the activity
	 * @return the added activity
	 */
	public Role addRoleToActivity(ObjectId activity, Role role,
			ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		Activity a = getActivity(f, activity);
		Role r = getRole(f, role.getId());
		addRoleToActivity(a, r, f);
		return r;
	}

	/**
	 * Adds a SIGNAL to an ACTIVITY of a FLASHMOB
	 * @param s a signal which shall be added
	 * @param activity the id of an activity
	 * @param flashmob the id of a flashmob
	 * @return the added signal
	 */
	public Signal addSignal(Signal s, ObjectId activity, ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		Activity a = getActivity(f, activity);
		getStore().activities().save(a.setSignal(s));
		return s;
	}

	/**
	 * Adds a TASK to an ACTIVITY, the task is mapped to the associated role
	 * @param t a task which shall be added
	 * @param role the id of a role which is associated to the task
	 * @param activity the id of the activity which shall receive the task
	 * @param flashmob the id of a flashmob which contains the activity/role
	 * @return the added task
	 */
	public Task addTask(Task t, ObjectId role, ObjectId activity,
			ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		return addTask(t, getRole(f, role), getActivity(f, activity));
	}

	/**
	/**
	 * Adds a TASK to an ACTIVITY, the task is mapped to the associated role
	 * @param t a task which shall be added
	 * @param role a role which is associated to the task
	 * @param activity an activity which shall receive the task
	 * @return the added task
	 */
	public Task addTask(Task t, Role role, Activity activity) {
		getStore().activities().save(activity.addTask(role, t));
		return t;
	}

	/**
	 * Adds a TRIGGER to a FLASHMOB
	 * @param t a trigger
	 * @param flashmob a flashmob
	 * @return the added trigger
	 */
	public Trigger addTrigger(Trigger t, Flashmob flashmob) {
		getStore().flashmobs().save(flashmob.addTrigger(t));
		return t;
	}

	/**
	 * Adds a TRIGGER to a FLASHMOB
	 * @param t a trigger
	 * @param flashmob the id of a flashmob
	 * @return the added trigger
	 */
	public Trigger addTrigger(Trigger t, ObjectId flashmob) {
		return addTrigger(t, getFlashmob(flashmob));
	}

	/**
	 * Saves the FLASHMOB in the datastore
	 * @param f a flasmob
	 * @return the saved flashmob
	 */
	public Flashmob createFlashmob(Flashmob f) {
		return getStore().flashmobs().save(f);
	}

	/**
	 * Saves the USER in the datastore
	 * @param u a user
	 * @return the saved user
	 */
	public User createUser(User u) {
		User u2 = getUser(u.getUsername());
		if (u2 != null) {
			throw ServiceError.badRequest("username already used");
		}
		return getStore().users().save(u);
	}

	/**
	 * Deletes an ACTIVITY from the datastore
	 * @param flashmob a flashmob
	 * @param activity an activity
	 */
	public void deleteActivity(Flashmob flashmob, Activity activity) {
		getStore().activities().delete(activity);
	}

	/**
	 * Deletes an ACTIVITY from the datastore
	 * @param flashmob a flashmob
	 * @param activity the id of an activity
	 */
	public void deleteActivity(Flashmob flashmob, ObjectId activity) {
		deleteActivity(flashmob, getActivity(flashmob, activity));
	}

	/**
	 * Deletes an ACTIVITY from the datastore
	 * @param flashmob the id of a flashmob
	 * @param activity the id of an activity
	 */
	public void deleteActivity(ObjectId flashmob, ObjectId activity) {
		deleteActivity(getFlashmob(flashmob), activity);
	}

	/**
	 * Deletes a FLASHMOB from the datastore
	 * @param flashmob a flashmob
	 */
	public void deleteFlashmob(Flashmob flashmob) {
		getStore().flashmobs().delete(flashmob);
	}

	/**
	 * Deletes a FLASHMOB from the datastors
	 * @param flashmob the id of a flashmob
	 */
	public void deleteFlashmob(ObjectId flashmob) {
		deleteFlashmob(getFlashmob(flashmob));
	}

	/**
	 * Deletes a TRIGGER from a FLASHMOB
	 * @param flashmob a flashmob
	 * @param trigger the id of a trigger
	 */
	public void deleteTrigger(Flashmob flashmob, ObjectId trigger) {
		deleteTrigger(flashmob, getTrigger(flashmob, trigger));
	}

	/**
	 * Deletes a TRIGGER from a FLASHMOB
	 * @param flashmob a flashmob
	 * @param trigger a trigger
	 */
	public void deleteTrigger(Flashmob flashmob, Trigger trigger) {
		getStore().triggers().delete(trigger);
	}

	/**
	 * Deletes a TRIGGER from a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @param trigger the id of a trigger
	 */
	public void deleteTrigger(ObjectId flashmob, ObjectId trigger) {
		deleteTrigger(getFlashmob(flashmob), trigger);
	}

	/**
	 * Deletes a USER from the datastore
	 * @param user the username
	 */
	public void deleteUser(String user) {
		deleteUser(getUser(user));
	}

	/**
	 * Deletes a USER from the datastore
	 * @param user a user
	 */
	public void deleteUser(User user) {
		getStore().users().delete(user);
	}

	/**
	 * List all ACTIVITIES of a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @return a list of activities
	 */
	public List<Activity> getActivities(ObjectId flashmob) {
		return getFlashmob(flashmob).getActivities();
	}

	/**
	 * List all ACTIVITIES associated to a ROLE of a FLASHMOB
	 * @param role the id of a role
	 * @param flashmob the id of a flashmonb
	 * @return a list of activities
	 */
	public List<Activity> getActivitiesForRole(ObjectId role, ObjectId flashmob) {
		return Utils.asList(getRole(getFlashmob(flashmob), role).getActivities());
	}

	/**
	 * List all ACTIVITIES for a USER participating in a FLASHMOB
	 * @param user a username
	 * @param flashmob the id of a flashmob
	 * @return a list of activites
	 */
	public List<Activity> getActivitiesForUser(String user, ObjectId flashmob) {
		return getStore().activities()
				.get(getFlashmob(flashmob), getUser(user));
	}

	/**
	 * Return a certain ACTIVITY of a FLASHMOB
	 * @param flashmob a flashmob
	 * @param activity the id of an activity
	 * @return an activity
	 */
	public Activity getActivity(Flashmob flashmob, ObjectId activity) {
		Activity a = getStore().activities().get(activity);
		if (!flashmob.getActivities().contains(a)
				|| !a.getFlashmob().equals(flashmob)) {
			throw ServiceError.activityNotFound();
		}
		return a;
	}

	/**
	 * Return a certain ACTIVITY of a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @param activity the id of an activity
	 * @return an activity
	 */
	public Activity getActivity(ObjectId flashmob, ObjectId activity) {
		return getActivity(getFlashmob(flashmob), activity);
	}

	/**
	 * List COMMENTS of a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @return a list of comments
	 */
	public List<Comment> getCommentsForFlashmob(ObjectId flashmob) {
		return getStore().comments().get(getFlashmob(flashmob));
	}

	/**
	 * Return a FLASHMOB with a certin id
	 * @param flashmob the id of a flashmob
	 * @return a flashmob
	 */
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

	/**
	 * Return a FLASHMOB of a USER
	 * @param user a username
	 * @param flashmob the id of a flashmob
	 * @return a flashmob
	 */
	public Flashmob getFlashmob(String user, ObjectId flashmob) {
		return getFlashmob(getUser(user), flashmob);
	}

	/**
	 * Return a FLASHMOB of a USER
	 * @param user a user
	 * @param flashmob the id of a flashmob
	 * @return a flashmob
	 */
	public Flashmob getFlashmob(User user, ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		if (!f.hasUser(user)) {
			throw ServiceError.flashmobNotFound();
		}
		return f;
	}

	/**
	 * List Flashmobs
	 * @param limit the size of the list which shall be returned
	 * @param near a point in the neighbourhood of a flashmob
	 * @param user name of a certain user
	 * @param bbox a boundingbox containing the flashmobs
	 * @param from flashmob shall not happen earlier than this datetime
	 * @param to flashmob shall not happen later than this datetim
	 * @param sorting by which attribute how shall the list be sorted?
	 * @param descending if true, the list will be sorted descending
	 * @param show the showstatus
	 * @param search a searchstring
	 * @param participant username of a certain participant
	 * @param minParticipants number of minimal participants
	 * @param maxParticipants number of maximal participants
	 * @return a list of flashmobs
	 */
	//TODO javadoc: search, show
	public List<Flashmob> getFlashmobs(int limit, Point near, String user,
			BoundingBox bbox, DateTime from, DateTime to, Sorting sorting,
			boolean descending, ShowStatus show, String search,
			String participant, int minParticipants, int maxParticipants) {
		return getStore().flashmobs().get(limit, near, getUser(user), bbox,
				from, to, sorting, descending, show, search,
				getUser(participant), minParticipants, maxParticipants);
	}

	/**
	 * List FLASHMOBS of a USER
	 * @param user a username
	 * @return a list of flashmobs
	 */
	public List<Flashmob> getFlashmobsFromUser(String user) {
		return getStore().flashmobs().get(getUser(user));
	}

	/**
	 * Return a ROLE of a FLASHMOB
	 * @param f a flashmob
	 * @param role the id of a role
	 * @return a role
	 */
	public Role getRole(Flashmob f, ObjectId role) {
		Role r = getStore().roles().get(role);
		if (!r.getFlashmob().equals(f) || !f.getRoles().contains(r)) {
			throw ServiceError.roleNotFound();
		}
		return r;
	}

	/**
	 * return a ROLE of a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @param role the id of a role
	 * @return a role
	 */
	public Role getRole(ObjectId flashmob, ObjectId role) {
		Flashmob f = getFlashmob(flashmob);
		return getRole(f, role);
	}

	/**
	 * List ROLES of a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @return a list of roles
	 */
	public List<Role> getRoles(ObjectId flashmob) {
		return getFlashmob(flashmob).getRoles();
	}

	/**
	 * List ROLES of an ACTIVITY in a FLASHMOB
	 * @param activity the id of an activity
	 * @param flashmob the id of a flashmob
	 * @return a list of roles
	 */
	public List<Role> getRoles(ObjectId activity, ObjectId flashmob) {
		return getActivity(getFlashmob(flashmob), activity).getRoles();
	}

	/**
	 * Return a SIGNAL of an ACTIVITY in a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @param activity the id of an activity
	 * @return a signal
	 */
	public Signal getSignal(ObjectId flashmob, ObjectId activity) {
		return getStore().activities().getSignalOfActivity(
				getActivity(getFlashmob(flashmob), activity));
	}

	/**
	 * Return the local datastore
	 * @return the datastor
	 */
	public Store getStore() {
		return this.store;
	}

	/**
	 * Return a TASK of an ACTIVITY / ROLE in a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @param role the id of a role
	 * @param activity the id og an activity
	 * @return the TASK
	 */
	public Task getTask(ObjectId flashmob, ObjectId role, ObjectId activity) {
		Flashmob f = getFlashmob(flashmob);
		Task t = getActivity(f, activity).getTask(getRole(f, role));
		if (t == null) {
			throw ServiceError.taskNotFound();
		}
		return t;
	}

	/**
	 * Return the TASK of a USER in an ACTIVITY of a FLASHMOB
	 * @param activity the id of an activity
	 * @param flashmob th id of a flashmob
	 * @param user a username
	 * @return a task
	 */
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

	/**
	 * Return a TRIGGER of a FLASHMOB
	 * @param flashmob a flashmob
	 * @param trigger the id of a trigger
	 * @return a trigger
	 */
	public Trigger getTrigger(Flashmob flashmob, ObjectId trigger) {
		Trigger t = getStore().triggers().get(trigger);
		if (!flashmob.getTriggers().contains(t)
				|| !t.getFlashmob().equals(flashmob)) {
			throw ServiceError.triggerNotFound();
		}
		return t;
	}

	/**
	 * Return a TRIGGER of a FLASHMOB
	 * @param trigger the id of a trigger
	 * @param flashmob the id of a flashmob
	 * @return a trigger
	 */
	public Trigger getTrigger(ObjectId trigger, ObjectId flashmob) {
		return getTrigger(getFlashmob(flashmob), trigger);
	}

	/**
	 * Return a TRIGGER of an ACTIVITY in a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @param activity the id of an activity
	 * @return a trigger
	 */
	public Trigger getTriggerOfActivity(ObjectId flashmob, ObjectId activity) {
		return getActivity(getFlashmob(flashmob), activity).getTrigger();
	}

	/**
	 * List TRIGGERS of a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @return a list of triggers
	 */
	public List<Trigger> getTriggers(ObjectId flashmob) {
		return getStore().triggers().get(getFlashmob(flashmob));
	}

	/**
	 * Return a USER with a certain username
	 * @param user the username
	 * @return a user
	 */
	public User getUser(String user) {
		if (user == null) {
			return null;
		}
		return getStore().users().get(user);
	}

	/**
	 * retunr a list of USERs
	 * @param limit length of the lis
	 * @return list of users
	 */
	public List<User> getUsers(int limit) {
		return getStore().users().get(limit);
	}

	/**
	 * List USERS participating in a ROLE of a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @param role the id of a role
	 * @param limit lenght-limit of the list
	 * @return a list of users
	 */
	public List<User> getUsersForRole(ObjectId flashmob, ObjectId role,
			int limit) {
		Role r = getRole(flashmob, role);
		return Utils.sublist(r.getUsers(), 0, limit + 1);
	}

	/**
	 * List USERS participating in a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @return a list of users
	 */
	public List<User> getUsersOfFlashmob(ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		Set<User> users = Utils.set();
		for (Role r : f.getRoles()) {
			users.addAll(r.getUsers());
		}
		return Utils.asList(users);
	}

	/**
	 * Register a USER as participating in a ROLE of a FLASHMOB
	 * @param u a user
	 * @param role the id of a role
	 * @param flashmob the id of a flashmob
	 * @return a user
	 */
	public User registerUser(User u, ObjectId role, ObjectId flashmob) {
		Role r = getRole(flashmob, role);
		getStore().roles().save(r.addUser(u));
		return u;
	}
	

	/**
	 * Unregister a USER from a ROLE
	 * @param user a username
	 * @param role the id of a role
	 * @param flashmob the id of a flashmob
	 */
	public void unregisterUserFromRole(String user, ObjectId role, ObjectId flashmob){
	    Role r = getRole(flashmob, role);
	    User u = getUser(user);
	    r.getUsers().remove(u);
	    u.getRoles().remove(r);
	    
	    getStore().users().save(u);
	    getStore().roles().save(r);
	}
	
	/**
	 * Removes a TRIGGER from an ACTIVITY in a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @param activity the id of an activity
	 */
	public void removeTriggerFromActivity(ObjectId flashmob, ObjectId activity) {
		Flashmob f = getFlashmob(flashmob);
		Activity a = getActivity(f, activity);
		a.setTrigger(null);
		getStore().activities().save(a);
	}

	/**
	 * Sets the TRIGGER of an ACTIVITY in a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @param activity the id of an activity
	 * @param t a trigger
	 * @return the trigger
	 */
	public Trigger setTriggerForActivity(ObjectId flashmob, ObjectId activity,
			Trigger t) {
		Flashmob f = getFlashmob(flashmob);
		Activity a = getActivity(f, activity);
		t = getTrigger(f, t.getId());
		a.setTrigger(t);
		getStore().activities().save(a);
		return t;
	}

	/**
	 * Updates the ACTIVITY activity of a FLASHMOB with the ACTIVITY changes 
	 * @param changes an activity containing the new data
	 * @param activity the id of the activity which has to be updated
	 * @param flashmob the id of a flashmob
	 * @return the updated activity
	 */
	public Activity updateActivity(Activity changes, ObjectId activity,
			ObjectId flashmob) {
		return getStore().activities().save(
				update(getActivity(flashmob, activity), changes));
	}

	/**
	 * Updates a FLASHMOB id with the FLASHMOB flashmob
	 * @param id the id of the flashmob which has to be updated
	 * @param flashmob the new flashmob
	 * @returnthe updated flashmob
	 */
	public Flashmob updateFlashmob(ObjectId id, Flashmob flashmob) {
		return getStore().flashmobs().save(update(getFlashmob(id), flashmob));
	}

	/**
	 * Updates a ROLES role of a FLASHMOB with the ROLE changes
	 * @param changes a role containing the new data
	 * @param role the id of the role which shall be updated
	 * @param flashmob the id of a flashmob
	 * @return the updated roles
	 */
	public Role updateRole(Role changes, ObjectId role, ObjectId flashmob) {
		return getStore().roles()
				.save(update(getRole(flashmob, role), changes));
	}

	/**
	 * Updates a SIGNAL of an ACTIVITY in a FLASHMOB
	 * @param signal the new signal
	 * @param activity the id of an activity
	 * @param flashmob the id of a flashmob
	 * @return the updated signal
	 */
	public Signal updateSignal(Signal signal, ObjectId activity,
			ObjectId flashmob) {
		Activity a = getActivity(flashmob, activity);
		Signal s = getStore().activities().getSignalOfActivity(a);
		update(s, signal);
		getStore().activities().save(a);
		return s;
	}

	/**
	 * Updates the TASK of an ACTIVITY of a FLASHMOB
	 * @param task the new task
	 * @param role the id of a role containing the task
	 * @param activity the id of an activity containing the task
	 * @param flashmob the id of a flashmob
	 * @return the updated task
	 */
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

	/**
	 * Updates a USER with a certain username
	 * @param updates a modified user
	 * @param user a username
	 * @return the updated user
	 */
	public User updateUser(User updates, String user) {
		User old = getUser(user);
		User changed = update(old, updates);
		getStore().users().save(changed);
		return changed;
	}

	/**
	 * Retunrs an ACTIVITY of a USERname in a FLASHMOB
	 * @param user a username
	 * @param flashmob the id of a flashmob
	 * @param activity the id of an activity
	 * @return an activity
	 */
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

	/**
	 * Return a COMMENT of a FLASHMON
	 * @param flashmob the id of a flashmob
	 * @param comment the id of a comment
	 * @return a comment
	 */
	public Comment getCommentForFlashmob(ObjectId flashmob, ObjectId comment) {
		Flashmob f = getFlashmob(flashmob);
		Comment c = getStore().comments().get(comment);
		if (!c.getFlashmob().equals(f)) {
			throw ServiceError.commentNotFound();
		}
		return c;
	}

	/**
	 * Updates a COMMENT of a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @param comment the id of the comment which has to be changed
	 * @param changes the new comment
	 * @return the updated comment
	 */
	public Comment updateComment(ObjectId flashmob, ObjectId comment,
			Comment changes) {
		return getStore().comments().save(
				update(getCommentForFlashmob(flashmob, comment), changes));
	}

	/**
	 * Return a ROLE of an ACTIVITY of a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @param activity the id of an activity
	 * @param role the id of a role
	 * @return a roles
	 */
	public Role getRoleForActivity(ObjectId flashmob, ObjectId activity, ObjectId role) {
		Flashmob f = getFlashmob(flashmob);
		Activity a = getActivity(f, activity);
		Role r =  getRole(f, role);
		if (!a.getRoles().contains(r) || !r.getActivities().contains(a)) {
			throw ServiceError.roleNotFound();
		}
		return r;
	}

	/**
	 * Removes a ROLE from an ACTIVITY in a FLAHMOB
	 * @param flashmob the id of a flashmob
	 * @param activity the id of an activity
	 * @param role the id of a role
	 */
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
	

	/**
	 * Retunrs an ACTIVIT of a ROLE
	 * @param flashmob the id of a flashmob
	 * @param role the id of a role
	 * @param activity the id of an activity
	 * @return an activity
	 */
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

	/**
	 * Removes a ROLE from a FLASHMOB
	 * @param flashmob the id of a flashmob
	 * @param role the id of a role
	 */
	public void removeRoleFromFlashmob(ObjectId flashmob, ObjectId role) {
	    Flashmob f = getFlashmob(flashmob);
	    Role r = getRole(f, role);
	    
	    f.getRoles().remove(r);
	    //TODO : muss hier noch der FM aus der role gelöscht werden?
	    getStore().flashmobs().save(f);
    }

    /**
     * Removes a TASK from a ROLE of an ACTIVITY
     * @param flashmob the id of a flashmob
     * @param role the id of a role
     * @param activity the id of an activity
     */
    public void removeTaskFromRole(ObjectId flashmob, ObjectId role, ObjectId activity) {
	    Flashmob f = getFlashmob(flashmob);
	    Role r = getRole(f, role);
	    Activity a = getActivity(f, activity);
	    
	   r.getActivities().remove(a);
	   a.getRoles().remove(r);
	   
	   getStore().roles().save(r);
	   getStore().activities().save(a);
    }
}
