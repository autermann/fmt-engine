/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ifgi.fmt.web.servlet.users.flashmobs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.FLASHMOBS_OF_USER)
public class FlashmobsServlet extends AbstractServlet {
	/*
	 * /users/{uid}/flashmobs
	 */

	@GET
	@Produces(MediaTypes.FLASHMOB_LIST)
	public List<Flashmob> getFlashmobs(@PathParam(PathParams.USER) ObjectId user) {
		// @ToDo
		return null;
	}

}
