package de.ifgi.fmt.web.servlet.flashmobs.users;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.USERS_OF_FLASHMOB)
public class UsersServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}/users
	 */

	@GET
	@Produces(MediaTypes.USER_LIST)
	// get the participants of a flashmob
	public List<User> getUsers(@PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
		return getService().getUsersOfFlashmob();
	}
}
