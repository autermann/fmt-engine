package de.ifgi.fmt.web.provider.impl;

import javax.ws.rs.ext.Provider;

import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.web.provider.AbstractJSONListProvider;

@Provider
public class UserListEncoder extends AbstractJSONListProvider<User> {

	public UserListEncoder() {
		super(User.class, JSONConstants.USERS_KEY);
	}

}
