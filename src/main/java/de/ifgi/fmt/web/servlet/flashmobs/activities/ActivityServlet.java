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
package de.ifgi.fmt.web.servlet.flashmobs.activities;

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

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Path(Paths.ACTIVITY_OF_FLASHMOB)
public class ActivityServlet extends AbstractServlet {

	/**
	 * 
	 * @param flashmob
	 * @param activity
	 * @return
	 */
	@GET
	@PermitAll
	@Produces(MediaTypes.ACTIVITY)
	public Activity getActivity(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity) {
		return getService().getActivity(flashmob, activity).setView(
				View.ACTIVITY_OF_FLASHMOB);
	}

	/**
	 * 
	 * @param flashmob
	 * @param activity
	 * @param a
	 * @return
	 */
	@PUT
	@RolesAllowed({ Roles.USER, Roles.ADMIN })
	@Produces(MediaTypes.ACTIVITY)
	@Consumes(MediaTypes.ACTIVITY)
	public Activity updateActivity(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity, Activity a) {
		canChangeFlashmob(flashmob);
		return getService().updateActivity(a, activity, flashmob).setView(
				View.ACTIVITY_OF_FLASHMOB);
	}

	/**
	 * 
	 * @param flashmob
	 * @param activity
	 */
	@DELETE
	@RolesAllowed({ Roles.USER, Roles.ADMIN })
	public void deleteActivity(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity) {
		canChangeFlashmob(flashmob);
		getService().deleteActivity(flashmob, activity);
	}
}
