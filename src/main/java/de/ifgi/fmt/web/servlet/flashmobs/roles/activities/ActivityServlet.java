package de.ifgi.fmt.web.servlet.flashmobs.roles.activities;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.ACTIVITY_OF_ROLE_OF_FLASHMOB)
public class ActivityServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}/roles/{rid}/acitivities/{aid}
	 */

	@DELETE
	// remove an activity from a role
	public Response removeActivity(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role,
			@PathParam(PathParams.ACTIVITY) ObjectId activity) {
		// @ToDo
		return null;
	}
}
