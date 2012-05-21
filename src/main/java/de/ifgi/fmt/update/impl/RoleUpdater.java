package de.ifgi.fmt.update.impl;

import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.update.EntityUpdater;
import de.ifgi.fmt.update.UpdateFactory.Updates;

@Updates(Role.class)
public class RoleUpdater extends EntityUpdater<Role> {

	@Override
	public Role update(Role old, Role changes) {
		// TODO update role
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
