/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ifgi.fmt.web.servlet.users;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.USERS)
public class UsersServlet extends AbstractServlet {
	/*
	 * /users
	 */
	@GET
	@Produces(MediaTypes.USER_LIST)
	public List<User> getUsers(
			@QueryParam(QueryParams.LIMIT) @DefaultValue(DEFAULT_LIMIT) int limit) {
		return getService().getUsers(limit);
	}

	@POST
	@Produces(MediaTypes.USER)
	@Consumes(MediaTypes.USER)
	public Response setUser(User u) {
		User saved = getService().createUser(u);
		URI uri = getUriInfo().getBaseUriBuilder().path(Paths.USER)
				.build(u.getId());
		return Response.created(uri).entity(saved).build();
	}

}
