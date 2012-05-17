package de.ifgi.fmt.web.servlet.flashmobs.activities.roles;

import de.ifgi.fmt.ServiceError;
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

import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.ROLES_OF_ACTIVITY_OF_FLASHMOB)
public class RolesServlet extends AbstractServlet {
    /*
     * /flashmobs/{fid}/activities/{aid}/roles
     */

    @GET
    @Produces(MediaTypes.ROLE_LIST)
    // get the roles involved in an task
    public List<Role> getRoles(
	    @PathParam(PathParams.FLASHMOB) ObjectId flashmob,
	    @PathParam(PathParams.ACTIVITY) ObjectId activity) {
	return getService().getRoles(activity, flashmob);
    }

    @POST
    @Produces(MediaTypes.ROLE)
    @Consumes(MediaTypes.ROLE)
    // add an role to an activity
    public Response setRole(@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
	    @PathParam(PathParams.ACTIVITY) ObjectId activity, Role r) {
	// @ToDo
	Role saved = getService().addRole(r, activity, flashmob);
	URI uri = getUriInfo().getBaseUriBuilder().path(Paths.ROLE_OF_ACTIVITY_OF_FLASHMOB).build(r.getId());
	return Response.created(uri).entity(saved).build();
    }
}
