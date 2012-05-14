package de.ifgi.fmt.model;

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
		return BCrypt.hashpw(unhashed, BCrypt.gensalt());
	}
	
}
