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
import javax.validation.constraints.Past;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.utils.constants.ModelConstants;


/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Entity(ModelConstants.Trigger.COLLECTION_NAME)
public class Trigger {
	
	@NotNull
	@Past
	@Indexed
	@Property(ModelConstants.Common.CREATION_TIME)
	private DateTime creationTime = new DateTime();

	@SafeHtml
	@Property(ModelConstants.Trigger.DESCRIPTION)
	private String description;

	@NotNull
	@Indexed
	@Reference(value = ModelConstants.Trigger.FLASHMOB, lazy = true)
	private Flashmob flashmob;

	@NotNull
	@Id
	private ObjectId id = new ObjectId();

	@NotNull
	@Indexed
	@Property(ModelConstants.Common.LAST_CHANGED)
	private DateTime lastChangedTime = new DateTime();

	@Property(ModelConstants.Trigger.LOCATION)
	private Point location;

	@NotNull
	@Property(ModelConstants.Trigger.TIME)
	private DateTime time;

	/**
	 * 
	 */
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

	/**
	 * 
	 * @return
	 */
	public DateTime getCreationTime() {
		return creationTime;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @return
	 */
	public Flashmob getFlashmob() {
		return flashmob;
	}

	/**
	 * 
	 * @return
	 */
	public ObjectId getId() {
		return id;
	}

	/**
	 * 
	 * @return
	 */
	public DateTime getLastChangedTime() {
		return lastChangedTime;
	}

	/**
	 * 
	 * @return
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * 
	 * @return
	 */
	public DateTime getTime() {
		return time;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	/**
	 * 
	 * @param creationTime
	 * @return
	 */
	public Trigger setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	/**
	 * 
	 * @param description
	 * @return
	 */
	public Trigger setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * 
	 * @param flashmob
	 * @return
	 */
	public Trigger setFlashmob(Flashmob flashmob) {
		this.flashmob = flashmob;
		return this;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Trigger setId(ObjectId id) {
		this.id = id;
		return this;
	}

	/**
	 * 
	 * @param lastChangedTime
	 * @return
	 */
	public Trigger setLastChangedTime(DateTime lastChangedTime) {
		this.lastChangedTime = lastChangedTime;
		return this;
	}

	/**
	 * 
	 * @param location
	 * @return
	 */
	public Trigger setLocation(Point location) {
		this.location = location;
		return this;
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public Trigger setTime(DateTime time) {
		this.time = time;
		return this;
	}

	@Override
	public String toString() {
		return getId().toString();
	}

}
