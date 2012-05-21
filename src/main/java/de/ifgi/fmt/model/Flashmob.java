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

import org.bson.types.ObjectId;
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
	public static final String COMMENTS = "comments";
	public static final String ROLES = "roles";
	public static final String TRIGGERS = "triggers";
	public static final String VALIDITY = "validity";
	public static final String COORDINATOR = "coordinator";

	@Property(Flashmob.TITLE)
	private String title;

	@Property(Flashmob.DESCRIPTION)
	private String description;

	@Property(Flashmob.START)
	private DateTime start;

	@Property(Flashmob.END)
	private DateTime end;

	@Property(Flashmob.PUBLISH)
	private DateTime publish;

	@Property(Flashmob.PUBLIC)
	private boolean isPublic;

	@Indexed(IndexDirection.GEO2D)
	@Property(Flashmob.LOCATION)
	private Point location;

	@Property(Flashmob.KEY)
	private String key;

	@Property(Flashmob.VALIDITY)
	private Validity validity = Validity.NOT_CHECKED;

	@Reference(Flashmob.ACTIVITIES)
	private List<Activity> activities = Utils.list();

	@Reference(Flashmob.ROLES)
	private List<Role> roles = Utils.list();

	@Reference(Flashmob.TRIGGERS)
	private List<Trigger> triggers = Utils.list();

	@Reference(Flashmob.COMMENTS)
	private List<Comment> comments = Utils.list();
	
	@Reference(Flashmob.COORDINATOR)
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
		this.start = start;
		return this;
	}

	public Flashmob setEnd(DateTime end) {
		this.end = end;
		return this;
	}

	public Flashmob setPublish(DateTime publish) {
		this.publish = publish;
		return this;
	}

	public Flashmob setPublic(boolean isPublic) {
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
		return start;
	}

	public DateTime getEnd() {
		return end;
	}

	public DateTime getPublish() {
		return publish;
	}

	public boolean isPublic() {
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

	public List<Comment> getComments() {
		return comments;
	}

	public Flashmob setComments(List<Comment> comments) {
		this.comments = comments;
		return this;
	}

	public Flashmob addComment(Comment comment) {
		getComments().add(comment.setFlashmob(this));
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
	
}
