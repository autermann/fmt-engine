package de.ifgi.fmt.web.servlet.flashmob;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.FLASHMOB)
public class FlashmobServlet extends AbstractServlet {
	/*
	 * /flashmobs/{fid}
	 */

	@GET
	@Produces(MediaTypes.FLASHMOB)
	public Flashmob getFlashmob(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
		return getService().getFlashmob(flashmob);
	}

	@PUT
	@Produces(MediaTypes.FLASHMOB)
	@Consumes(MediaTypes.FLASHMOB)
	// update a specific flashmob
	public Flashmob updateFlashmob(@PathParam(PathParams.FLASHMOB) ObjectId id,
			Flashmob flashmob) {
		return getService().updateFlashmob(id, flashmob);
	}
}
