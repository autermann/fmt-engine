package de.ifgi.fmt.web.provider.impl;

import javax.ws.rs.ext.Provider;

import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.web.provider.AbstractJSONListProvider;

@Provider
public class RoleListEncoder extends AbstractJSONListProvider<Role> {

	public RoleListEncoder() {
		super(Role.class, JSONConstants.ROLES_KEY);
	}

}
