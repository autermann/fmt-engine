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
package de.ifgi.fmt.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Transient;

import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.utils.Utils;

@Polymorphic
@Entity(Activity.COLLECTION_NAME)
public class Activity {
	public static final String COLLECTION_NAME = "activities";
	public static final String CREATION_TIME = "creationTime";
	public static final String DESCRIPTION = "description";
	public static final String FLASHMOB = "flashmob";
	public static final String SIGNAL = "signal";
	public static final String TASKS = "savedTasks";
	public static final String TITLE = "title";
	public static final String TRIGGER = "trigger";

	@NotNull@Past@Indexed
	@Property(Activity.CREATION_TIME)
	private DateTime creationTime = new DateTime();

	@NotBlank
	@SafeHtml
	@Property(Activity.DESCRIPTION)
	private String description;

	@Indexed
	@NotNull
	@Reference(value = Activity.FLASHMOB, lazy = true)
	private Flashmob flashmob;

	@NotNull@Id
	private ObjectId id = new ObjectId();

	@Embedded
	private List<TaskForRole> savedTasks = Utils.list();

	@Reference(value = Activity.SIGNAL, lazy = true)
	private Signal signal;

	@Transient
	private Map<Role, Task> tasks = Utils.map();

	@NotBlank
	@SafeHtml
	@Property(Activity.TITLE)
	private String title;

	@Reference(value = Activity.TRIGGER, lazy = true)
	private Trigger trigger;

	public Activity addRole(Role role) {
		this.tasks.put(role.addAcitivity(this), null);
		return this;
	}
	
	public Activity addTask(Role role, Task task) {
		this.tasks.put(role, task.setActivity(this).setRole(role));
		return this;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Activity) {
			return getId().equals(((Activity) o).getId());
		}
		return false;
	}

	public DateTime getCreationTime() {
		return creationTime;
	}

	public String getDescription() {
		return description;
	}

	public Flashmob getFlashmob() {
		return flashmob;
	}

	public ObjectId getId() {
		return id;
	}

	public List<Role> getRoles() {
		return Utils.asList(this.tasks.keySet());
	}

	public Signal getSignal() {
		return signal;
	}

	public Task getTask(Role r) {
		return getTasks().get(r);
	}

	public Map<Role, Task> getTasks() {
		return tasks;
	}

	public String getTitle() {
		return title;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@PostLoad
	public void postLoad() {
		for (TaskForRole p : savedTasks) {
			tasks.put(p.getRole(), p.getTask());
		}
	}

	@PrePersist
	public void prePersist() {
		for (Entry<Role, Task> e : tasks.entrySet()) {
			savedTasks.add(new TaskForRole(e.getKey(), e.getValue()));
		}
	}

	public Activity removeRole(Role r) {
		this.tasks.remove(r.removeActivity(this));
		return this;
	}

	public Activity removeTask(Role role) {
		Task t = this.tasks.put(role, null);
		if (t != null) {
			t.setActivity(null);
		}
		return this;
	}

	public Activity removeTask(Task t) {
		if (!t.getActivity().equals(this)) {
			throw new IllegalArgumentException(
					"Task is not part of this activity.");
		}
		return removeTask(t.getRole());
	}

	public Activity setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	public Activity setDescription(String description) {
		this.description = description;
		return this;
	}

	public Activity setFlashmob(Flashmob flashmob) {
		this.flashmob = flashmob;
		return this;
	}

	public Activity setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public Activity setSignal(Signal signal) {
		this.signal = signal;
		return this;
	}

	public void setTasks(Map<Role, Task> tasks) {
		this.tasks = tasks;
	}

	public Activity setTitle(String title) {
		this.title = title;
		return this;
	}

	public Activity setTrigger(Trigger trigger) {
		this.trigger = trigger;
		return this;
	}

	@Override
	public String toString() {
		return getId().toString();
	}
}
