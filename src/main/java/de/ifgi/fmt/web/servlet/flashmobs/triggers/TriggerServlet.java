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
package de.ifgi.fmt.web.servlet.flashmobs.triggers;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.TRIGGER_OF_FLASHMOB)
public class TriggerServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}/triggers/{tid}
	 */

	@GET
	@Produces(MediaTypes.TRIGGER)
	public Trigger getTrigger(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.TRIGGER) ObjectId trigger) {

		return getService().getTrigger(trigger, flashmob);
	}

	@PUT
	@Produces(MediaTypes.TRIGGER)
	@Consumes(MediaTypes.TRIGGER)
	public Response updateTriggers(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.TRIGGER) ObjectId trigger, Trigger t) {
		// @ToDo
		return null;
	}

	@DELETE
	public Response removeTrigger(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.TRIGGER) ObjectId trigger) {
		// @ToDo
		return null;
	}
}
