package de.ifgi.fmt.model.signal;

import com.google.code.morphia.annotations.Polymorphic;

import de.ifgi.fmt.mongo.Identifiable;

@Polymorphic
public abstract class Signal extends Identifiable {
	
	public String getType() {
		return getClass().getName()
				.replace(getClass().getPackage().getName() + ".", "")
				.replace("Signal", "");
	}

}
