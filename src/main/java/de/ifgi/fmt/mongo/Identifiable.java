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
package de.ifgi.fmt.mongo;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;

@Polymorphic
public abstract class Identifiable {

	public static final String CREATION_TIME = "creationTime";

	@Id
	private ObjectId id;

	@Indexed
	@Property(Identifiable.CREATION_TIME)
	private DateTime creationTime = new DateTime();

	public Identifiable(String s) {
		this(new ObjectId(s));
	}

	public Identifiable(ObjectId id) {
		setId(id);
	}

	public Identifiable() {
		setId(new ObjectId());
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public DateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Identifiable) {
			return getId().equals(((Identifiable) o).getId());
		}
		return false;
	}

	@Override
	public String toString() {
		return getId().toString();
	}

}
