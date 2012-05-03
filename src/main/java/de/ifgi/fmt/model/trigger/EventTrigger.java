package de.ifgi.fmt.model.trigger;

import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;

@Polymorphic
public class EventTrigger extends Trigger {

	public static final String DESCRIPTION = "description";

	@Property(EventTrigger.DESCRIPTION)
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
