/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ifgi.fmt.web.servlet.users.flashmobs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.FLASHMOB_OF_USER)
public class FlashmobServlet extends AbstractServlet {
	/*
	 * /users/{uid}/flashmobs/{fid}
	 */

	@GET
	@Produces(MediaTypes.FLASHMOB)
	public User getFlashmob(@PathParam(PathParams.USER) ObjectId user,
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
		// @ToDo
		return getService().getFlashmob(user, flashmob);
	}
}
