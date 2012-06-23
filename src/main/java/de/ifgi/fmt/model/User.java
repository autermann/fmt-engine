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

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Property;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.utils.BCrypt;
import de.ifgi.fmt.utils.Utils;
import de.ifgi.fmt.utils.constants.Constants.Regex;
import de.ifgi.fmt.utils.constants.RESTConstants.Roles;

@Entity(User.COLLECTION_NAME)
public class User {
	public static final String AUTH_TOKEN = "authToken";
	public static final String COLLECTION_NAME = "users";
	public static final String CREATION_TIME = "creationTime";
	public static final String EMAIL = "email";
	public static final String PASSWORD_HASH = "password";
	public static final String ROLES = "roles";
	public static final String USERNAME = "username";

	@Property(User.AUTH_TOKEN)
	private String authToken;

	@NotNull
	@Past
	@Indexed
	@Property(User.CREATION_TIME)
	private DateTime creationTime = new DateTime();

	@Email
	@Property(User.EMAIL)
	@Indexed(unique = true, sparse = true)
	private String email;

	@NotEmpty
	@NotNull
	@Property(User.PASSWORD_HASH)
	private String passwordHash;

	@NotNull
	@Property(User.ROLES)
	private Set<String> roles = Utils.set(Roles.USER);

	@Id
	@NotBlank
	@SafeHtml
	@Length(min = 4, max = 128)
	@Pattern(regexp = "^[\\w]+$")
	@Property(User.USERNAME)
	@Indexed(unique = true, sparse = true)
	private String username;

	public User addRole(String role) {
		getRoles().add(role);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			return getUsername().equals(((User) o).getUsername());
		}
		return false;
	}
	public String getAuthToken() {
		return authToken;
	}
	
	public DateTime getCreationTime() {
		return creationTime;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public String getUsername() {
		return username;
	}

	private String hash(String unhashed) {
		if (unhashed == null) {
			return null;
		}
		return BCrypt.hashpw(unhashed, BCrypt.gensalt());
	}

	@Override
	public int hashCode() {
		return getUsername().hashCode();
	}

	public boolean hasRole(String role) {
		for (String r : getRoles()) {
			if (r.equalsIgnoreCase(role)) {
				return true;
			}
		}
		return false;
	}

	public boolean isValidPassword(String password) {
		return BCrypt.checkpw(password, getPasswordHash());
	}

	public User setAuthToken(String authToken) {
		this.authToken = authToken;
		return this;
	}

	public User setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	public User setEmail(String s) {
		if (s != null) {
			s = s.trim();
		}
		this.email = s;
		return this;
	}

	public User setPassword(String s) {
		if (s != null) {
			if (!Regex.PASSWORD.matcher(s).matches()) {
				throw ServiceError.badRequest("password does not match '"
						+ Regex.PASSWORD.pattern() + "'");
			}
		}
		return setPasswordHash(hash(s));
	}

	public User setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
		return this;
	}

	public User setRoles(Set<String> roles) {
		this.roles = roles;
		return this;
	}

	public User setUsername(String s) {
		this.username = s;
		return this;
	}

	@Override
	public String toString() {
		return getUsername();
	}

}
