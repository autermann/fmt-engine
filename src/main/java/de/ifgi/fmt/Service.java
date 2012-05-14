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
	
	private Store store = new Store();

	public Store getStore() {
		return this.store;
	}
	
	public Flashmob getFlashmob(ObjectId flashmob) {
		Flashmob f = getStore().getFlashmob(flashmob);
		if (f == null) {
			throw ServiceError.noSuchFlashmob();
		}
		return f;
	}

	public Flashmob createFlashmob(Flashmob f) {
		return getStore().saveFlashmob(f);
	}

	public Flashmob updateFlashmob(ObjectId id, Flashmob flashmob) {
		return getStore().saveFlashmob(getUpdater(Flashmob.class).update(getFlashmob(id), flashmob));
	}
}
