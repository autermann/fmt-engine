package de.ifgi.fmt.web.servlet.user.flashmob.activities;

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

@Path(Paths.ACTIVITIES_OF_FLASHMOB_OF_USER)
public class UserFlashmobActivitiesServlet extends AbstractServlet {
    /*
     * /users/{uid}/flashmobs/{fid}/activities
     * 
     */

    @GET
    @Produces(MediaTypes.ACTIVITY_LIST)
    public List<Activity> getActivities(
	    @PathParam(PathParams.USER) ObjectId user,
	    @PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
	//@ToDo
	return null;
    }
}
