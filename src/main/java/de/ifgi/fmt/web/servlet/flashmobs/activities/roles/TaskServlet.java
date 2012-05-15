package de.ifgi.fmt.web.servlet.flashmobs.activities.roles;

import java.net.URI;
import java.util.List;

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

@Path(Paths.TASKS_OF_ROLE_OF_ACTIVITY_OF_FLASHMOB)
public class TaskServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}/activities/{aid}/roles/{rid}/task
	 */

	@GET
	@Produces(MediaTypes.TASK_LIST)
	public List<Task> getTasks(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity,
			@PathParam(PathParams.ROLE) ObjectId role) {
		// @ToDo
		return null;
	}

	@POST
	@Produces(MediaTypes.TASK)
	@Consumes(MediaTypes.TASK)
	// set the task of an activity o a role
	public Response setTask(@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity,
			@PathParam(PathParams.ROLE) ObjectId role, Task t) {
		// @ToDo
		Task saved = getService().addTask(t, role, activity, flashmob);
		URI uri = getUriInfo().getBaseUriBuilder()
				.path(Paths.TASKS_OF_ROLE_OF_ACTIVITY_OF_FLASHMOB)
				.build(t.getId());
		return Response.created(uri).entity(saved).build();
	}

	@PUT
	@Produces(MediaTypes.TASK)
	@Consumes(MediaTypes.TASK)
	public Task updateTask(@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
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
