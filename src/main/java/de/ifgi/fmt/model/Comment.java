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
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;

@Entity(Comment.COLLECTION_NAME)
public class Comment {

	public static final String COLLECTION_NAME = "comments";
	public static final String CREATION_TIME = "creationTime";
	public static final String FLASHMOB = "flashmob";
	public static final String LAST_CHANGED = "lastChanged";
	public static final String TEXT = "text";
	public static final String TIME = "time";
	public static final String USER = "user";

	@NotNull
	@Past
	@Indexed
	@Property(Comment.CREATION_TIME)
	private DateTime creationTime = new DateTime();

	@NotNull
	@Indexed
	@Reference(value = Comment.FLASHMOB, lazy = true)
	private Flashmob flashmob;

	@NotNull
	@Id
	private ObjectId id = new ObjectId();

	@NotNull
	@Past
	@Indexed
	@Property(Comment.LAST_CHANGED)
	private DateTime lastChangedTime = new DateTime();

	@SafeHtml
	@NotBlank
	@Property(Comment.TEXT)
	private String text;

	@NotNull
	@Indexed
	@Past
	@Property(Comment.TIME)
	private DateTime time;

	@NotNull
	@Indexed
	@Reference(value = Comment.USER, lazy = true)
	private User user;

	@PrePersist
	public void changed() {
		setLastChangedTime(new DateTime());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Comment) {
			return getId().equals(((Comment) o).getId());
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

	public String getText() {
		return text;
	}

	public DateTime getTime() {
		return time;
	}

	public User getUser() {
		return user;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	public Comment setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	public Comment setFlashmob(Flashmob flashmob) {
		this.flashmob = flashmob;
		return this;
	}

	public Comment setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public Comment setLastChangedTime(DateTime lastChangedTime) {
		this.lastChangedTime = lastChangedTime;
		return this;
	}

	public Comment setText(String text) {
		this.text = text;
		return this;
	}

	public Comment setTime(DateTime time) {
		this.time = time;
		return this;
	}

	public Comment setUser(User user) {
		this.user = user;
		return this;
	}

	@Override
	public String toString() {
		return getId().toString();
	}

}
