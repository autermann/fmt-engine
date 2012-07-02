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

/**
 * This Class reoresents a Comment
 * @author Autermann, Demuth, Radtke
 */
@Entity(Comment.COLLECTION_NAME)
public class Comment {

    /**
     * Definition
     */
    public static final String COLLECTION_NAME = "comments";
    /**
     * Definition
     */
    public static final String CREATION_TIME = "creationTime";
    /**
     * Definition
     */
    public static final String FLASHMOB = "flashmob";
    /**
     * Definition
     */
    public static final String LAST_CHANGED = "lastChanged";
    /**
     * Definition
     */
    public static final String TEXT = "text";
    /**
     * Definition
     */
    public static final String TIME = "time";
    /**
     * Definition
     */
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

    /**
     * Set the time whne this comment was changed last
     */
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

    /**
     * Return the time when this comment was created
     * @return a datetime
     */
    public DateTime getCreationTime() {
	return creationTime;
    }

    /**
     * Return the Flashmob associated to this comment
     * @return a flashmob
     */
    public Flashmob getFlashmob() {
	return flashmob;
    }

    /**
     * retunr the id of this comment
     * @return ObjectID
     */
    public ObjectId getId() {
	return id;
    }

    /**
     * Retunr the time when this comment was changed last
     * @return datetime
     */
    public DateTime getLastChangedTime() {
	return lastChangedTime;
    }

    /**
     * Get the comment-text of this Comment
     * @return a String
     */
    public String getText() {
	return text;
    }

    /**
     * Get the time-attribute of a comment
     * @return a datetime
     */
    public DateTime getTime() {
	return time;
    }

    /**
     * Get the User associated to this comment
     * @return a User
     */
    public User getUser() {
	return user;
    }

    @Override
    public int hashCode() {
	return getId().hashCode();
    }

    /**
     * Set the Creationtime of this comment
     * @param creationTime a datetime
     * @return this comment
     */
    public Comment setCreationTime(DateTime creationTime) {
	this.creationTime = creationTime;
	return this;
    }

    /**
     * Associated a flashmob with this comment
     * @param flashmob a flashmob
     * @return this comment
     */
    public Comment setFlashmob(Flashmob flashmob) {
	this.flashmob = flashmob;
	return this;
    }

    /**
     * Set the ObjectID of this comment
     * @param id an objectid
     * @return this comment
     */
    public Comment setId(ObjectId id) {
	this.id = id;
	return this;
    }

    /**
     * Set the time when this comment was changed last
     * @param lastChangedTime a datetime
     * @return this comment
     */
    public Comment setLastChangedTime(DateTime lastChangedTime) {
	this.lastChangedTime = lastChangedTime;
	return this;
    }

    /**
     * Set the text of a comment
     * @param text a String
     * @return this comment
     */
    public Comment setText(String text) {
	this.text = text;
	return this;
    }

    /**
     * Set the time of this comment
     * @param time a datetime
     * @return this comment
     */
    public Comment setTime(DateTime time) {
	this.time = time;
	return this;
    }

    /**
     * Set the User of a Comment
     * @param user a User
     * @return this comment
     */
    public Comment setUser(User user) {
	this.user = user;
	return this;
    }

    @Override
    public String toString() {
	return getId().toString();
    }
}
