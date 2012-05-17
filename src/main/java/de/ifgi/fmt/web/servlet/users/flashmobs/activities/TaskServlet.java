/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ifgi.fmt.web.servlet.users.flashmobs.activities;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.TASKS_OF_ACTIVITY_OF_FLASHMOB_OF_USER)
public class TaskServlet extends AbstractServlet {
    /*
     * /users/{uid}/flashmobs/{fid}/activities/{aid}/task
     */

    @GET
    @Produces(MediaTypes.TASK_LIST)
    public List<Task> getTasks(@PathParam(PathParams.USER) ObjectId user,
	    @PathParam(PathParams.FLASHMOB) ObjectId flashmob,
	    @PathParam(PathParams.ACTIVITY) ObjectId activity) {
	
	return getService().getTaskForActivity(activity, flashmob, user);
    }
}
