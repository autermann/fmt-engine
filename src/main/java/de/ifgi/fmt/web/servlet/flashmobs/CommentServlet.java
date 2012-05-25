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

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.COMMENT_FOR_FLASHMOB)
public class CommentServlet extends AbstractServlet {

	@GET
	@Produces(MediaTypes.COMMENT)
	public Comment getComment(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.COMMENT) ObjectId comment) {
		return getService().getCommentForFlashmob(flashmob, comment);
	}

	@PUT
	@RolesAllowed({ Roles.USER, Roles.ADMIN })
	@Consumes(MediaTypes.COMMENT)
	@Produces(MediaTypes.COMMENT)
	public Comment changeComment(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.COMMENT) ObjectId comment, Comment changes) {
		if (!isAdminOrUserWithId(getComment(flashmob, comment).getUser().getId())) {
			throw ServiceError.forbidden("You can only change your own comments");
		}
		return getService().updateComment(flashmob, comment, changes);
	}

}
