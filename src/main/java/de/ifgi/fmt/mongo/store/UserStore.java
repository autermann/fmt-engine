package de.ifgi.fmt.mongo.store;

import de.ifgi.fmt.model.User;
import de.ifgi.fmt.mongo.MongoDB.MongoDao;

public class UserStore {

	protected MongoDao<User> uDao = MongoDao.get(User.class);
}
