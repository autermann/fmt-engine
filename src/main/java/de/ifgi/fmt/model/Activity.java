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

/**
 * This Class represents an Activity
 * @author Autermann, Demuth, Radtke
 */
@Polymorphic
@Entity(Activity.COLLECTION_NAME)
public class Activity {

    /**
     * Definiton
     */
    public static final String COLLECTION_NAME = "activities";
    /**
     * Definition
     */
    public static final String CREATION_TIME = "creationTime";
    /**
     * Definition
     */
    public static final String DESCRIPTION = "description";
    /**
     * Definition
     */
    public static final String FLASHMOB = "flashmob";
    /**
     * Definition
     */
    public static final String LAST_CHANGED = "lastChanged";
    /**
     * Definition
     */
    public static final String SIGNAL = "signal";
    /**
     * Definition
     */
    public static final String TASKS = "savedTasks";
    /**
     * Definition
     */
    public static final String TITLE = "title";
    /**
     * Definition
     */
    public static final String TRIGGER = "trigger";
    @NotNull
    @Past
    @Indexed
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
    @NotNull
    @Id
    private ObjectId id = new ObjectId();
    @NotNull
    @Indexed
    @Property(Activity.LAST_CHANGED)
    private DateTime lastChangedTime = new DateTime();
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

    /**
     * Add a ROLE to this ACTIVITY
     * @param role a role
     * @return this activity
     */
    public Activity addRole(Role role) {
	this.tasks.put(role.addActivity(this), null);
	return this;
    }

    /**
     * Add a TASK to a ROLE of this ACTIVITY
     * @param role a role
     * @param task a task
     * @return this activity
     */
    public Activity addTask(Role role, Task task) {
	this.tasks.put(role.addActivity(this),
		task.setActivity(this).setRole(role));
	return this;
    }

    /**
     * Set the laszChangedTime of this ACTIVITY
     */
    @PrePersist
    public void changed() {
	setLastChangedTime(new DateTime());
    }

    @Override
    public boolean equals(Object o) {
	if (o instanceof Activity) {
	    return getId().equals(((Activity) o).getId());
	}
	return false;
    }

    /**
     * Return the creationtime of this activity
     * @return cration time
     */
    public DateTime getCreationTime() {
	return creationTime;
    }

    /**
     * Return the description of this activity
     * @return descriotion
     */
    public String getDescription() {
	return description;
    }

    /**
     * Return the Flashmob which is associated to this activity
     * @return flashmob
     */
    public Flashmob getFlashmob() {
	return flashmob;
    }

    /**
     * Return the ObjectID of this activity
     * @return id
     */
    public ObjectId getId() {
	return id;
    }

    /**
     * Retrun the the latest time when this activity was changed
     * @return lastchangedtime
     */
    public DateTime getLastChangedTime() {
	return lastChangedTime;
    }

    /**
     * Return a list of roles associated to this activity
     * @return List of Roles
     */
    public List<Role> getRoles() {
	return Utils.asList(this.tasks.keySet());
    }

    /**
     * Return a Signal which is associated to this activity
     * @return signal
     */
    public Signal getSignal() {
	return signal;
    }

    /**
     * Return a Task associated to a ROLE of this activity
     * @param r a role
     * @return a task
     */
    public Task getTask(Role r) {
	return getTasks().get(r);
    }

    /**
     * Key-Value map of Role, Task which returns a task
     * @return task
     */
    public Map<Role, Task> getTasks() {
	return tasks;
    }

    /**
     * Retunr the title of this activity
     * @return title
     */
    public String getTitle() {
	return title;
    }

    /**
     * Return the trigger of this activity
     * @return trigger
     */
    public Trigger getTrigger() {
	return trigger;
    }

    @Override
    public int hashCode() {
	return getId().hashCode();
    }
    
    //TODO Javadoc
    /**
     * Iterate all tasks to .... ?
     */
    @PostLoad
    public void postLoad() {
	for (TaskForRole p : savedTasks) {
	    tasks.put(p.getRole(), p.getTask());
	}
    }

    //TODO Javadoc
    /**
     * Iterate all Role,Task mapping in the entryset of tasks to .... ?
     */
    @PrePersist
    public void prePersist() {
	for (Entry<Role, Task> e : tasks.entrySet()) {
	    savedTasks.add(new TaskForRole(e.getKey(), e.getValue()));
	}
    }

    /**
     * Remove a ROLE from this ACTIVITY
     * @param r a role
     * @return this activity
     */
    public Activity removeRole(Role r) {
	this.tasks.remove(r.removeActivity(this));
	return this;
    }

    /**
     * Remove a TASK from a ROLE of this activity
     * @param role the role whose task shall be removed
     * @return this activity
     */
    public Activity removeTask(Role role) {
	Task t = this.tasks.put(role, null);
	if (t != null) {
	    t.setActivity(null);
	}
	return this;
    }

    /**
     * Remove a TASK from this ACTIVITY
     * @param t a task
     * @return this activity
     */
    public Activity removeTask(Task t) {
	if (!t.getActivity().equals(this)) {
	    throw new IllegalArgumentException(
		    "Task is not part of this activity.");
	}
	return removeTask(t.getRole());
    }

    /**
     * Set the crationtime of this activity
     * @param creationTime a datetime
     * @return this activity
     */
    public Activity setCreationTime(DateTime creationTime) {
	this.creationTime = creationTime;
	return this;
    }

    /**
     * Set the description of this activity
     * @param description a description
     * @return this activity
     */
    public Activity setDescription(String description) {
	this.description = description;
	return this;
    }

    /**
     * Associate this Activity with a Flashmob
     * @param flashmob a Flashmob
     * @return this Activity
     */
    public Activity setFlashmob(Flashmob flashmob) {
	this.flashmob = flashmob;
	return this;
    }

    /**
     * Set the ID of this activity
     * @param id an ObjectID
     * @return this activity
     */
    public Activity setId(ObjectId id) {
	this.id = id;
	return this;
    }

    /**
     * Set the time when this Activity was changed last
     * @param lastChangedTime datetime
     * @return this activity
     */
    public Activity setLastChangedTime(DateTime lastChangedTime) {
	this.lastChangedTime = lastChangedTime;
	return this;
    }

    /**
     * Set the signal of this activity
     * @param signal a signal
     * @return this activity
     */
    public Activity setSignal(Signal signal) {
	this.signal = signal;
	return this;
    }

    /**
     * Assosicate tasks with this activity
     * @param tasks a Role,Tasks Mapping
     */
    public void setTasks(Map<Role, Task> tasks) {
	this.tasks = tasks;
    }

    /**
     * Set the title of this activity
     * @param title a string
     * @return this acitivity
     */
    public Activity setTitle(String title) {
	this.title = title;
	return this;
    }

    /**
     * Set the trigger of this activity
     * @param trigger a trigger
     * @return this activity
     */
    public Activity setTrigger(Trigger trigger) {
	this.trigger = trigger;
	return this;
    }

    @Override
    public String toString() {
	return getId().toString();
    }
}
