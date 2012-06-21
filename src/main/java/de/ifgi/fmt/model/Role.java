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

import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.utils.Utils;

@Entity(Role.COLLECTION_NAME)
public class Role {
	public enum Category {
		EASY, HARD, ULTRA;
	}
	public static final String ACTIVITIES = "activities";
	public static final String CATEGORY = "category";
	public static final String COLLECTION_NAME = "roles";
	public static final String CREATION_TIME = "creationTime";
	public static final String DESCRIPTION = "description";
	public static final String TITLE = "title";
	public static final String FLASHMOB = "flashmob";
	public static final String ITEMS = "items";
	public static final String MAX_COUNT = "maxCount";
	public static final String MIN_COUNT = "minCount";
	public static final String START_POINT = "startPoint";

	public static final String USERS = "users";

	@NotNull
	@Reference(value = Role.ACTIVITIES, lazy = true)
	private Set<Activity> activites = Utils.set();

	@Property(Role.CATEGORY)
	private Category category;

	@NotNull
	@Past
	@Indexed
	@Property(Flashmob.CREATION_TIME)
	private DateTime creationTime = new DateTime();

	@NotBlank
	@SafeHtml
	@Property(Role.DESCRIPTION)
	private String description;

	@NotNull
	@Reference(value = Role.FLASHMOB, lazy = true)
	private Flashmob flashmob;

	@NotNull
	@Id
	private ObjectId id = new ObjectId();
	
	@NotBlank
	@Property(Role.TITLE)
	private String title;

	@NotNull
	@Property(Role.ITEMS)
	private Set<String> items = Utils.set();

	@Min(0)
	@Property(Role.MAX_COUNT)
	private Integer maxCount;

	@Min(0)
	@Property(Role.MIN_COUNT)
	private Integer minCount = new Integer(0);

	//@NotNull Mail: Re: [FMT] Service auf giv-flashmob, 19.06.12 - 1940
	@Property(Role.START_POINT)
	private Point startPoint;
	
	@NotNull
	@Reference(value = Role.USERS, lazy = true)
	private Set<User> users = Utils.set();

	public Role addAcitivity(Activity activity) {
		getActivities().add(activity);
		return this;
	}

	public Role addUser(User u) {
		getUsers().add(u);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Role) {
			return getId().equals(((Role) o).getId());
		}
		return false;
	}

	public Set<Activity> getActivities() {
		return activites;
	}

	public Category getCategory() {
		return category;
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
	
	public String getTitle() {
		return title;
	}

	public Set<String> getItems() {
		return items;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public Integer getMinCount() {
		return minCount;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public Set<User> getUsers() {
		return users;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	public Role removeActivity(Activity activity) {
		getActivities().remove(activity);
		return this;
	}

	public Role removeUser(User u) {
		getUsers().remove(u);
		return this;
	}

	public Role setActivities(Set<Activity> activities) {
		this.activites = activities;
		return this;
	}

	public Role setCategory(Category category) {
		this.category = category;
		return this;
	}

	public Role setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	public Role setDescription(String description) {
		this.description = description;
		return this;
	}

	public Role setFlashmob(Flashmob flashmob) {
		this.flashmob = flashmob;
		return this;
	}

	public Role setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public Role setItems(Set<String> items) {
		this.items = items;
		return this;
	}

	public Role setMaxCount(Integer maxCount) {
		if (maxCount != null && maxCount.intValue() > 0) {
			if (getMinCount() != null
					&& getMinCount().intValue() > maxCount.intValue()) {
				throw new IllegalArgumentException(
						"min count can not be bigger than max count. "
								+ maxCount + " <= " + getMinCount());
			}
			this.maxCount = maxCount;
		}
		return this;
	}

	public Role setMinCount(Integer minCount) {
		if (minCount != null && minCount.intValue() > 0) {
			if (getMaxCount() != null && getMaxCount().intValue() < minCount.intValue()) {
				throw new IllegalArgumentException(
						"min count can not be bigger than max count. "
								+ getMaxCount() + " <= " + minCount);
			}
			this.minCount = minCount;
		}
		return this;
	}

	public Role setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
		return this;
	}

	public Role setUsers(Set<User> users) {
		this.users = users;
		return this;
	}

	@Override
	public String toString() {
		return getId().toString();
	}

}
