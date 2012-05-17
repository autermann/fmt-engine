package de.ifgi.fmt;

import static de.ifgi.fmt.update.UpdateFactory.update;

import java.util.List;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.mongo.Store;
import de.ifgi.fmt.utils.Utils;

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
		return false; //TODO validity check
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

	public Activity addActivity(Activity a, ObjectId flashmob) {
		Flashmob f = getFlashmob(flashmob);
		getStore().saveFlashmob(f.addActivity(a));
		return a;
	}

	public Role addRole(ObjectId flashmob, Role r) {
		Flashmob f =getFlashmob(flashmob);
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

	public List<User> getUsersForRole(ObjectId flashmob, ObjectId role, int limit) {
		Flashmob f = getFlashmob(flashmob);
		Role r = getRole(role);
		if (!r.getFlashmob().equals(f)) {
			throw ServiceError.roleNotFound();
		}
		return Utils.sublist(r.getUsers(), 0, limit+1);
	}

    public List<Role> getRoles(ObjectId flashmob) {
	throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<Task> getTasksForRole(ObjectId role, ObjectId activity, ObjectId flashmob) {
	throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<Role> getRoles(ObjectId activity, ObjectId flashmob) {
	throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<Activity> getActivitiesForRole(ObjectId role, ObjectId flashmob) {
	throw new UnsupportedOperationException("Not yet implemented");
    }

    public Trigger getTrigger(ObjectId trigger, ObjectId flashmob) {
	throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<Trigger> getTriggers(ObjectId flashmob) {
	throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<User> getUsersFromFlashmob() {
	throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<Flashmob> getFlashmobsFromUser(ObjectId user) {
	throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<Activity> getActivitiesForUser(ObjectId user, ObjectId flashmob) {
	throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<Task> getTaskForActivity(ObjectId activity, ObjectId flashmob, ObjectId user) {
	throw new UnsupportedOperationException("Not yet implemented");
    }

}
