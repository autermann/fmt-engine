package de.ifgi.fmt.model.task;

import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;

import de.ifgi.fmt.mongo.Identifiable;

@Polymorphic
public class Task extends Identifiable {

	public static final String DESCRIPTION = "description";

	@Property(Task.DESCRIPTION)
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
