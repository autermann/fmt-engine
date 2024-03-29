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
package de.ifgi.fmt.web.servlet.flashmobs.roles.activities;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Signal;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Path(Paths.ACTIVITY_OF_ROLE_OF_FLASHMOB)
public class ActivityServlet extends AbstractServlet {

	/**
	 * 
	 * @param flashmob
	 * @param role
	 * @param activity
	 */
	@DELETE
	@RolesAllowed({ Roles.USER, Roles.ADMIN })
	public void removeActivity(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role,
			@PathParam(PathParams.ACTIVITY) ObjectId activity) {
		canChangeFlashmob(flashmob);
		getService().removeRoleFromActivity(flashmob, activity, role);
	}

	/**
	 * 
	 * @param flashmob
	 * @param role
	 * @param activity
	 * @return
	 */
	@GET
	@PermitAll
	@Produces(MediaTypes.ACTIVITY)
	public Activity getActivity(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role,
			@PathParam(PathParams.ACTIVITY) ObjectId activity) {
		return getService().getActivityForRole(flashmob, role, activity)
				.setView(View.ACTIVITY_OF_ROLE_OF_FLASHMOB);
	}

	/**
	 * 
	 * @param flashmob
	 * @param role
	 * @param activity
	 * @return
	 */
	@GET
	@PermitAll
	@Path(Paths.SIGNAL)
	@Produces(MediaTypes.SIGNAL)
	public Signal getSignals(@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role,
			@PathParam(PathParams.ACTIVITY) ObjectId activity) {
		return getService().getSignal(flashmob, activity).setView(
				View.SIGNAL_OF_ACTIVITY_OF_ROLE_OF_FLASHMOB);
	}
}
