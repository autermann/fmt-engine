package de.ifgi.fmt.web.servlet.flashmobs.activities;

import de.ifgi.fmt.ServiceError;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;
import java.net.URI;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path(Paths.ACTIVITY_OF_FLASHMOB)
public class ActivityServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}/activities/{aid}
	 */

	@GET
	@Produces(MediaTypes.ACTIVITY)
	// get a specific activity
	public Activity getActivity(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity) {
                
		return getService().getActivity(flashmob, activity);
	}

	@PUT
	@Produces(MediaTypes.ACTIVITY)
	@Consumes(MediaTypes.ACTIVITY)
	// change the activity
	public Activity updateActivity(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity, Activity a) {
		// @ToDo
		return getService().updateActivity(a, activity, flashmob);
	}
}
