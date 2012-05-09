package de.ifgi.fmt.model.trigger;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Reference;

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.mongo.Identifiable;

@Polymorphic
@Entity(Trigger.COLLECTION_NAME)
public class Trigger extends Identifiable {
	public static final String COLLECTION_NAME = "triggers";
	public static final String FLASHMOB = "flashmob";
	
	@Reference(Trigger.FLASHMOB)
	private Flashmob flashmob;

	public Flashmob getFlashmob() {
		return flashmob;
	}

	public void setFlashmob(Flashmob flashmob) {
		this.flashmob = flashmob;
	}

}
