package de.ifgi.fmt.web.provider.impl;

import javax.ws.rs.ext.Provider;

import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.web.provider.AbstractJSONProvider;

@Provider
public class RoleEncoder extends AbstractJSONProvider<Role> {

	public RoleEncoder() {
		super(Role.class);
	}

}
