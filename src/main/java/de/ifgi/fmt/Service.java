package de.ifgi.fmt;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.mongo.store.FlashmobStore;

public class Service {

	private static Service service;

	public static Service getInstance() {
		return (service == null) ? service = new Service() : service;
	}
	
	private FlashmobStore fmStore = new FlashmobStore();

	public FlashmobStore getFlashmobStore() {
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
		getFlashmobStore().save(f);
		return f;
	}
}
