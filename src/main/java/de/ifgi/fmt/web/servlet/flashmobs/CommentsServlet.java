package de.ifgi.fmt.web.servlet.flashmobs;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.COMMENTS_FOR_FLASHMOB)
public class CommentsServlet extends AbstractServlet {

	@GET
	@Produces(MediaTypes.COMMENT_LIST)
	public List<Comment> getComments(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
		return getService().getCommentsForFlashmob(flashmob);
	}
	
	@POST
	@Consumes(MediaTypes.COMMENT)
	@Produces(MediaTypes.COMMENT)
	public Response addComment(@PathParam(PathParams.FLASHMOB) ObjectId flashmob, Comment comment) {
		Comment c = getService().addComment(flashmob, comment);
		URI uri = getUriInfo().getBaseUriBuilder().path(Paths.COMMENT_FOR_FLASHMOB).build(flashmob,c.getId());
		return Response.created(uri).entity(c).build();
	}
}
