package de.ifgi.fmt;

import static de.ifgi.fmt.update.UpdateFactory.getUpdater;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.mongo.Store;

public class Service {

	private static Service service;

	public static Service getInstance() {
		return (service == null) ? service = new Service() : service;
	}
	
	private Store fmStore = new Store();

	public Store getFlashmobStore() {
		return fmStore;
	}
	
	public Flashmob getFlashmob(ObjectId flashmob) {
		Flashmob f = getFlashmobStore().getFlashmob(flashmob);
		if (f == null) {
			throw ServiceError.noSuchFlashmob();
		}
		return f;
	}

	public Flashmob createFlashmob(Flashmob f) {
		return getFlashmobStore().saveFlashmob(f);
	}

	public Flashmob updateFlashmob(ObjectId id, Flashmob flashmob) {
		return getFlashmobStore().saveFlashmob(getUpdater(Flashmob.class).update(getFlashmob(id), flashmob));
	}
}
