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

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.update.EntityUpdater;
import de.ifgi.fmt.update.UpdateFactory.Updates;

@Updates(Activity.class)
public class ActivityUpdater extends EntityUpdater<Activity> {

	@Override
	public Activity update(Activity old, Activity changes) {
		if (changes.getDescription() != null) {
			old.setDescription(changes.getDescription());
		}
		if (changes.getTitle() != null) {
			old.setTitle(changes.getTitle());
		}
		return old;
	}

}