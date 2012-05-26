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

import javax.validation.constraints.NotNull;

import com.google.code.morphia.annotations.Reference;

import de.ifgi.fmt.model.task.Task;

public class TaskForRole {
	public static final String ROLE = "role";
	public static final String TASK = "task";

	@NotNull
	@Reference(value = TaskForRole.ROLE, lazy = true)
	private Role role;
	
	@NotNull
	@Reference(value = TaskForRole.TASK, lazy = true)
	private Task task;

	public TaskForRole(Role role, Task task) {
		this.role = role;
		this.task = task;
	}

	public TaskForRole() {
	}

	public Task getTask() {
		return task;
	}

	public TaskForRole setTask(Task task) {
		this.task = task;
		return this;
	}

	public Role getRole() {
		return role;
	}

	public TaskForRole setRole(Role role) {
		this.role = role;
		return this;
	}
}
