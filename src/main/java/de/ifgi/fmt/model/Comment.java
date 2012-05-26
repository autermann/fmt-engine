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
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;

import de.ifgi.fmt.mongo.Identifiable;

@Polymorphic
@Entity(Comment.COLLECTION_NAME)
public class Comment extends Identifiable {

	public static final String COLLECTION_NAME = "comments";
	public static final String TEXT = "text";
	public static final String FLASHMOB = "flashmob";
	public static final String USER = "user";
	public static final String TIME = "time";

	@NotNull
	@Indexed
	@Reference(value = Comment.FLASHMOB, lazy = true)
	private Flashmob flashmob;

	@NotNull
	@Indexed
	@Reference(value = Comment.USER, lazy = true)
	private User user;
	
	@SafeHtml
	@NotBlank
	@Property(Comment.TEXT)
	private String text;

	@NotNull
	@Indexed
	@Past
	@Property(Comment.TIME)
	private DateTime time;

	public Comment(ObjectId id) {
		super(id);
	}

	public Comment(String id) {
		super(id);
	}

	public Comment() {
		super();
	}

	public Flashmob getFlashmob() {
		return flashmob;
	}

	public Comment setFlashmob(Flashmob flashmob) {
		this.flashmob = flashmob;
		return this;
	}

	public User getUser() {
		return user;
	}

	public Comment setUser(User user) {
		this.user = user;
		return this;
	}

	public String getText() {
		return text;
	}

	public Comment setText(String text) {
		this.text = text;
		return this;
	}

	public DateTime getTime() {
		return time;
	}

	public Comment setTime(DateTime time) {
		this.time = time;
		return this;
	}

}
