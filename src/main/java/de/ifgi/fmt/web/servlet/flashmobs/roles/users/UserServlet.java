package de.ifgi.fmt.web.servlet.flashmobs.roles.users;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.USERS_OF_ROLE_OF_FLASHMOB)
public class UserServlet extends AbstractServlet {

	/*
	 * /flashmobs/{fid}/roles/{rid}/users/{uid}
	 */
	@DELETE
	// Unregister a User from a role
	public Response unregisterUser(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role,
			@PathParam(PathParams.USER) ObjectId user) {
		// @ToDo
		return null;
	}
}
