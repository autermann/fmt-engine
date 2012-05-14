package de.ifgi.fmt.model.signal;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Polymorphic;

import de.ifgi.fmt.mongo.Identifiable;

@Polymorphic
@Entity(Signal.COLLECTION_NAME)
public abstract class Signal extends Identifiable {
	
	public static final String COLLECTION_NAME = "signals";
	
	public String getType() {
		return getClass().getName()
				.replace(getClass().getPackage().getName() + ".", "")
				.replace("Signal", "");
	}

}
