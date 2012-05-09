package de.ifgi.fmt.model;

import java.util.Set;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;

import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.mongo.Identifiable;

@Polymorphic
@Entity(Activity.COLLECTION_NAME)
public class Activity extends Identifiable {

	public static final String COLLECTION_NAME = "activities";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String FLASHMOB = "flashmob";
	public static final String ROLES = "roles";
	public static final String TRIGGER = "trigger";

	@Property(Activity.TITLE)
	private String title;
	
	@Property(Activity.DESCRIPTION)
	private String description;
	
	@Reference(Activity.FLASHMOB)
	private Flashmob flashmob;
	
	@Property(Activity.ROLES)
	private Set<Role> roles;
	
	@Reference(Activity.TRIGGER)
	private Trigger trigger;	

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

	public Flashmob getFlashmob() {
		return flashmob;
	}

	public void setFlashmob(Flashmob flashmob) {
		this.flashmob = flashmob;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

}
