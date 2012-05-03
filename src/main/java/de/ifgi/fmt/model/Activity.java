package de.ifgi.fmt.model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;

import de.ifgi.fmt.mongo.Identifiable;


@Polymorphic
@Entity(Activity.COLLECTION_NAME)
public class Activity extends Identifiable {

	public static final String COLLECTION_NAME = "activities";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	
	@Property(Activity.TITLE)
	private String title;
	@Property(Activity.DESCRIPTION)
	private String description;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
