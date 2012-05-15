package de.ifgi.fmt;

import static de.ifgi.fmt.update.UpdateFactory.update;

import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.mongo.Store;

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
		return getStore().getFlashmob(flashmob);
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
	
	public Role getRole(ObjectId role) {
		return getStore().getRole(role);
	}

	public Role getRole(ObjectId role, ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		Role r = getRole(role);
		if (!f.getRoles().contains(r))
			throw ServiceError.roleNotFound();
		return r;
	}

	public Role updateRole(Role changes, ObjectId roleID, ObjectId flashmob) {
		return getStore().saveRole(update(getRole(roleID, flashmob), changes));
	}

	public User registerUser(User u, ObjectId role, ObjectId flashmob) {
		Role r = getRole(role, flashmob);
		getStore().saveRole(r.addUser(u));
		return u;
	}
	
	public Activity getActivity(Flashmob flashmob, ObjectId activity) {
		Activity a = getStore().getActivity(activity);
		if (!flashmob.getActivities().contains(a) || !a.getFlashmob().equals(flashmob)) {
			throw ServiceError.activityNotFound();
		}
		return a;
	}
	
	public Activity getActivity(ObjectId flashmob, ObjectId activity) {
		return getActivity(getFlashmob(flashmob), activity);
	}

	public Task addTask(Task t, ObjectId role, ObjectId activity, ObjectId flashmob) {
		Role r = getRole(role, flashmob);
		Activity a = getActivity(flashmob, activity);
		getStore().saveActivity(a.addTask(r, t));
		return t;
	}

	public Task updateTask(Task t, ObjectId role, ObjectId activity,
			ObjectId flashmob) {
		// TODO Auto-generated method stub
		return null;
	}

	public Activity addActivity(Activity a, ObjectId role, ObjectId flashmob) {
		// TODO Auto-generated method stub
		return null;
	}

	public Role addRole(Role r, ObjectId activity, ObjectId flashmob) {
		// TODO Auto-generated method stub
		return null;
	}

	public Signal addSignal(Signal s, ObjectId activity, ObjectId flashmob) {
		// TODO Auto-generated method stub
		return null;
	}

	public Signal updateSignal(Signal s, ObjectId activity, ObjectId flashmob) {
		// TODO Auto-generated method stub
		return null;
	}

	public Activity updateActivity(Activity a, ObjectId activity, ObjectId flashmob) {
		// TODO Auto-generated method stub
		return null;
	}

	public Trigger addTrigger(Trigger t, ObjectId activity, ObjectId flashmob) {
		// TODO Auto-generated method stub
		return null;
	}

	public Task addActivity(Activity a, ObjectId flashmob) {
		// TODO Auto-generated method stub
		return null;
	}

	public Role addRole(ObjectId flashmob, Role r) {
		// TODO Auto-generated method stub
		return null;
	}

	public User getFlashmob(ObjectId user, ObjectId flashmob) {
		// TODO Auto-generated method stub
		return null;
	}

	public User updateUser(User u, ObjectId user) {
		// TODO Auto-generated method stub
		return null;
	}

	public User getUser(ObjectId user) {
		// TODO Auto-generated method stub
		return null;
	}

	public Response deleteUser(ObjectId user) {
		// TODO Auto-generated method stub
		return null;
	}

	public User createUser(User u) {
		// TODO Auto-generated method stub
		return null;
	}

}
