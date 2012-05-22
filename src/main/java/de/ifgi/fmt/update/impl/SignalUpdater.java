/*
 * Copyright (C) 2012  Christian Autermann, Dustin Demuth, Maurin Radtke
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package de.ifgi.fmt.update.impl;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.model.signal.SoundSignal;
import de.ifgi.fmt.model.signal.TextSignal;
import de.ifgi.fmt.model.signal.VibrationSignal;
import de.ifgi.fmt.update.EntityUpdater;
import de.ifgi.fmt.update.UpdateFactory.Updates;

@Updates(Signal.class)
public class SignalUpdater extends EntityUpdater<Signal> {

	@Override
	public Signal update(Signal old, Signal changes) {
		if (old instanceof SoundSignal) {
			if (!(changes instanceof SoundSignal)) {
				throw ServiceError.badRequest("mismatching signal types");
			}
			SoundSignal ss = (SoundSignal) changes;
			if (ss.getLink() != null) {
				((SoundSignal) old).setLink(ss.getLink());
			}
		} else if (old instanceof VibrationSignal) {
			if (!(changes instanceof VibrationSignal)) {
				throw ServiceError.badRequest("mismatching signal types");
			}
		} else  if (old instanceof TextSignal) {
			if (!(changes instanceof TextSignal)) {
				throw ServiceError.badRequest("mismatching signal types");
			}
			TextSignal ts = (TextSignal) changes;
			if (ts.getText() != null) {
				((TextSignal) old).setText(ts.getText());
			}
		}
		return old;
	}
}
