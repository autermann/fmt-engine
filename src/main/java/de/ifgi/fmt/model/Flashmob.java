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

import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.utils.Utils;

@Entity(Flashmob.COLLECTION_NAME)
public class Flashmob {

	public static final String ACTIVITIES = "activities";
	public static final String COLLECTION_NAME = "flashmobs";
	public static final String COORDINATOR = "coordinator";
	public static final String CREATION_TIME = "creationTime";
	public static final String DESCRIPTION = "description";
	public static final String END = "end";
	public static final String KEY = "key";
	public static final String LAST_CHANGED = "lastChanged";
	public static final String LOCATION = "location";
	public static final String PUBLIC = "isPublic";
	public static final String PUBLISH = "publish";
	public static final String ROLES = "roles";
	public static final String START = "start";
	public static final String TITLE = "title";
	public static final String TRIGGERS = "triggers";
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
	@Past
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

	public Flashmob addActivity(Activity activity) {
		getActivities().add(activity.setFlashmob(this));
		return this;
	}

	public Flashmob addRole(Role role) {
		getRoles().add(role.setFlashmob(this));
		return this;
	}

	public Flashmob addTrigger(Trigger trigger) {
		getTriggers().add(trigger.setFlashmob(this));
		return this;
	}

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

	public List<Activity> getActivities() {
		return activities;
	}

	public User getCoordinator() {
		return coordinator;
	}

	public DateTime getCreationTime() {
		return creationTime;
	}

	public String getDescription() {
		return description;
	}

	public DateTime getEnd() {
		return endTime;
	}

	public ObjectId getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	public DateTime getLastChangedTime() {
		return lastChangedTime;
	}

	public Point getLocation() {
		return location;
	}

	public DateTime getPublish() {
		return publishTime;
	}

	public int getRegisteredUsers() {
		int registered = 0;
		for (Role r : getRoles()) {
			registered += r.getUsers().size();
		}
		return registered;
	}

	public int getRequiredUsers() {
		int required = 0;
		for (Role r : getRoles()) {
			required += r.getMinCount();
		}
		return required;
	}

	public List<Role> getRoles() {
		return this.roles;
	}

	public DateTime getStart() {
		return startTime;
	}

	public String getTitle() {
		return title;
	}

	public List<Trigger> getTriggers() {
		return triggers;
	}

	public Validity getValidity() {
		return validity;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	public boolean hasUser(User u) {
		for (Role r : getRoles())
			if (r.getUsers().contains(u))
				return true;
		return false;
	}

	public boolean isNotChecked() {
		return getValidity() == Validity.NOT_CHECKED;
	}

	public boolean isNotValid() {
		return getValidity() == Validity.NOT_VALID;
	}

	public Boolean isPublic() {
		return isPublic;
	}

	public boolean isValid() {
		return getValidity() == Validity.VALID;
	}

	public Flashmob setActivities(List<Activity> activities) {
		this.activities = activities;
		return this;
	}

	public Flashmob setCoordinator(User coordinator) {
		this.coordinator = coordinator;
		return this;
	}

	public Flashmob setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	public Flashmob setDescription(String description) {
		this.description = description;
		return this;
	}

	public Flashmob setEnd(DateTime end) {
		this.endTime = end;
		return this;
	}

	public Flashmob setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public Flashmob setKey(String key) {
		this.key = key;
		return this;
	}

	public Flashmob setLastChangedTime(DateTime lastChangedTime) {
		this.lastChangedTime = lastChangedTime;
		return this;
	}

	public Flashmob setLocation(Point location) {
		this.location = location;
		return this;
	}

	public Flashmob setNotChecked() {
		setValidity(Validity.NOT_CHECKED);
		return this;
	}

	public Flashmob setNotValid() {
		setValidity(Validity.NOT_VALID);
		return this;
	}

	public Flashmob setPublic(Boolean isPublic) {
		this.isPublic = isPublic;
		return this;
	}

	public Flashmob setPublish(DateTime publish) {
		this.publishTime = publish;
		return this;
	}

	public Flashmob setRoles(List<Role> roles) {
		this.roles = roles;
		return this;
	}

	public Flashmob setStart(DateTime start) {
		this.startTime = start;
		return this;
	}

	public Flashmob setTitle(String title) {
		this.title = title;
		return this;
	}

	public Flashmob setTriggers(List<Trigger> triggers) {
		this.triggers = triggers;
		return this;
	}

	public Flashmob setValid() {
		setValidity(Validity.VALID);
		return this;
	}

	public Flashmob setValidity(Validity validity) {
		this.validity = validity;
		return this;
	}

	@Override
	public String toString() {
		return getId().toString();
	}

}
