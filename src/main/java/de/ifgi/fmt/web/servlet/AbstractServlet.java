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
package de.ifgi.fmt.web.servlet;

import java.security.Principal;

import javax.annotation.security.PermitAll;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.ParamException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.Service;
import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.RESTConstants;
import de.ifgi.fmt.web.filter.auth.FmtPrinciple;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@PermitAll
public abstract class AbstractServlet implements RESTConstants {

    /**
     * 
     */
    protected static final String DEFAULT_LIMIT = "20";
	/**
	 * 
	 */
	protected static final String TRUE = "true";
	/**
	 * 
	 */
	protected static final String FALSE = "false";
	/**
	 * 
	 */
	protected static final String ZERO = "0";
	/**
	 * 
	 */
	protected static final String MINUS_ONE = "-1";

	/**
	 * 
	 */
	protected static final Logger log = LoggerFactory	.getLogger(RootServlet.class);
	private static final DateTimeFormatter ISO8601 = ISODateTimeFormat.dateTime();
	
	private final Service service = Service.getInstance();
	private final GeometryFactory geomFactory = new GeometryFactory();

	private @Context UriInfo uriInfo;
	private @Context SecurityContext securityContext;

	/**
	 * 
	 * @param role
	 * @return
	 */
	protected boolean hasRole(String role) {
		if (getSecurityContext() == null) {
			log.warn("No security context available for this request!");
			return false;
		}
		return getSecurityContext().isUserInRole(role);
	}
	
	/**
	 * 
	 * @return
	 */
	protected User getUser() {
		Principal p = getSecurityContext().getUserPrincipal();
		if (p != null && p instanceof FmtPrinciple) {
			return ((FmtPrinciple) p).getUser();
		}
		return null;
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	protected boolean isAdminOrUserWithId(String username) {
		return isAdmin() || (isUser() && getUser().getUsername().equals(username));
	}
	
	/**
	 * 
	 * @return
	 */
	protected boolean isLoggedIn() {
		return isUser() || isAdmin();
	}
	
	/**
	 * 
	 * @return
	 */
	protected boolean isAdmin() {
		return hasRole(Roles.ADMIN);
	}
	/**
	 * 
	 * @return
	 */
	protected boolean isUser() {
		return hasRole(Roles.USER);
	}
	/**
	 * 
	 * @param flashmob
	 */
	protected void isAdminOrCoordinator(ObjectId flashmob) {
		if (!isAdminOrUserWithId(getService().getFlashmob(flashmob).getCoordinator().getUsername())) {
			throw ServiceError.notCoordinator();
		}
	}
	/**
	 * 
	 * @return
	 */
	protected boolean isGuest() {
		return hasRole(Roles.GUEST) || !(isUser() || isAdmin());
	}
	
	/**
	 * 
	 * @return
	 */
	protected SecurityContext getSecurityContext(){
		return securityContext;
	}
	
	/**
	 * 
	 * @return
	 */
	protected UriInfo getUriInfo() {
		return this.uriInfo;
	}

	/**
	 * 
	 * @return
	 */
	protected Service getService() {
		return this.service;
	}

	/**
	 * 
	 * @param time
	 * @param paramName
	 * @return
	 */
	protected DateTime parseDateTime(String time, String paramName) {
		try {
			return ISO8601.parseDateTime(time);
		} catch (Exception e) {
			throw new ParamException.QueryParamException(e, paramName, null);
		}
	}

	/**
	 * 
	 * @param position
	 * @param paramName
	 * @return
	 */
	protected Point parsePoint(String position, String paramName) {
		if (position == null || position.isEmpty()) {
			return null;
		}
		String[] lonLat = position.split(",");
		if (lonLat.length != 2) {
			throw new ParamException.QueryParamException(null, paramName, null);
		}
		double lon = Double.parseDouble(lonLat[0]);
		double lat = Double.parseDouble(lonLat[1]);
		return geomFactory.createPoint(new Coordinate(lon, lat));
	}
}
