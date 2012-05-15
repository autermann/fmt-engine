package de.ifgi.fmt.web.servlet.flashmob.role.users;

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

import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.USERS_OF_ROLE_OF_FLASHMOB)
public class FlashmobRoleUsersServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}/roles/{rid}/users
	 */

	@GET
	@Produces(MediaTypes.USER_LIST)
	// get a list of users in a specific role
	public List<User> getUsers(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role) {
		// @ToDo
		return null;
	}

	@POST
	@Produces(MediaTypes.USER)
	@Consumes(MediaTypes.USER)
	// Register a User for a Role
	public Response registerUser(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role, User u) {
		// @ToDo
		User saved = getService().registerUser(u, role, flashmob);
		URI uri = getUriInfo().getBaseUriBuilder()
				.path(Paths.USER_OF_ROLE_OF_FLASHMOB).build(u.getId());
		return Response.created(uri).entity(saved).build();
	}
}
