package de.ifgi.fmt.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Transient;

import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.mongo.Identifiable;
import de.ifgi.fmt.utils.Utils;

@Polymorphic
@Entity(Activity.COLLECTION_NAME)
public class Activity extends Identifiable {

	public static final String COLLECTION_NAME = "activities";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String FLASHMOB = "flashmob";
	public static final String ROLES = "roles";
	public static final String TRIGGER = "trigger";
	public static final String SIGNAL = "signal";

	@Property(Activity.TITLE)
	private String title;

	@Property(Activity.DESCRIPTION)
	private String description;

	@Reference(Activity.FLASHMOB)
	private Flashmob flashmob;

	@Reference(Activity.TRIGGER)
	private Trigger trigger;

	@Reference(Activity.SIGNAL)
	private Signal signal;

	@Transient
	private Map<Role, Task> tasks = Utils.map();

	private List<TaskForRole> savedTasks = Utils.list();
	
	@PostLoad
	public void postLoad() {
		for (TaskForRole p : savedTasks) {
			tasks.put(p.getRole(), p.getTask());
		}
	}

	@PrePersist
	public void presave() {
		for (Entry<Role, Task> e : tasks.entrySet()) {
			savedTasks.add(new TaskForRole(e.getKey(), e.getValue()));
		}
	}

	public String getTitle() {
		return title;
	}

	public Activity setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Activity setDescription(String description) {
		this.description = description;
		return this;
	}

	public Flashmob getFlashmob() {
		return flashmob;
	}

	public Activity setFlashmob(Flashmob flashmob) {
		this.flashmob = flashmob;
		return this;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public Activity setTrigger(Trigger trigger) {
		this.trigger = trigger;
		return this;
	}

	public Signal getSignal() {
		return signal;
	}

	public Activity setSignal(Signal signal) {
		this.signal = signal;
		return this;
	}

	public Map<Role, Task> getTasks() {
		return tasks;
	}
	
	public Task getTask(Role r) {
		return getTasks().get(r);
	}

	public void setTasks(Map<Role, Task> tasks) {
		this.tasks = tasks;
	}

	public Activity addTask(Role role, Task task) {
		this.tasks.put(role, task.setActivity(this).setRole(role));
		return this;
	}

	public Activity addRole(Role role) {
		this.tasks.put(role.addAcitivity(this), null);
		return this;
	}
	
	public Activity removeRole(Role r) {
		this.tasks.remove(r.removeActivity(this));
		return this;
	}

	public List<Role> getRoles() {
		return Utils.asList(this.tasks.keySet());
	}

	public static class TaskForRole {
		
		@Reference
		private Role role;
		
		@Property
		private Task task;
		
		public TaskForRole(Role role, Task task) {
			this.role = role;
			this.task = task;
		}
		
		public TaskForRole() {
			
		}
		
		public Task getTask() {
			return task;
		}
		
		public void setTask(Task task) {
			this.task = task;
		}
		
		public Role getRole() {
			return role;
		}
		
		public void setRole(Role role) {
			this.role = role;
		}
	}
}
