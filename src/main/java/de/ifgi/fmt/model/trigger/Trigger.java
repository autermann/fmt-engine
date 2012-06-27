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
import javax.validation.constraints.Past;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;

import de.ifgi.fmt.model.Flashmob;

@Entity(Trigger.COLLECTION_NAME)
public class Trigger {
	public static final String COLLECTION_NAME = "triggers";
	public static final String CREATION_TIME = "creationTime";
	public static final String FLASHMOB = "flashmob";
	public static final String LAST_CHANGED = "lastChanged";

	@NotNull
	@Past
	@Indexed
	@Property(Trigger.CREATION_TIME)
	private DateTime creationTime = new DateTime();

	@NotNull
	@Indexed
	@Reference(value = Trigger.FLASHMOB, lazy = true)
	private Flashmob flashmob;

	@NotNull
	@Id
	private ObjectId id = new ObjectId();

	@NotNull
	@Past
	@Indexed
	@Property(Trigger.LAST_CHANGED)
	private DateTime lastChangedTime = new DateTime();

	@PrePersist
	public void changed() {
		setLastChangedTime(new DateTime());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Trigger) {
			return getId().equals(((Trigger) o).getId());
		}
		return false;
	}

	public DateTime getCreationTime() {
		return creationTime;
	}

	public Flashmob getFlashmob() {
		return flashmob;
	}

	public ObjectId getId() {
		return id;
	}

	public DateTime getLastChangedTime() {
		return lastChangedTime;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	public Trigger setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	public Trigger setFlashmob(Flashmob flashmob) {
		this.flashmob = flashmob;
		return this;
	}

	public Trigger setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public Trigger setLastChangedTime(DateTime lastChangedTime) {
		this.lastChangedTime = lastChangedTime;
		return this;
	}

	@Override
	public String toString() {
		return getId().toString();
	}

}
