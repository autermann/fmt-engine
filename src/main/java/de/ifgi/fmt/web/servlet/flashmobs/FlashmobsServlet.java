package de.ifgi.fmt.web.servlet.flashmobs;

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

@Path(Paths.FLASHMOBS)
public class FlashmobsServlet extends AbstractServlet {

    /*
     * /flashmobs
     * 
     */
    @GET
    @Produces(MediaTypes.FLASHMOB_LIST)
    //get a list of flashmobs
    public List<Flashmob> getFlashmobs(
            @QueryParam(QueryParams.LIMIT) int limit,
            @QueryParam(QueryParams.POSITION) String near,
            @QueryParam(QueryParams.USER) ObjectId user,
            @QueryParam(QueryParams.BBOX) String bbox,
            @QueryParam(QueryParams.FROM) String from,
            @QueryParam(QueryParams.TO) String to,
            @QueryParam(QueryParams.SORT) Sorting sorting,
            @QueryParam(QueryParams.DESCENDING) boolean descending,
            @QueryParam(QueryParams.SHOW) ShowStatus show,
            @QueryParam(QueryParams.SEARCH) String search,
            @QueryParam(QueryParams.PARTICIPANT) ObjectId participant) {
        return null;
    }

    @POST
    @Produces(MediaTypes.FLASHMOB)
    @Consumes(MediaTypes.FLASHMOB)
    //create a new flashmob
    public Response setFlashmob(Flashmob f) {
        Flashmob saved = getService().createFlashmob(f);
        URI uri = getUriInfo().getBaseUriBuilder().path(Paths.FLASHMOB).build(f.getId());
        return Response.created(uri).entity(saved).build();
    }
}
