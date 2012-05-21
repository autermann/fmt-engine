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

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;

import de.ifgi.fmt.mongo.Identifiable;
import de.ifgi.fmt.utils.BCrypt;


@Polymorphic
@Entity(User.COLLECTION_NAME)
public class User extends Identifiable {

	public static final String COLLECTION_NAME = "users";
	public static final String PASSWORD_HASH = "password";
	public static final String EMAIL = "email";
	public static final String USERNAME = "username";

	@Property(User.USERNAME)
	@Indexed(unique=true)
	private String username;
	
	@Property(User.EMAIL)
	@Indexed(unique=true)
	private String email;

	@Property(User.PASSWORD_HASH)
	private String passwordHash;
	
	public User(ObjectId id) {
		super(id);
	}

	public User(String id) {
		super(id);
	}

	public User() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public User setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public User setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public User setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
		return this;
	}
	
	public User setPassword(String password) {
		return setPasswordHash(hash(password));
	}
	
	public boolean isValidPassword(String password) {
		return BCrypt.checkpw(password, getPasswordHash());
	}

	private String hash(String unhashed) {
		if (unhashed == null) {
			return null;
		}
		return BCrypt.hashpw(unhashed, BCrypt.gensalt());
	}
	
}
