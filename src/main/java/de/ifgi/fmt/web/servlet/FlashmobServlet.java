package de.ifgi.fmt.web.servlet;

import java.net.URI;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Flashmob;

public class FlashmobServlet extends AbstractServlet {

	@GET
	@Path(Paths.FLASHMOBS)
	@Produces(MediaTypes.FLASHMOB_LIST)
	public List<Flashmob> getFlashmobs(
			@QueryParam(QueryParams.LIMIT) int limit,
			@QueryParam(QueryParams.POSITION) String near,
			@QueryParam(QueryParams.USER) ObjectId user,
			@QueryParam(QueryParams.BBOX) String bbox,
			@QueryParam(QueryParams.FROM) String from,
			@QueryParam(QueryParams.TO) String to,
			@QueryParam(QueryParams.SORT) Sorting sorting,
			@QueryParam(QueryParams.DESCENDING) boolean descending,
			@QueryParam(QueryParams.SHOW) ShowStatus show,
			@QueryParam(QueryParams.SEARCH) String search,
			@QueryParam(QueryParams.PARTICIPANT) ObjectId participant) {
		return null;
	}

	@GET
	@Path(Paths.FLASHMOB)
	@Produces(MediaTypes.FLASHMOB)
	public Flashmob getFlashmob(@PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
		return getService().getFlashmob(flashmob);
	}
	
	
	@PUT
	@Path(Paths.FLASHMOB)
	@Produces(MediaTypes.FLASHMOB)
	public Flashmob updateFlashmob(@PathParam(PathParams.FLASHMOB) ObjectId id, Flashmob flashmob) {
		return getService().updateFlashmob(id, flashmob);
	}

	@POST
	@Path(Paths.FLASHMOBS)
	@Produces(MediaTypes.FLASHMOB)
	public Response createFlashmob(Flashmob f) {
		Flashmob saved = getService().createFlashmob(f);
		URI uri = getUriInfo().getBaseUriBuilder().path(Paths.FLASHMOB).build(f.getId());
		return Response.created(uri).entity(saved).build();
	}
	
}
