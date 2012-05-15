package de.ifgi.fmt.web.servlet.flashmobs.activities;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bson.types.ObjectId;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.TRIGGER_OF_ACTIVITY)
public class TriggerServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}/activities/{aid}/triggers/{tid}
	 */

	@GET
	@Produces(MediaTypes.TRIGGER)
	public Response getTrigger(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.TRIGGER) ObjectId trigger,
			@PathParam(PathParams.ACTIVITY) ObjectId activity) {
		if (!getService().getActivity(flashmob, activity).getTrigger().getId()
				.equals(activity)) {
			throw ServiceError.triggerNotFound();
		}
		URI redirect = getUriInfo().getBaseUriBuilder()
				.path(Paths.TRIGGER_OF_FLASHMOB).build(flashmob, trigger);
		return Response.status(Status.TEMPORARY_REDIRECT).location(redirect)
				.build();
	}
	
	@POST
	@Produces(MediaTypes.TRIGGER)
	@Consumes(MediaTypes.TRIGGER)
	public Response setTrigger(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity, Trigger t) {
		// @ToDo
		Trigger saved = getService().addTrigger(t, activity, flashmob);
		URI uri = getUriInfo().getBaseUriBuilder()
				.path(Paths.TRIGGER_OF_ACTIVITY).build(t.getId());
		return Response.created(uri).entity(saved).build();
	}

	@DELETE
	public void removeTrigger(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.TRIGGER) ObjectId trigger,
			@PathParam(PathParams.ACTIVITY) ObjectId activity) {
		// ToDo
	}
}
