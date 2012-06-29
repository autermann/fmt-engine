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
package de.ifgi.fmt.web.servlet.users.flashmobs;

import java.net.URI;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.ROLE_OF_USER_IN_FLASHMOB)
public class RoleServlet extends AbstractServlet {

	@GET
	@RolesAllowed({ Roles.ADMIN, Roles.USER })
	@Produces(MediaTypes.ROLE)
	public Response getRoles(@PathParam(PathParams.USER) String user,
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
		isAdminOrUserWithId(user);
		Role r = getService().getRoleOfUserInFlashmob(flashmob, user);
		URI uri = getUriInfo().getBaseUriBuilder()
				.path(Paths.ROLE_FOR_FLASHMOB).build(flashmob, r.getId());
		return Response.seeOther(uri).build();
	}
}
