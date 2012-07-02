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
package de.ifgi.fmt.web.servlet.users.flashmobs.activities;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Path(Paths.ACTIVITIES_OF_FLASHMOB_OF_USER)
public class ActivitiesServlet extends AbstractServlet {

    /**
     * 
     * @param user
     * @param flashmob
     * @return
     */
    @GET
	@RolesAllowed({ Roles.ADMIN, Roles.USER })
	@Produces(MediaTypes.ACTIVITY_LIST)
	public List<Activity> getActivities(
			@PathParam(PathParams.USER) String user,
			@PathParam(PathParams.FLASHMOB) ObjectId flashmob) {
		if (!isAdminOrUserWithId(user)) {
			throw ServiceError.flashmobNotFound();
		}
		return getService().getActivitiesForUser(user, flashmob);
	}
}
