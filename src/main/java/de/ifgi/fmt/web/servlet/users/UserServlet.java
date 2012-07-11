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

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.sun.jersey.spi.container.ContainerRequest;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.filter.auth.Authorization;
import de.ifgi.fmt.web.servlet.AbstractServlet;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Path(Paths.USER)
public class UserServlet extends AbstractServlet {

	/**
	 * 
	 * @param user
	 * @return
	 */
	@GET
	@PermitAll
	@Produces(MediaTypes.USER)
	public User getUser(@PathParam(PathParams.USER) String user) {
		User u = getService().getUser(user);
		if (u == null) {
			throw ServiceError.userNotFound();
		}
		return u.setView(View.USER);
	}

	/**
	 * 
	 * @param userName
	 * @param changes
	 * @return
	 */
	@PUT
	@RolesAllowed({ Roles.USER, Roles.ADMIN })
	@Produces(MediaTypes.USER)
	@Consumes(MediaTypes.USER)
	public User updateUser(@PathParam(PathParams.USER) String userName,
			User changes) {

		if (!isAdminOrUserWithId(userName)) {
			throw ServiceError.forbidden("can only change yourself");
		}

		return getService().updateUser(changes, userName).setView(View.USER);
	}

	/**
	 * 
	 * @param user
	 * @param sr
	 */
	@DELETE
	@RolesAllowed({ Roles.USER, Roles.ADMIN })
	public void deleteUser(@PathParam(PathParams.USER) String user,
			@Context HttpServletRequest sr) {

		if (!isAdminOrUserWithId(user)) {
			throw ServiceError.forbidden("can only delete yourself");
		}

		getService().deleteUser(user);

		// do not log out the admin...
		if (isUser()) {
			Authorization.deauth(
					(ContainerRequest) getSecurityContext(), sr);
		}
	}
}
