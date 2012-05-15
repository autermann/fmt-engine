package de.ifgi.fmt.web.servlet.flashmob.triggers;

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

@Path(Paths.TRIGGERS_OF_FLASHMOB)
public class FlashmobTriggersServlet extends AbstractServlet {
    /*
     * /flashmobs/{fid}/triggers
     * 
     */

    @GET
    @Produces(MediaTypes.TRIGGER_LIST)
    public List<Trigger> getTriggers(
	    @PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
	//@ToDo
	return null;
    }

    @POST
    @Produces(MediaTypes.TRIGGER)
    @Consumes(MediaTypes.TRIGGER)
    public Response setTrigger(
	    @PathParam(PathParams.FLASHMOB) ObjectId flashmob,
	    Trigger t) {
	//@ToDo
	Trigger saved = getService().addTrigger(t, flashmob);
	URI uri = getUriInfo().getBaseUriBuilder().path(Paths.TRIGGER_OF_FLASHMOB).build(t.getId());
	return Response.created(uri).entity(saved).build();
    }
}
