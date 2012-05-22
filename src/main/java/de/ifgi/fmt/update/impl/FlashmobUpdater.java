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

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Validity;
import de.ifgi.fmt.update.EntityUpdater;
import de.ifgi.fmt.update.UpdateFactory.Updates;

@Updates(Flashmob.class)
public class FlashmobUpdater extends EntityUpdater<Flashmob>{

	@Override
	public Flashmob update(Flashmob old, Flashmob changes) {
		if (changes.getDescription() != null) {
			old.setDescription(changes.getDescription());
		}
		if (changes.getPublish() != null) {
			old.setPublish(changes.getPublish());
		}
		if (changes.getEnd() != null) {
			old.setEnd(changes.getEnd());
		}
		if (changes.getStart() != null) {
			old.setEnd(changes.getEnd());
		}
		if (changes.isPublic() != null) {
			old.setPublic(changes.isPublic());
		}
		if (changes.getKey() != null) {
			old.setKey(changes.getKey());
		}
		if (changes.getLocation() != null) {
			old.setLocation(changes.getLocation());
		}
		if (changes.getTitle() != null) {
			old.setTitle(changes.getTitle());
		}
		old.setValidity(Validity.NOT_CHECKED);
		return old;
	}

}
