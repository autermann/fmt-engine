package de.ifgi.fmt.web.servlet.flashmobs.activities;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.ACTIVITIES_OF_FLASHMOB)
public class ActivitiesServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}/activities
	 */

	@GET
	@Produces(MediaTypes.ACTIVITY_LIST)
	// get the activities of a flashmob
	public List<Activity> getActivities(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
		// @ToDo
		return null;
	}

	@POST
	@Produces(MediaTypes.ACTIVITY)
	@Consumes(MediaTypes.ACTIVITY)
	// add an activity to a flashmob
	public Response addActivity(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob, Activity a) {
		// @ToDo
		Activity saved = getService().addActivity(a, flashmob);
		URI uri = getUriInfo().getBaseUriBuilder()
				.path(Paths.ACTIVITY_OF_FLASHMOB).build(a.getId());
		return Response.created(uri).entity(saved).build();
	}
}
