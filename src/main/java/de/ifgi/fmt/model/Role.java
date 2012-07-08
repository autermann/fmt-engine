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
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.utils.Utils;
import de.ifgi.fmt.utils.constants.ModelConstants;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Entity(ModelConstants.Role.COLLECTION_NAME)
public class Role {

	/**
     * 
     */
	public enum Category {
		EASY, HARD, ULTRA;
	}

	@NotNull
	@Reference(value = ModelConstants.Role.ACTIVITIES, lazy = true)
	private Set<Activity> activites = Utils.set();
	@Property(ModelConstants.Role.CATEGORY)
	private Category category;
	@NotNull
	@Past
	@Indexed
	@Property(ModelConstants.Common.CREATION_TIME)
	private DateTime creationTime = new DateTime();
	@NotBlank
	@SafeHtml
	@Property(ModelConstants.Role.DESCRIPTION)
	private String description;
	@NotNull
	@Reference(value = ModelConstants.Role.FLASHMOB, lazy = true)
	private Flashmob flashmob;
	@NotNull
	@Id
	private ObjectId id = new ObjectId();
	@NotNull
	@Property(ModelConstants.Role.ITEMS)
	private Set<String> items = Utils.set();
	@NotNull
	@Indexed
	@Property(ModelConstants.Common.LAST_CHANGED)
	private DateTime lastChangedTime = new DateTime();
	@Min(0)
	@Property(ModelConstants.Role.MAX_COUNT)
	private Integer maxCount;
	@Min(0)
	@Property(ModelConstants.Role.MIN_COUNT)
	private Integer minCount = new Integer(0);
	@Property(ModelConstants.Role.START_POINT)
	private Point startPoint;
	@NotBlank
	@Property(ModelConstants.Role.TITLE)
	private String title;
	@NotNull
	@Reference(value = ModelConstants.Role.USERS, lazy = true)
	private Set<User> users = Utils.set();

	/**
	 * 
	 * @param activity
	 * @return
	 */
	public Role addActivity(Activity activity) {
		getActivities().add(activity);
		return this;
	}

	/**
	 * 
	 * @param u
	 * @return
	 */
	public Role addUser(User u) {
		getUsers().add(u);
		return this;
	}

	/**
     * 
     */
	@PrePersist
	public void changed() {
		setLastChangedTime(new DateTime());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Role) {
			return getId().equals(((Role) o).getId());
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public Set<Activity> getActivities() {
		return activites;
	}

	/**
	 * 
	 * @return
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * 
	 * @return
	 */
	public DateTime getCreationTime() {
		return creationTime;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @return
	 */
	public Flashmob getFlashmob() {
		return flashmob;
	}

	/**
	 * 
	 * @return
	 */
	public ObjectId getId() {
		return id;
	}

	/**
	 * 
	 * @return
	 */
	public Set<String> getItems() {
		return items;
	}

	/**
	 * 
	 * @return
	 */
	public DateTime getLastChangedTime() {
		return lastChangedTime;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getMaxCount() {
		return maxCount;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getMinCount() {
		return minCount;
	}

	/**
	 * 
	 * @return
	 */
	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @return
	 */
	public Set<User> getUsers() {
		return users;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	/**
	 * 
	 * @param activity
	 * @return
	 */
	public Role removeActivity(Activity activity) {
		getActivities().remove(activity);
		return this;
	}

	/**
	 * 
	 * @param u
	 * @return
	 */
	public Role removeUser(User u) {
		getUsers().remove(u);
		return this;
	}

	/**
	 * 
	 * @param activities
	 * @return
	 */
	public Role setActivities(Set<Activity> activities) {
		this.activites = activities;
		return this;
	}

	/**
	 * 
	 * @param category
	 * @return
	 */
	public Role setCategory(Category category) {
		this.category = category;
		return this;
	}

	/**
	 * 
	 * @param creationTime
	 * @return
	 */
	public Role setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	/**
	 * 
	 * @param description
	 * @return
	 */
	public Role setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * 
	 * @param flashmob
	 * @return
	 */
	public Role setFlashmob(Flashmob flashmob) {
		this.flashmob = flashmob;
		return this;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Role setId(ObjectId id) {
		this.id = id;
		return this;
	}

	/**
	 * 
	 * @param items
	 * @return
	 */
	public Role setItems(Set<String> items) {
		this.items = items;
		return this;
	}

	/**
	 * 
	 * @param lastChangedTime
	 * @return
	 */
	public Role setLastChangedTime(DateTime lastChangedTime) {
		this.lastChangedTime = lastChangedTime;
		return this;
	}

	/**
	 * 
	 * @param maxCount
	 * @return
	 */
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

	/**
	 * 
	 * @param minCount
	 * @return
	 */
	public Role setMinCount(Integer minCount) {
		if (minCount != null && minCount.intValue() > 0) {
			if (getMaxCount() != null
					&& getMaxCount().intValue() < minCount.intValue()) {
				throw new IllegalArgumentException(
						"min count can not be bigger than max count. "
								+ getMaxCount() + " <= " + minCount);
			}
			this.minCount = minCount;
		}
		return this;
	}

	/**
	 * 
	 * @param startPoint
	 * @return
	 */
	public Role setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
		return this;
	}

	/**
	 * 
	 * @param title
	 * @return
	 */
	public Role setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * 
	 * @param users
	 * @return
	 */
	public Role setUsers(Set<User> users) {
		this.users = users;
		return this;
	}

	@Override
	public String toString() {
		return getId().toString();
	}
}
