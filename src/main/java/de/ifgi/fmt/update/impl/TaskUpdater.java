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
package de.ifgi.fmt.update.impl;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.task.LineTask;
import de.ifgi.fmt.model.task.LinkTask;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.update.EntityUpdater;
import de.ifgi.fmt.update.UpdateFactory.Updates;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Updates(Task.class)
public class TaskUpdater extends EntityUpdater<Task> {

	/**
	 * 
	 * @param old
	 * @param changes
	 * @return
	 */
	@Override
	public Task update(Task old, Task changes) {
		if (old instanceof LineTask) {
			if (changes instanceof LinkTask) {
				throw ServiceError.badRequest("incompatible task types");
			}
			LineTask tt = (LineTask) changes;
			if (tt.getLine() != null) {
				((LineTask) old).setLine(tt.getLine());
			}
		} else if (old instanceof LinkTask) {
			if (changes instanceof LineTask) {
				throw ServiceError.badRequest("incompatible task types");
			}
			LinkTask tt = (LinkTask) changes;
			if (tt.getLink() != null) {
				((LinkTask) old).setLink(tt.getLink());
			}
			if (tt.getType() != null) {
				((LinkTask) old).setType(tt.getType());
			}
		}
		if (changes.getDescription() != null) {
			old.setDescription(changes.getDescription());
		}
		return old;
	}

}
