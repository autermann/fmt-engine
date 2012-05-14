package de.ifgi.fmt.web.provider.impl;

import javax.ws.rs.ext.Provider;

import de.ifgi.fmt.model.User;
import de.ifgi.fmt.web.provider.AbstractJSONProvider;

@Provider
public class UserProvider extends AbstractJSONProvider<User> {

	public UserProvider() {
		super(User.class);
	}

}
