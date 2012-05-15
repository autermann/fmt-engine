package de.ifgi.fmt.web.servlet.flashmob;

import de.ifgi.fmt.web.servlet.AbstractServlet;

import de.ifgi.fmt.model.Activity;
import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;

@Path(Paths.FLASHMOB)
public class FlashmobServlet extends AbstractServlet {
    /*
     * /flashmobs/{fid}
     * 
     */

    @GET
    @Produces(MediaTypes.FLASHMOB)
    public Flashmob getFlashmob(@PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
	return getService().getFlashmob(flashmob);
    }

    @PUT
    @Produces(MediaTypes.FLASHMOB)
    @Consumes(MediaTypes.FLASHMOB)
    //update a specific flashmob
    public Flashmob updateFlashmob(
	    @PathParam(PathParams.FLASHMOB) ObjectId id,
	    Flashmob flashmob) {
	return getService().updateFlashmob(id, flashmob);
    }
}
