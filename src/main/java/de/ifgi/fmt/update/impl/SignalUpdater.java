package de.ifgi.fmt.update.impl;

import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.update.EntityUpdater;
import de.ifgi.fmt.update.UpdateFactory.Updates;

@Updates(Signal.class)
public class SignalUpdater extends EntityUpdater<Signal> {

	@Override
	public Signal update(Signal old, Signal changes) {
		// TODO update signal
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
