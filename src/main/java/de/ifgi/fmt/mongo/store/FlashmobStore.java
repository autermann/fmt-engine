package de.ifgi.fmt.mongo.store;

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.mongo.MongoDB.MongoDao;

public class FlashmobStore {

	protected MongoDao<Flashmob> fmDao = MongoDao.get(Flashmob.class);
	
	
}
