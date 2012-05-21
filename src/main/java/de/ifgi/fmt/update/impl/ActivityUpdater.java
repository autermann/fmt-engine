package de.ifgi.fmt.update.impl;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.update.EntityUpdater;
import de.ifgi.fmt.update.UpdateFactory.Updates;

@Updates(Activity.class)
public class ActivityUpdater extends EntityUpdater<Activity> {

	@Override
	public Activity update(Activity old, Activity changes) {
		// TODO update activity
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
