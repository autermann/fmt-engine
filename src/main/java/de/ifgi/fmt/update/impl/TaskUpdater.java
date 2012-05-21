package de.ifgi.fmt.update.impl;

import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.update.EntityUpdater;
import de.ifgi.fmt.update.UpdateFactory.Updates;

@Updates(Task.class)
public class TaskUpdater extends EntityUpdater<Task> {

	@Override
	public Task update(Task old, Task changes) {
		// TODO update task
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
