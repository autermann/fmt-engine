package de.ifgi.fmt.mongo.store;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.mongo.MongoDB.MongoDao;

public class FlashmobStore {

	protected MongoDao<Flashmob> fmDao = MongoDao.get(Flashmob.class);
	protected MongoDao<Activity> acDao = MongoDao.get(Activity.class);
	protected MongoDao<Role> roDao = MongoDao.get(Role.class);
	protected MongoDao<Trigger> trDao = MongoDao.get(Trigger.class);
	
	public Flashmob getFlashmob(ObjectId id) {
		return this.fmDao.get(id);
	}

	public void save(Flashmob f) {
		this.fmDao.save(f);
	}
}
