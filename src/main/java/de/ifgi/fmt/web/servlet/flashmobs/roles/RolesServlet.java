package de.ifgi.fmt.web.servlet.flashmobs.roles;

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

@Path(Paths.ROLES_FOR_FLASHMOB)
public class RolesServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}/roles
	 */

	@GET
	@Produces(MediaTypes.ROLE_LIST)
	// get list of roles for a flashmob
	public List<Role> getRoles(@PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
		return getService().getRoles(flashmob);
	}

	@POST
	@Produces(MediaTypes.ROLE)
	@Consumes(MediaTypes.ROLE)
	// create a new role for a flashmob
	public Response addRole(@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			Role r) {
		// @ToDo
		Role saved = getService().addRole(flashmob, r);
		URI uri = getUriInfo().getBaseUriBuilder()
				.path(Paths.ROLE_FOR_FLASHMOB).build(r.getId());
		return Response.created(uri).entity(saved).build();
	}
}
