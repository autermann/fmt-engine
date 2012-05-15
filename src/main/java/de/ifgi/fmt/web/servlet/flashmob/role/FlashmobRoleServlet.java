package de.ifgi.fmt.web.servlet.flashmob.role;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.ROLE_FOR_FLASHMOB)
public class FlashmobRoleServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}/roles/{rid}
	 */

	@GET
	@Produces(MediaTypes.ROLE)
	// get a specific role
	public Role getRoles(@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role) {
		// @ToDo
		return getService().getRole(role, flashmob);
	}

	@PUT
	@Path(Paths.ROLE_FOR_FLASHMOB)
	@Produces(MediaTypes.ROLE)
	@Consumes(MediaTypes.ROLE)
	// modify a role
	public Role updateRole(@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId roleID, Role r) {
		// @ToDo
		return getService().updateRole(r, roleID, flashmob);
	}

	@DELETE
	@Path(Paths.ROLE_FOR_FLASHMOB)
	// delete a role
	public Response removeRole(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role) {
		// @ToDo
		return null;
	}
}
