package de.ifgi.fmt.web.servlet.flashmobs.activities;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.SIGNALS_OF_ACTIVITY)
public class SignalServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}/activities/{aid}/signal
	 */

	@GET
	@Produces(MediaTypes.SIGNAL_LIST)
	public List<Signal> getSignals(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity) {
		// @ToDo
		return null;
	}

	@POST
	@Produces(MediaTypes.SIGNAL)
	@Consumes(MediaTypes.SIGNAL)
	// creat a new signal
	public Response setSignal(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity, Signal s) {
		// @ToDo
		Signal saved = getService().addSignal(s, activity, flashmob);
		URI uri = getUriInfo().getBaseUriBuilder()
				.path(Paths.SIGNALS_OF_ACTIVITY).build(s.getId());
		return Response.created(uri).entity(saved).build();
	}

	@PUT
	@Produces(MediaTypes.SIGNAL)
	@Consumes(MediaTypes.SIGNAL)
	public Response updateSignal(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity, Signal s) {
		// @ToDo
		Signal saved = getService().updateSignal(s, activity, flashmob);
		URI uri = getUriInfo().getBaseUriBuilder()
				.path(Paths.SIGNALS_OF_ACTIVITY).build(s.getId());
		return Response.created(uri).entity(saved).build();
	}
}
