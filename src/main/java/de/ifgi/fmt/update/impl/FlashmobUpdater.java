package de.ifgi.fmt.update.impl;

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.update.EntityUpdater;
import de.ifgi.fmt.update.UpdateFactory.Updates;

@Updates(Flashmob.class)
public class FlashmobUpdater extends EntityUpdater<Flashmob>{

	@Override
	public Flashmob update(Flashmob old, Flashmob changes) {
		if (changes.getDescription() != null) {
			old.setDescription(changes.getDescription());
		}
		if (changes.getPublish() != null) {
			old.setPublish(changes.getPublish());
		}
		if (changes.getEnd() != null) {
			old.setEnd(changes.getEnd());
		}
		if (changes.getStart() != null) {
			old.setEnd(changes.getEnd());
		}
		// TODO Auto-generated method stub
		return null;
	}

}
