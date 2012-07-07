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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.utils.IndexDirection;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.utils.Utils;

/**
 * This class represents a Flashmob
 * @author Autermann, Demuth, Radtke
 */
@Entity(Flashmob.COLLECTION_NAME)
public class Flashmob {

    /**
     * Definition
     */
    public static final String ACTIVITIES = "activities";
    /**
     * Definition
     */
    public static final String COLLECTION_NAME = "flashmobs";
    /**
     * Definition
     */
    public static final String COORDINATOR = "coordinator";
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
    public static final String END = "end";
    /**
     * Definition
     */
    public static final String KEY = "key";
    /**
     * Definition
     */
    public static final String LAST_CHANGED = "lastChanged";
    /**
     * Definition
     */
    public static final String LOCATION = "location";
    /**
     * Definition
     */
    public static final String PUBLIC = "isPublic";
    /**
     * Definition
     */
    public static final String PUBLISH = "publish";
    /**
     * Definition
     */
    public static final String ROLES = "roles";
    /**
     * Definition
     */
    public static final String START = "start";
    /**
     * Definition
     */
    public static final String TITLE = "title";
    /**
     * Definition
     */
    public static final String TRIGGERS = "triggers";
    /**
     * Definition
     */
    public static final String VALIDITY = "validity";
    @NotNull
    @Reference(value = Flashmob.ACTIVITIES, lazy = true)
    private List<Activity> activities = Utils.list();
    @NotNull
    @Reference(value = Flashmob.COORDINATOR, lazy = true)
    private User coordinator;
    @NotNull
    @Past
    @Indexed
    @Property(Flashmob.CREATION_TIME)
    private DateTime creationTime = new DateTime();
    @NotBlank
    @SafeHtml
    @Property(Flashmob.DESCRIPTION)
    private String description;
    @Property(Flashmob.END)
    private DateTime endTime;
    @NotNull
    @Id
    private ObjectId id = new ObjectId();
    @Property(Flashmob.PUBLIC)
    private Boolean isPublic;
    @Property(Flashmob.KEY)
    private String key;
    @NotNull
    @Indexed
    @Property(Flashmob.LAST_CHANGED)
    private DateTime lastChangedTime = new DateTime();
    @NotNull
    @Indexed(IndexDirection.GEO2D)
    @Property(Flashmob.LOCATION)
    private Point location;
    @Property(Flashmob.PUBLISH)
    private DateTime publishTime;
    @NotNull
    @Reference(value = Flashmob.ROLES, lazy = true)
    private List<Role> roles = Utils.list();
    @NotNull
    @Property(Flashmob.START)
    private DateTime startTime;
    @NotBlank
    @SafeHtml
    @Property(Flashmob.TITLE)
    private String title;
    @NotNull
    @Reference(value = Flashmob.TRIGGERS, lazy = true)
    private List<Trigger> triggers = Utils.list();
    @Property(Flashmob.VALIDITY)
    private Validity validity = Validity.NOT_CHECKED;

    /**
     * Adds an Activity to a Flashmob
     * @param activity an Activity
     * @return this flashmob
     */
    public Flashmob addActivity(Activity activity) {
	getActivities().add(activity.setFlashmob(this));
	return this;
    }

    /**
     * Adds a Role to a Flashmob
     * @param role a Role
     * @return this flashmob
     */
    public Flashmob addRole(Role role) {
	getRoles().add(role.setFlashmob(this));
	return this;
    }

    /**
     * Adds a Trigger to a Flashmob
     * @param trigger a Trigger
     * @return this Flashmob
     */
    public Flashmob addTrigger(Trigger trigger) {
	getTriggers().add(trigger.setFlashmob(this));
	return this;
    }

    /**
     * Set the time of the last change of this Flashmob to the actual time 
     */
    @PrePersist
    public void changed() {
	setLastChangedTime(new DateTime());
    }

    @Override
    public boolean equals(Object o) {
	if (o instanceof Flashmob) {
	    return getId().equals(((Flashmob) o).getId());
	}
	return false;
    }

    /**
     * List activities associated to this flashmob
     * @return a list of activites
     */
    public List<Activity> getActivities() {
	return activities;
    }

    /**
     * Return the coordinator of this flashmob
     * @return a User
     */
    public User getCoordinator() {
	return coordinator;
    }

    /**
     * Return the creation time of a flasmob
     * @return a datetime
     */
    public DateTime getCreationTime() {
	return creationTime;
    }

    /**
     * return the description of a flashmob
     * @return a string
     */
    public String getDescription() {
	return description;
    }

    /**
     * Retunr the End-Time of the flashmob
     * @return datetime object
     */
    public DateTime getEnd() {
	return endTime;
    }

    /**
     * Retunrn the id of a flashmob
     * @return an object id
     */
    public ObjectId getId() {
	return id;
    }

    /**
     * return the enrollment key of the flashmob
     * @return a key
     */
    public String getKey() {
	return key;
    }

    /**
     * return the time when the flashmob was changed last
     * @return a datetime
     */
    public DateTime getLastChangedTime() {
	return lastChangedTime;
    }

    /**
     * return the location of a flashmob
     * @return a point pbject
     */
    public Point getLocation() {
	return location;
    }

    /**
     * return the publication time of a flashmob
     * @return a datetime
     */
    public DateTime getPublish() {
	return publishTime;
    }

    /**
     * return the amount of registered users
     * @return an int
     */
    public int getRegisteredUsers() {
	int registered = 0;
	for (Role r : getRoles()) {
	    registered += r.getUsers().size();
	}
	return registered;
    }

    /**
     * return the amount of users required to perform the flashmob
     * @return an int
     */
    public int getRequiredUsers() {
	int required = 0;
	for (Role r : getRoles()) {
	    required += r.getMinCount();
	}
	return required;
    }

    /**
     * return a list of roles
     * @return a list of roles
     */
    public List<Role> getRoles() {
	return this.roles;
    }

    /**
     * retunr the starttime of the flashmob
     * @return a datetime
     */
    public DateTime getStart() {
	return startTime;
    }

    /**
     * return the title of the flashmob
     * @return a string
     */
    public String getTitle() {
	return title;
    }

    /**
     * return a list of triggers
     * @return trigger-list
     */
    public List<Trigger> getTriggers() {
	return triggers;
    }

    /**
     * Return if a flashmob is valid
     * @return a validity-object
     */
    public Validity getValidity() {
	return validity;
    }

    @Override
    public int hashCode() {
	return getId().hashCode();
    }

    /**
     * Return if a User U has enrolled for a flashmob
     * @param u the user
     * @return boolean, true if u is enrolled
     */
    public boolean hasUser(User u) {
	for (Role r : getRoles()) {
	    if (r.getUsers().contains(u)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Return true if the flashmob is not checked
     * @return boolean
     */
    public boolean isNotChecked() {
	return getValidity() == Validity.NOT_CHECKED;
    }

    /**
     * Return true if the flashmob is invalid
     * @return a boolean
     */
    public boolean isNotValid() {
	return getValidity() == Validity.NOT_VALID;
    }

    /**
     * return if the flashmob is public
     * @return a boolean
     */
    public Boolean isPublic() {
	return isPublic;
    }

    /**
     * return true if the flashmob is valid
     * @return a boolean
     */
    public boolean isValid() {
	return getValidity() == Validity.VALID;
    }

    /**
     * Associate a list of activities with this flashmob
     * @param activities list of activities
     * @return this flashmob
     */
    public Flashmob setActivities(List<Activity> activities) {
	this.activities = activities;
	return this;
    }

    /**
     * Set the coordinator of this flashmob
     * @param coordinator a user
     * @return this flashmob
     */
    public Flashmob setCoordinator(User coordinator) {
	this.coordinator = coordinator;
	return this;
    }

    /**
     * set the creation time of a fglashmob
     * @param creationTime a datetime
     * @return this flashmob
     */
    public Flashmob setCreationTime(DateTime creationTime) {
	this.creationTime = creationTime;
	return this;
    }

    /**
     * Set the description of this Flashmob
     * @param description a string
     * @return this flashmob
     */
    public Flashmob setDescription(String description) {
	this.description = description;
	return this;
    }

    /**
     * Set the endtime of this flashmob
     * @param end a datetime
     * @return this flashmob
     */
    public Flashmob setEnd(DateTime end) {
	this.endTime = end;
	return this;
    }

    /**
     * Set the ID of this flashmob
     * @param id an obejctid
     * @return this flashmob
     */
    public Flashmob setId(ObjectId id) {
	this.id = id;
	return this;
    }

    /**
     * set the ernollment key of this flashmob
     * @param key a string
     * @return this flashmob
     */
    public Flashmob setKey(String key) {
	this.key = key;
	return this;
    }

    /**
     * set the time when the flashmob was changed last
     * @param lastChangedTime a datetime
     * @return this flashmob
     */
    public Flashmob setLastChangedTime(DateTime lastChangedTime) {
	this.lastChangedTime = lastChangedTime;
	return this;
    }

    /**
     * set the location of the flashmob
     * @param location a point
     * @return this flashmob
     */
    public Flashmob setLocation(Point location) {
	this.location = location;
	return this;
    }

    /**
     * set the validity of a flashmob to NOT_CHECKED
     * @return this flashmob
     */
    public Flashmob setNotChecked() {
	setValidity(Validity.NOT_CHECKED);
	return this;
    }

    /**
     * set the validity of this Flashmob to NOT_VALID
     * @return this flashmob
     */
    public Flashmob setNotValid() {
	setValidity(Validity.NOT_VALID);
	return this;
    }

    /**
     * set the flashmob to public /notpublic
     * @param isPublic boolean
     * @return this flashmob
     */
    public Flashmob setPublic(Boolean isPublic) {
	this.isPublic = isPublic;
	return this;
    }

    /**
     * Set the time when the flashmob has to be published
     * @param publish a datetime
     * @return this flashmob
     */
    public Flashmob setPublish(DateTime publish) {
	this.publishTime = publish;
	return this;
    }

    /**
     * Associate a list of roles with a flasmob
     * @param roles a list of roles
     * @return this flashmob
     */
    public Flashmob setRoles(List<Role> roles) {
	this.roles = roles;
	return this;
    }

    /**
     * Set the starttime of a flashmob
     * @param start a datetime
     * @return this flashmob
     */
    public Flashmob setStart(DateTime start) {
	this.startTime = start;
	return this;
    }

    /**
     * set the title of a flashmob
     * @param title a string
     * @return this flashmob
     */
    public Flashmob setTitle(String title) {
	this.title = title;
	return this;
    }

    /**
     * Associate a lsit of triggers with this flashmob
     * @param triggers a list of triggers
     * @return this flashmob
     */
    public Flashmob setTriggers(List<Trigger> triggers) {
	this.triggers = triggers;
	return this;
    }

    /**
     * Set the validity of a flashmob to VALID
     * @return this flashmob
     */
    public Flashmob setValid() {
	setValidity(Validity.VALID);
	return this;
    }

    /**
     * set the validity to a certain value
     * @param validity a valid validity
     * @return this flashmob
     */
    public Flashmob setValidity(Validity validity) {
	this.validity = validity;
	return this;
    }

    @Override
    public String toString() {
	return getId().toString();
    }
}
