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

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.utils.IndexDirection;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.mongo.Identifiable;
import de.ifgi.fmt.utils.Utils;

@Polymorphic
@Entity(Flashmob.COLLECTION_NAME)
public class Flashmob extends Identifiable {

	public static final String COLLECTION_NAME = "flashmobs";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String START = "start";
	public static final String END = "end";
	public static final String PUBLISH = "publish";
	public static final String PUBLIC = "isPublic";
	public static final String LOCATION = "location";
	public static final String KEY = "key";
	public static final String ACTIVITIES = "activities";
	public static final String ROLES = "roles";
	public static final String TRIGGERS = "triggers";
	public static final String VALIDITY = "validity";
	public static final String COORDINATOR = "coordinator";

	@NotBlank
	@SafeHtml
	@Property(Flashmob.TITLE)
	private String title;
	
	@NotBlank
	@SafeHtml
	@Property(Flashmob.DESCRIPTION)
	private String description;

	@NotNull
	@Property(Flashmob.START)
	private DateTime startTime;

	@Property(Flashmob.END)
	private DateTime endTime;

	@Property(Flashmob.PUBLISH)
	private DateTime publishTime;

	@Property(Flashmob.PUBLIC)
	private Boolean isPublic;
	
	@NotNull
	@Indexed(IndexDirection.GEO2D)
	@Property(Flashmob.LOCATION)
	private Point location;

	@Property(Flashmob.KEY)
	private String key;

	@Property(Flashmob.VALIDITY)
	private Validity validity = Validity.NOT_CHECKED;

	@NotNull
	@Reference(value = Flashmob.ACTIVITIES, lazy = true)
	private List<Activity> activities = Utils.list();

	@NotNull
	@Reference(value = Flashmob.ROLES, lazy = true)
	private List<Role> roles = Utils.list();

	@NotNull
	@Reference(value = Flashmob.TRIGGERS, lazy = true)
	private List<Trigger> triggers = Utils.list();

	@NotNull
	@Reference(value = Flashmob.COORDINATOR, lazy = true)
	private User coordinator;

	public Flashmob(ObjectId id) {
		super(id);
	}

	public Flashmob(String id) {
		super(id);
	}

	public Flashmob() {
		super();
	}

	public String getDescription() {
		return description;
	}

	public Flashmob setTitle(String title) {
		this.title = title;
		return this;
	}

	public Flashmob setStart(DateTime start) {
		this.startTime = start;
		return this;
	}

	public Flashmob setEnd(DateTime end) {
		this.endTime = end;
		return this;
	}

	public Flashmob setPublish(DateTime publish) {
		this.publishTime = publish;
		return this;
	}

	public Flashmob setPublic(Boolean isPublic) {
		this.isPublic = isPublic;
		return this;
	}

	public Flashmob setLocation(Point location) {
		this.location = location;
		return this;
	}

	public Flashmob setKey(String key) {
		this.key = key;
		return this;
	}

	public Flashmob setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public DateTime getStart() {
		return startTime;
	}

	public DateTime getEnd() {
		return endTime;
	}

	public DateTime getPublish() {
		return publishTime;
	}

	public Boolean isPublic() {
		return isPublic;
	}

	public Point getLocation() {
		return location;
	}

	public String getKey() {
		return key;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public Flashmob setActivities(List<Activity> activities) {
		this.activities = activities;
		return this;
	}

	public Flashmob addActivity(Activity activity) {
		getActivities().add(activity.setFlashmob(this));
		return this;
	}

	public List<Role> getRoles() {
		return this.roles;
	}

	public Flashmob setRoles(List<Role> roles) {
		this.roles = roles;
		return this;
	}

	public Flashmob addRole(Role role) {
		getRoles().add(role.setFlashmob(this));
		return this;
	}

	public List<Trigger> getTriggers() {
		return triggers;
	}

	public Flashmob setTriggers(List<Trigger> triggers) {
		this.triggers = triggers;
		return this;
	}

	public Flashmob addTrigger(Trigger trigger) {
		getTriggers().add(trigger.setFlashmob(this));
		return this;
	}

	public Validity getValidity() {
		return validity;
	}

	public Flashmob setValidity(Validity validity) {
		this.validity = validity;
		return this;
	}

	public Flashmob setNotChecked() {
		setValidity(Validity.NOT_CHECKED);
		return this;
	}

	public Flashmob setValid() {
		setValidity(Validity.VALID);
		return this;
	}

	public Flashmob setNotValid() {
		setValidity(Validity.NOT_VALID);
		return this;
	}

	public boolean isNotChecked() {
		return getValidity() == Validity.NOT_CHECKED;
	}

	public boolean isValid() {
		return getValidity() == Validity.VALID;
	}

	public boolean isNotValid() {
		return getValidity() == Validity.NOT_VALID;
	}
	
	public boolean hasUser(User u) {
		for (Role r : getRoles())
			if (r.getUsers().contains(u)) 
				return true;
		return false;
	}

	public User getCoordinator() {
		return coordinator;
	}

	public Flashmob setCoordinator(User coordinator) {
		this.coordinator = coordinator;
		return this;
	}

	public int getRequiredUsers() {
		int required = 0;
		for (Role r : getRoles()) {
			required += r.getMinCount();
		}
		return required;
	}

	public int getRegisteredUsers() {
		int registered = 0;
		for (Role r : getRoles()) {
			registered += r.getUsers().size();
		}
		return registered;
	}
	
}
