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
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Role;

@Polymorphic
@Entity(Task.COLLECTION_NAME)
public class Task {
	public static final String ACTIVITY = "activity";
	public static final String COLLECTION_NAME = "tasks";
	public static final String CREATION_TIME = "creationTime";
	public static final String DESCRIPTION = "description";
	public static final String ROLE = "role";

	@NotNull
	@Indexed
	@Reference(value = Task.ACTIVITY, lazy = true)
	private Activity activity;

	@NotNull
	@Past
	@Indexed
	@Property(Task.CREATION_TIME)
	private DateTime creationTime = new DateTime();

	@NotBlank
	@SafeHtml
	@Property(Task.DESCRIPTION)
	private String description;

	@NotNull
	@Id
	private ObjectId id = new ObjectId();

	@NotNull
	@Indexed
	@Reference(value = Task.ROLE, lazy = true)
	private Role role;

	@Override
	public boolean equals(Object o) {
		if (o instanceof Task) {
			return getId().equals(((Task) o).getId());
		}
		return false;
	}

	public Activity getActivity() {
		return activity;
	}

	public DateTime getCreationTime() {
		return creationTime;
	}

	public String getDescription() {
		return description;
	}
	
	public ObjectId getId() {
		return id;
	}

	public Role getRole() {
		return role;
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	public Task setActivity(Activity activity) {
		this.activity = activity;
		return this;
	}

	public Task setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	public Task setDescription(String description) {
		this.description = description;
		return this;
	}

	public Task setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public Task setRole(Role role) {
		this.role = role;
		return this;
	}

	@Override
	public String toString() {
		return getId().toString();
	}

}
