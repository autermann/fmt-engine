package de.ifgi.fmt.model;

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

	@Reference(Comment.FLASHMOB)
	private Flashmob flashmob;
	
	@Reference(Comment.USER)
	private User user;

	@Property(Comment.TEXT)
	private String text;

	public Flashmob getFlashmob() {
		return flashmob;
	}

	public void setFlashmob(Flashmob flashmob) {
		this.flashmob = flashmob;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
