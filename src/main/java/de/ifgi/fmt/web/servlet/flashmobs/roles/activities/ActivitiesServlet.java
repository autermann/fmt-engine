package de.ifgi.fmt.web.servlet.flashmobs.roles.activities;

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

@Path(Paths.ACTIVITIES_OF_ROLE_OF_FLASHMOB)
public class ActivitiesServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}/roles/{rid}/acitivities
	 */

	@GET
	@Produces(MediaTypes.ACTIVITY_LIST)
	// get the activities a role is involved in
	public List<Activity> getActivities(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role) {
		// @ToDo
		return null;
	}

	@POST
	@Produces(MediaTypes.ACTIVITY)
	@Consumes(MediaTypes.ACTIVITY)
	// add a role to a activity
	public Response setActivity(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role, Activity a) {
		// @ToDo
		Activity saved = getService().addActivity(a, role, flashmob);
		URI uri = getUriInfo().getBaseUriBuilder()
				.path(Paths.ACTIVITY_OF_ROLE_OF_FLASHMOB).build(a.getId());
		return Response.created(uri).entity(saved).build();
	}
}