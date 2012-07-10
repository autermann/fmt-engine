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
package de.ifgi.fmt.web.servlet.flashmobs.roles.users;

import java.net.URI;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Path(Paths.USERS_OF_ROLE_OF_FLASHMOB)
public class UsersServlet extends AbstractServlet {

    /**
     * 
     * @param flashmob
     * @param role
     * @param limit
     * @return
     */
    @GET
    @PermitAll
	@Produces(MediaTypes.USER_LIST)
	public List<User> getUsers(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role,
			@QueryParam(QueryParams.LIMIT) @DefaultValue(DEFAULT_LIMIT) int limit) {
		return view(View.USERS_OF_ROLE_OF_FLASHMOB, getService().getUsersForRole(flashmob, role, limit));
	}

	/**
	 * 
	 * @param flashmob
	 * @param role
	 * @param u
	 * @return
	 */
	@POST
	@RolesAllowed({ Roles.USER, Roles.ADMIN })
	@Produces(MediaTypes.USER)
	@Consumes(MediaTypes.USER)
	public Response registerUser(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role, User u) {
		if (!isAdminOrUserWithId(u.getUsername())) {
			throw ServiceError.forbidden("can only be done by the user himself");
		}
		User saved = getService().registerUser(u, role, flashmob);
		URI uri = getUriInfo().getBaseUriBuilder()
				.path(Paths.USER_OF_ROLE_OF_FLASHMOB)
				.build(flashmob, role, saved);
		return Response.created(uri).entity(saved.setView(View.USER_OF_ROLE_OF_FLASHMOB)).build();
	}
}
