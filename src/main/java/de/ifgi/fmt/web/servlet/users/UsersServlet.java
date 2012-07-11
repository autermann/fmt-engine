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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ifgi.fmt.web.servlet.users;

import java.net.URI;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.container.ContainerRequest;

import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.filter.auth.Authorization;
import de.ifgi.fmt.web.servlet.AbstractServlet;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Path(Paths.USERS)
public class UsersServlet extends AbstractServlet {

	/**
	 * 
	 * @param limit
	 * @return
	 */
	@GET
	@PermitAll
	@Produces(MediaTypes.USER_LIST)
	public List<User> getUsers(
			@QueryParam(QueryParams.SEARCH) String search,
			@QueryParam(QueryParams.LIMIT) @DefaultValue(DEFAULT_LIMIT) int limit) {
		if (search != null) {
			return view(View.USERS, getService().getUsers(search));
		} else {
			return view(View.USERS, getService().getUsers(limit));
		}
	}

	/**
	 * 
	 * @param u
	 * @param sr
	 * @return
	 */
	@POST
	@RolesAllowed({ Roles.GUEST, Roles.ADMIN })
	@Produces(MediaTypes.USER)
	@Consumes(MediaTypes.USER)
	public Response createUser(User u, @Context HttpServletRequest sr) {
		User saved = getService().createUser(u);
		if (!isAdmin()) {
			Authorization.auth((ContainerRequest) getSecurityContext(),
					sr, saved);
		}
		URI uri = getUriInfo().getBaseUriBuilder().path(Paths.USER).build(u);
		return Response.created(uri).entity(saved.setView(View.USER)).build();
	}

}
