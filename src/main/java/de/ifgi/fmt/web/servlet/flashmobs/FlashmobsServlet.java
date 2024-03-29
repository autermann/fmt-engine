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
package de.ifgi.fmt.web.servlet.flashmobs;

import java.net.URI;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.model.BoundingBox;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Path(Paths.FLASHMOBS)
public class FlashmobsServlet extends AbstractServlet {

	/**
	 * 
	 * @param limit
	 * @param near
	 * @param user
	 * @param bbox
	 * @param from
	 * @param to
	 * @param sorting
	 * @param descending
	 * @param show
	 * @param search
	 * @param minParticipants
	 * @param maxParticipants
	 * @param participant
	 * @return
	 */
	@GET
	@PermitAll
	@Produces(MediaTypes.FLASHMOB_LIST)
	public List<Flashmob> getFlashmobs(
			@QueryParam(QueryParams.LIMIT) int limit,
			@QueryParam(QueryParams.POSITION) String near,
			@QueryParam(QueryParams.USER) String user,
			@QueryParam(QueryParams.BBOX) BoundingBox bbox,
			@QueryParam(QueryParams.FROM) String from,
			@QueryParam(QueryParams.TO) String to,
			@QueryParam(QueryParams.SHOW) ShowStatus show,
			@QueryParam(QueryParams.SEARCH) String search,
			@QueryParam(QueryParams.MIN_PARTICIPANTS) @DefaultValue(ZERO) int minParticipants,
			@QueryParam(QueryParams.MAX_PARTICIPANTS) @DefaultValue(MINUS_ONE) int maxParticipants,
			@QueryParam(QueryParams.PARTICIPANT) String participant) {

		DateTime t = null;
		if (to != null) {
			t = parseDateTime(to, QueryParams.TO);
		}
		DateTime f = null;
		if (from != null) {
			f = parseDateTime(from, QueryParams.FROM);
		}
		Point point = null;
		if (near != null) {
			point = parsePoint(near, QueryParams.POSITION);
		}
		return view(
				View.FLASHMOBS,
				getService().getFlashmobs(limit, point, user, bbox, f, t, show,
						search, participant, minParticipants, maxParticipants));
	}

	/**
	 * 
	 * @param f
	 * @return
	 */
	@POST
	@RolesAllowed({ Roles.USER, Roles.ADMIN })
	@Produces(MediaTypes.FLASHMOB)
	@Consumes(MediaTypes.FLASHMOB)
	public Response createFlashmob(Flashmob f) {
		User u = getUser();
		if (u != null && f.getCoordinator() == null) {
			f.setCoordinator(u);
		}
		Flashmob saved = getService().createFlashmob(f);
		URI uri = getUriInfo().getBaseUriBuilder().path(Paths.FLASHMOB)
				.build(f);
		return Response.created(uri).entity(saved.setView(View.FLASHMOB))
				.build();
	}
}
