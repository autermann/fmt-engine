package de.ifgi.fmt.model;

import com.google.code.morphia.annotations.Reference;

import de.ifgi.fmt.model.task.Task;

public class TaskForRole {
	public static final String ROLE = "role";
	public static final String TASK = "task";
	
	@Reference(TaskForRole.ROLE)
	private Role role;
	@Reference(TaskForRole.TASK)
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
