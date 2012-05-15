package de.ifgi.fmt.web.servlet.flashmob.activity.role;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.ROLE_OF_ACTIVITY_OF_FLASHMOB)
public class FlashmobActivityRoleServlet extends AbstractServlet {

	/*
	 * /flashmobs/{fid}/activities/{aid}/roles/{rid}
	 */
	@DELETE
	// remove a role from an activity
	public Response removeRole(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity,
			@PathParam(PathParams.ROLE) ObjectId role) {
		// @ToDo
		return null;
	}
}
