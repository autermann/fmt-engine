package de.ifgi.fmt.mongo.store;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.User;
import de.ifgi.fmt.mongo.MongoDB.MongoDao;

public class UserStore {

	protected MongoDao<User> uDao = MongoDao.get(User.class);
	
	
	public void saveUser(User u) {
		this.uDao.save(u);
	}
	
	public User getUser(ObjectId id) {
		return this.uDao.get(id);
	}
	
	public void deleteUser(User u) {
		//TODO cascade
		this.uDao.delete(u);
	}
}
