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
package de.ifgi.fmt.web.servlet.flashmobs.activities.roles;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.TASK_OF_ROLE_OF_ACTIVITY_OF_FLASHMOB)
public class TaskServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}/activities/{aid}/roles/{rid}/task
	 */

	@GET
	@Produces(MediaTypes.TASK)
	public Task getTasks(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity,
			@PathParam(PathParams.ROLE) ObjectId role) {

		return getService().getTask(flashmob, role, activity);
	}

	@POST
	@Produces(MediaTypes.TASK)
	@Consumes(MediaTypes.TASK)
	// set the task of an activity o a role
	public Response setTask(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity,
			@PathParam(PathParams.ROLE) ObjectId role, Task t) {
		// @ToDo
		Task saved = getService().addTask(t, role, activity, flashmob);
		URI uri = getUriInfo().getBaseUriBuilder()
				.path(Paths.TASK_OF_ROLE_OF_ACTIVITY_OF_FLASHMOB)
				.build(flashmob, activity, role);
		return Response.created(uri).entity(saved).build();
	}

	@PUT
	@Produces(MediaTypes.TASK)
	@Consumes(MediaTypes.TASK)
	public Task updateTask(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity,
			@PathParam(PathParams.ROLE) ObjectId role, Task t) {
		// @ToDo
		return getService().updateTask(t, role, activity, flashmob);
	}

	@DELETE
	// Delete a Task from a role in an activity
	public Response removeTask(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role,
			@PathParam(PathParams.ACTIVITY) ObjectId activity) {
		// @ToDo
		return null;
	}
}
