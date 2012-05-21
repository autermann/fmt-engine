package de.ifgi.fmt.update.impl;

import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.update.EntityUpdater;
import de.ifgi.fmt.update.UpdateFactory.Updates;

@Updates(Trigger.class)
public class TriggerUpdater extends EntityUpdater<Trigger> {

	@Override
	public Trigger update(Trigger old, Trigger changes) {
		// TODO update trigger
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
