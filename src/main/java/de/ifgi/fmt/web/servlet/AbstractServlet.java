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

import java.lang.reflect.Method;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.Service;
import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.utils.constants.RESTConstants;

public abstract class AbstractServlet implements RESTConstants {

	protected static final String DEFAULT_LIMIT = "20";

	protected static final Logger log = LoggerFactory.getLogger(RootServlet.class);

	private UriInfo uriInfo;
	private final Service service = Service.getInstance();
	private final GeometryFactory geomFactory = new GeometryFactory();

	protected static Method getMethod(Class<?> c, String name) {
		for (Method m : c.getMethods()) {
			if (name.equals(m.getName())) {
				return m;
			}
		}
		throw ServiceError.internal("No method called " + name);
	}

	@Context
	public void setUriInfo(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	protected UriInfo getUriInfo() {
		return this.uriInfo;
	}

	protected Service getService() {
		return this.service;
	}

	protected Point parsePoint(String position) {
		if (position == null || position.isEmpty()) {
			return null;
		}
		String[] lonLat = position.split(",");
		if (lonLat.length != 2) {
			throw ServiceError.invalidParameter(QueryParams.POSITION);
		}
		double lon = Double.parseDouble(lonLat[0]);
		double lat = Double.parseDouble(lonLat[1]);
		return geomFactory.createPoint(new Coordinate(lon, lat));
	}
}
