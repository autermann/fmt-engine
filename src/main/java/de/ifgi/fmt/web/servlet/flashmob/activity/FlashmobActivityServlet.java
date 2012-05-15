package de.ifgi.fmt.web.servlet.flashmob.activity;

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

@Path(Paths.ACTIVITY_OF_FLASHMOB)
public class FlashmobActivityServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}/activities/{aid}
	 */

	@GET
	@Produces(MediaTypes.ACTIVITY)
	// get a specific activity
	public Activity getActivity(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity) {
		// @ToDo
		return null;
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
