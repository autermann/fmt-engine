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

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Path(Paths.ACTIVITIES_OF_ROLE_OF_FLASHMOB)
public class ActivitiesServlet extends AbstractServlet {

    /**
     * 
     * @param flashmob
     * @param role
     * @return
     */
    @GET
	@Produces(MediaTypes.ACTIVITY_LIST)
	public List<Activity> getActivitiesOfRole(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role) {
		return getService().getActivitiesForRole(role, flashmob);
	}

    /**
     * 
     * @param flashmob
     * @param role
     * @param a
     * @return
     */
    @POST
	@Produces(MediaTypes.ACTIVITY)
	@Consumes(MediaTypes.ACTIVITY)
	public Response setActivityOfRole(
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob,
			@PathParam(PathParams.ROLE) ObjectId role, Activity a) {
		Activity saved = getService().addRoleToActivity(a, role, flashmob);
		URI uri = getUriInfo().getBaseUriBuilder()
				.path(Paths.ACTIVITY_OF_ROLE_OF_FLASHMOB)
				.build(flashmob, role, a.getId());
		return Response.created(uri).entity(saved).build();
	}
}
