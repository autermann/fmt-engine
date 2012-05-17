/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ifgi.fmt.web.servlet.users;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.USER)
public class UserServlet extends AbstractServlet {
	/*
	 * /users/{uid}
	 */

	@GET
	@Produces(MediaTypes.USER)
	public User getUser(@PathParam(PathParams.USER) ObjectId user) {
		return getService().getUser(user);
	}

	@PUT
	@Produces(MediaTypes.USER)
	@Consumes(MediaTypes.USER)
	public User getUser(@PathParam(PathParams.USER) ObjectId user, User u) {
		// @ToDo
		return getService().updateUser(u, user);
	}

	@DELETE
	public void deleteUser(@PathParam(PathParams.USER) ObjectId user) {
		// @ToDo
		getService().deleteUser(user);
	}
}
