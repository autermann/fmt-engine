package de.ifgi.fmt.model;

import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Entity;
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

	@Reference(Comment.FLASHMOB)
	private Flashmob flashmob;
	
	@Reference(Comment.USER)
	private User user;

	@Property(Comment.TEXT)
	private String text;
	
	@Property(Comment.TIME)
	private DateTime time;

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
