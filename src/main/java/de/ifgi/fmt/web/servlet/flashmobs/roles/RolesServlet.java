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
package de.ifgi.fmt.web.servlet.flashmobs.roles;

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

import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Path(Paths.ROLES_FOR_FLASHMOB)
public class RolesServlet extends AbstractServlet {

	/**
	 * 
	 * @param flashmob
	 * @return
	 */
	@GET
	@PermitAll
	@Produces(MediaTypes.ROLE_LIST)
	public List<Role> getRoles(@PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
		return view(View.ROLES_FOR_FLASHMOB, getService().getRoles(flashmob));
	}

	/**
	 * 
	 * @param flashmob
	 * @param r
	 * @return
	 */
	@POST
	@RolesAllowed({ Roles.USER, Roles.ADMIN })
	@Produces(MediaTypes.ROLE)
	@Consumes(MediaTypes.ROLE)
	public Response addRole(@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			Role r) {
		canChangeFlashmob(flashmob);
		Role saved = getService().addRole(flashmob, r);
		URI uri = getUriInfo().getBaseUriBuilder()
				.path(Paths.ROLE_FOR_FLASHMOB).build(flashmob, r);
		return Response.created(uri)
				.entity(saved.setView(View.ROLE_FOR_FLASHMOB)).build();
	}
}
