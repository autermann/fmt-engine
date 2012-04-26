package de.ifgi.fmt.web.servlet;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Flashmob;

public class FlashmobServlet extends AbstractServlet {

	@GET
	@Path(Paths.FLASHMOBS)
	public List<Flashmob> getFlashmobs(@QueryParam(QueryParams.LIMIT) int limit,
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
	public Flashmob getFlashmob(@PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
		return null;
	}

}
