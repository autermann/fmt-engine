package de.ifgi.fmt.update.impl;

import de.ifgi.fmt.model.User;
import de.ifgi.fmt.update.EntityUpdater;
import de.ifgi.fmt.update.UpdateFactory.Updates;

@Updates(User.class)
public class UserUpdater extends EntityUpdater<User> {

	@Override
	public User update(User old, User changes) {
		// TODO update user
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
