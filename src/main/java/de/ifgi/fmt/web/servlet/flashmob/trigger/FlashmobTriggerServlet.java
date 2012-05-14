package de.ifgi.fmt.web.servlet.flashmob.trigger;

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

@Path(Paths.TRIGGER_OF_FLASHMOB)
public class FlashmobTriggerServlet extends AbstractServlet {
    /*
     * /flashmobs/{fid}/triggers/{tid}
     * 
     */

    @GET
    @Produces(MediaTypes.TRIGGER_LIST)
    public List<Trigger> getTrigger(
            @PathParam(PathParams.FLASHMOB) ObjectId flashmob,
            @PathParam(PathParams.TRIGGER) ObjectId trigger
            ) {
        //@ToDo
        return null;
    }

    @PUT
    @Produces(MediaTypes.TRIGGER)
    @Consumes(MediaTypes.TRIGGER)
    public Response updateTriggers(
            @PathParam(PathParams.FLASHMOB) ObjectId flashmob,
            @PathParam(PathParams.TRIGGER) ObjectId trigger,
            Trigger t) {
        //@ToDo
        return null;
    }

    @DELETE
    public Response removeTrigger(
            @PathParam(PathParams.FLASHMOB) ObjectId flashmob,
            @PathParam(PathParams.TRIGGER) ObjectId trigger) {
        //@ToDo
        return null;
    }
}
