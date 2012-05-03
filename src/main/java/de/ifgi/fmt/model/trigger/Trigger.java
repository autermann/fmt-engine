package de.ifgi.fmt.model.trigger;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Polymorphic;

import de.ifgi.fmt.mongo.Identifiable;

@Polymorphic
@Entity(Trigger.COLLECTION_NAME)
public class Trigger extends Identifiable {
	public static final String COLLECTION_NAME = "triggers";

}
