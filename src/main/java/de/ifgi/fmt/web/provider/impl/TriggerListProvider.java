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
package de.ifgi.fmt.web.provider.impl;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.utils.constants.RESTConstants.MediaTypes;
import de.ifgi.fmt.web.provider.AbstractJSONListProvider;

@Provider
@Produces(MediaTypes.TRIGGER_LIST)
public class TriggerListProvider extends AbstractJSONListProvider<Trigger> {

	public TriggerListProvider() {
		super(Trigger.class, JSONConstants.TRIGGERS_KEY, MediaTypes.TRIGGER_LIST_TYPE);
	}

}
