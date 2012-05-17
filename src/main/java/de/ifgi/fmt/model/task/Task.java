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

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.mongo.Identifiable;

@Polymorphic
@Entity(Task.COLLECTION_NAME)
public class Task extends Identifiable {
	public static final String COLLECTION_NAME = "tasks";
	public static final String DESCRIPTION = "description";
	public static final String ROLE = "role";
	public static final String ACTIVITY = "activity";

	@Property(Task.DESCRIPTION)
	private String description;

	@Indexed
	@Reference(Task.ROLE)
	private Role role;
	
	@Indexed
	@Reference(Task.ACTIVITY)
	private Activity activity;
	
	public String getDescription() {
		return description;
	}

	public Task setDescription(String description) {
		this.description = description;
		return this;
	}

	public Activity getActivity() {
		return activity;
	}

	public Task setActivity(Activity activity) {
		this.activity = activity;
		return this;
	}

	public Role getRole() {
		return role;
	}

	public Task setRole(Role role) {
		this.role = role;
		return this;
	}

}
