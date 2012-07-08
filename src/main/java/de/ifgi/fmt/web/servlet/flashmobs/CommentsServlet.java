/*
 * Copyright (C) 2012  Christian Autermann, Dustin Demuth, Maurin Radtke
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package de.ifgi.fmt.web.servlet.flashmobs;

import java.net.URI;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Path(Paths.COMMENTS_FOR_FLASHMOB)
public class CommentsServlet extends AbstractServlet {

    /**
     * 
     * @param flashmob
     * @return
     */
    @GET
    @PermitAll
	@Produces(MediaTypes.COMMENT_LIST)
	public List<Comment> getComments(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
		return getService().getCommentsForFlashmob(flashmob);
	}
	
	/**
	 * 
	 * @param flashmob
	 * @param comment
	 * @return
	 */
	@POST
	@RolesAllowed({ Roles.USER, Roles.ADMIN })
	@Consumes(MediaTypes.COMMENT)
	@Produces(MediaTypes.COMMENT)
	public Response addComment(@PathParam(PathParams.FLASHMOB) ObjectId flashmob, Comment comment) {
		Comment c = getService().addComment(flashmob, comment.setUser(getUser()));
		URI uri = getUriInfo().getBaseUriBuilder().path(Paths.COMMENT_FOR_FLASHMOB).build(flashmob, c.getId());
		return Response.created(uri).entity(c).build();
	}
}
