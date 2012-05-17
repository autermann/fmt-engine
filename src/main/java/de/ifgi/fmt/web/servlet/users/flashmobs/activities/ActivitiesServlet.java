package de.ifgi.fmt.web.servlet.users.flashmobs.activities;

import de.ifgi.fmt.ServiceError;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.ACTIVITIES_OF_FLASHMOB_OF_USER)
public class ActivitiesServlet extends AbstractServlet {
    /*
     * /users/{uid}/flashmobs/{fid}/activities
     */

    @GET
    @Produces(MediaTypes.ACTIVITY_LIST)
    public List<Activity> getActivities(
	    @PathParam(PathParams.USER) ObjectId user,
	    @PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
	
	if (!getService().getUser(user).getFlashmobs().getId().equals(flashmob)) {
	    throw ServiceError.flashmobNotFound();
	}
	return getService().getActivitiesForUser(user, flashmob);
    }
}
