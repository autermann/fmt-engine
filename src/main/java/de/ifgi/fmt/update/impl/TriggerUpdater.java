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
import de.ifgi.fmt.model.trigger.EventTrigger;
import de.ifgi.fmt.model.trigger.LocationTrigger;
import de.ifgi.fmt.model.trigger.TimeTrigger;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.update.EntityUpdater;
import de.ifgi.fmt.update.UpdateFactory.Updates;

@Updates(Trigger.class)
public class TriggerUpdater extends EntityUpdater<Trigger> {

	@Override
	public Trigger update(Trigger old, Trigger changes) {
		if (old instanceof EventTrigger) {
			if (!(changes instanceof EventTrigger)) {
				throw ServiceError.badRequest("incompatible trigger types");
			}
			EventTrigger tt = (EventTrigger) changes;
			if (tt.getDescription() != null) {
				((EventTrigger) old).setDescription(tt.getDescription());
			}
		} else if (old instanceof TimeTrigger) {
			if (!(changes instanceof TimeTrigger)) {
				throw ServiceError.badRequest("incompatible trigger types");
			}
			TimeTrigger tt = (TimeTrigger) changes;
			if (tt.getTime() != null) {
				((TimeTrigger) old).setTime(tt.getTime());
			}
			
		} else if (old instanceof LocationTrigger) {
			if (!(changes instanceof LocationTrigger)) {
				throw ServiceError.badRequest("incompatible trigger types");
			}
			LocationTrigger tt = (LocationTrigger) changes;
			if (tt.getLocation() != null) {
				((LocationTrigger) old).setLocation(tt.getLocation());
			}
		}
		return old;
	}

}
