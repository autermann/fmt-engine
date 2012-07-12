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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Signal;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Path(Paths.SIGNAL_OF_ACTIVITY)
public class SignalServlet extends AbstractServlet {

	/**
	 * 
	 * @param flashmob
	 * @param activity
	 * @return
	 */
	@GET
	@PermitAll
	@Produces(MediaTypes.SIGNAL)
	public Signal getSignals(@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity) {
		return getService().getSignal(flashmob, activity).setView(
				View.SIGNAL_OF_ACTIVITY);
	}

	/**
	 * 
	 * @param flashmob
	 * @param activity
	 * @param s
	 * @return
	 */
	@POST
	@RolesAllowed({ Roles.USER, Roles.ADMIN })
	@Produces(MediaTypes.SIGNAL)
	@Consumes(MediaTypes.SIGNAL)
	public Response setSignal(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity, Signal s) {
		canChangeFlashmob(flashmob);
		Signal saved = getService().addSignal(s, activity, flashmob);
		return Response.created(getUriInfo().getRequestUri())
				.entity(saved.setView(View.SIGNAL_OF_ACTIVITY)).build();
	}

	/**
	 * 
	 * @param flashmob
	 * @param activity
	 * @param s
	 * @return
	 */
	@PUT
	@RolesAllowed({ Roles.USER, Roles.ADMIN })
	@Produces(MediaTypes.SIGNAL)
	@Consumes(MediaTypes.SIGNAL)
	public Signal updateSignal(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ACTIVITY) ObjectId activity, Signal s) {
		canChangeFlashmob(flashmob);
		return getService().updateSignal(s, activity, flashmob).setView(
				View.SIGNAL_OF_ACTIVITY);
	}
}
