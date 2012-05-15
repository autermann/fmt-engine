package de.ifgi.fmt.web.servlet.flashmob.role.activity.task;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.TASKS_OF_ACTIVITY_OF_ROLE_OF_FLASHMOB)
public class FlashmobRoleActivityTaskServlet extends AbstractServlet {
    /*
     * /flashmobs/{fid}/roles/{rid}/acitivities/{aid}/task
     * 
     */

    @GET
    @Produces(MediaTypes.TASK_LIST)
    //get the task of a role in an activity
    public List<Task> getTasks(
	    @PathParam(PathParams.FLASHMOB) ObjectId flashmob,
	    @PathParam(PathParams.ROLE) ObjectId role,
	    @PathParam(PathParams.ACTIVITY) ObjectId activity) {
	//@ToDo
	return null;
    }

    @POST
    @Produces(MediaTypes.TASK)
    @Consumes(MediaTypes.TASK)
    //set the task of a role in an activity
    public Response setTask(
	    @PathParam(PathParams.FLASHMOB) ObjectId flashmob,
	    @PathParam(PathParams.ROLE) ObjectId role,
	    @PathParam(PathParams.ACTIVITY) ObjectId activity,
	    Task t) {
	//@ToDo
	Task saved = getService().addTask(t, role, activity, flashmob);
	URI uri = getUriInfo().getBaseUriBuilder().path(Paths.TASKS_OF_ACTIVITY_OF_ROLE_OF_FLASHMOB).build(t.getId());
	return Response.created(uri).entity(saved).build();
    }

    @PUT
    @Produces(MediaTypes.TASK)
    @Consumes(MediaTypes.TASK)
    //change the task of a role in an activity
    public Task updateTask(
	    @PathParam(PathParams.FLASHMOB) ObjectId flashmob,
	    @PathParam(PathParams.ROLE) ObjectId role,
	    @PathParam(PathParams.ACTIVITY) ObjectId activity,
	    Task t) {
	//@ToDo
	return getService().updateTask(t, role, activity, flashmob);
    }

    @DELETE
    //Delete a Task from a role in an activity
    public Response removeTask(
	    @PathParam(PathParams.FLASHMOB) ObjectId flashmob,
	    @PathParam(PathParams.ROLE) ObjectId role,
	    @PathParam(PathParams.ACTIVITY) ObjectId activity) {
	//@ToDo
	return null;
    }
}