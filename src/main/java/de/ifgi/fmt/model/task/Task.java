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
package de.ifgi.fmt.model.task;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.utils.constants.ModelConstants;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Polymorphic
@Entity(ModelConstants.Task.COLLECTION_NAME)
public class Task {

	@NotNull
	@Indexed
	@Reference(value = ModelConstants.Task.ACTIVITY, lazy = true)
	private Activity activity;

	@NotNull
	@Past
	@Indexed
	@Property(ModelConstants.Common.CREATION_TIME)
	private DateTime creationTime = new DateTime();

	@NotBlank
	@SafeHtml
	@Property(ModelConstants.Task.DESCRIPTION)
	private String description;

	@NotNull
	@Id
	private ObjectId id = new ObjectId();

	@NotNull
	@Indexed
	@Property(ModelConstants.Common.LAST_CHANGED)
	private DateTime lastChangedTime = new DateTime();

	@NotNull
	@Indexed
	@Reference(value = ModelConstants.Task.ROLE, lazy = true)
	private Role role;

	/**
	 * 
	 */
	@PrePersist
	public void changed() {
		setLastChangedTime(new DateTime());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Task) {
			return getId().equals(((Task) o).getId());
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public Activity getActivity() {
		return activity;
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
	public ObjectId getId() {
		return id;
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
	public Role getRole() {
		return role;
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
	public Task setActivity(Activity activity) {
		this.activity = activity;
		return this;
	}

	/**
	 * 
	 * @param creationTime
	 * @return
	 */
	public Task setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	/**
	 * 
	 * @param description
	 * @return
	 */
	public Task setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Task setId(ObjectId id) {
		this.id = id;
		return this;
	}

	/**
	 * 
	 * @param lastChangedTime
	 * @return
	 */
	public Task setLastChangedTime(DateTime lastChangedTime) {
		this.lastChangedTime = lastChangedTime;
		return this;
	}

	/**
	 * 
	 * @param role
	 * @return
	 */
	public Task setRole(Role role) {
		this.role = role;
		return this;
	}

	@Override
	public String toString() {
		return getId().toString();
	}

}
