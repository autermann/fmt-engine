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
package de.ifgi.fmt.web.filter.auth;

import java.util.Collections;
import java.util.List;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

public class RolesAllowedFilterFactory implements ResourceFilterFactory {

	private @Context
	SecurityContext sc;

	private class Filter implements ResourceFilter, ContainerRequestFilter {
		private final boolean denyAll;
		private final String[] rolesAllowed;

		protected Filter() {
			this.denyAll = true;
			this.rolesAllowed = null;
		}

		protected Filter(String[] rolesAllowed) {
			this.denyAll = false;
			this.rolesAllowed = (rolesAllowed != null) ? rolesAllowed : new String[] {};
		}

		@Override public ContainerRequestFilter getRequestFilter() { return this; }
		@Override public ContainerResponseFilter getResponseFilter() { return null; }

		@Override
		public ContainerRequest filter(ContainerRequest request) {
			if (!denyAll) {
				for (String role : rolesAllowed) {
					if (sc.isUserInRole(role))
						return request;
				}
			}
			if (sc.getUserPrincipal() == null) {
				throw new WebApplicationException(Response.Status.UNAUTHORIZED);
			}
			throw new WebApplicationException(Response.Status.FORBIDDEN);
		}
	}

	@Override
	public List<ResourceFilter> create(AbstractMethod am) {
		if (am.isAnnotationPresent(DenyAll.class))
			return Collections.<ResourceFilter> singletonList(new Filter());
		RolesAllowed ra = am.getAnnotation(RolesAllowed.class);
		if (ra != null)
			return Collections.<ResourceFilter> singletonList(new Filter(ra
					.value()));
		if (am.isAnnotationPresent(PermitAll.class))
			return null;
		ra = am.getResource().getAnnotation(RolesAllowed.class);
		if (ra != null)
			return Collections.<ResourceFilter> singletonList(new Filter(ra
					.value()));
		return null;
	}
}
