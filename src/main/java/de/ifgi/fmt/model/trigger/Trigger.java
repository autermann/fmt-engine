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
package de.ifgi.fmt.model.trigger;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Reference;

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.mongo.Identifiable;

@Polymorphic
@Entity(Trigger.COLLECTION_NAME)
public class Trigger extends Identifiable {
	public static final String COLLECTION_NAME = "triggers";
	public static final String FLASHMOB = "flashmob";
	// public static final String ACTIVITIES = "activities";

	@NotNull
	@Indexed
	@Reference(value = Trigger.FLASHMOB, lazy = true)
	private Flashmob flashmob;

	public Trigger(ObjectId id) {
		super(id);
	}

	public Trigger(String id) {
		super(id);
	}

	public Trigger() {
		super();
	}

	public Flashmob getFlashmob() {
		return flashmob;
	}

	public Trigger setFlashmob(Flashmob flashmob) {
		this.flashmob = flashmob;
		return this;
	}

}
