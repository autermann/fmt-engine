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

import java.security.Principal;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.RESTConstants.Roles;

public class FmtSecurityContext implements SecurityContext {
	private final User u;
	private @Context UriInfo uri;

	public FmtSecurityContext(User u) {
		this.u = u;
	}

	@Override
	public Principal getUserPrincipal() {
		if (getUser() == null) { 
			return null;
		}
		return new FmtPrinciple(getUser());
	}

	public User getUser() {
		return this.u;
	}
	
	@Override
	public boolean isUserInRole(String r) {
		if (getUser() == null) {
			return r.equalsIgnoreCase(Roles.GUEST);
		}
		return getUser().hasRole(r);
	}
		
	@Override
	public boolean isSecure() {
		return uri.getRequestUri().getScheme().equals("https");
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}
}