package de.ifgi.fmt.model.task;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.mongo.Identifiable;

@Polymorphic
@Entity(Task.COLLECTION_NAME)
public class Task extends Identifiable {
	
	public static final String COLLECTION_NAME = "tasks";
	public static final String DESCRIPTION = "description";
	public static final String ROLE = "role";
	public static final String ACTIVITY = "activity";

	@Property(Task.DESCRIPTION)
	private String description;

	@Reference(Task.ROLE)
	private Role role;
	
	@Reference(Task.ACTIVITY)
	private Activity activity;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
