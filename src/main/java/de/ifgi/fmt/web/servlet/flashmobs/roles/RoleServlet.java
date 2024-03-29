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

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Path(Paths.ROLE_FOR_FLASHMOB)
public class RoleServlet extends AbstractServlet {

	/**
	 * 
	 * @param flashmob
	 * @param role
	 * @return
	 */
	@GET
	@PermitAll
	@Produces(MediaTypes.ROLE)
	public Role getRoles(@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role) {
		return getService().getRole(flashmob, role).setView(
				View.ROLE_FOR_FLASHMOB);
	}

	/**
	 * 
	 * @param flashmob
	 * @param role
	 * @param r
	 * @return
	 */
	@PUT
	@RolesAllowed({ Roles.USER, Roles.ADMIN })
	@Produces(MediaTypes.ROLE)
	@Consumes(MediaTypes.ROLE)
	public Role updateRole(@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role, Role r) {
		canChangeFlashmob(flashmob);
		return getService().updateRole(r, role, flashmob).setView(
				View.ROLE_FOR_FLASHMOB);
	}

	/**
	 * 
	 * @param flashmob
	 * @param role
	 */
	@DELETE
	@RolesAllowed({ Roles.ADMIN, Roles.USER })
	public void removeRole(@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role) {
		canChangeFlashmob(flashmob);
		getService().removeRoleFromFlashmob(flashmob, role);
	}
}
