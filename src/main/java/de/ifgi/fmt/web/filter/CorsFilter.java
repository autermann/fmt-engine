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
package de.ifgi.fmt.web.filter;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
public class CorsFilter implements ContainerResponseFilter {
	private static final String ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	private static final String ALLOW_HEADERS = "Access-Control-Allow-Headers";
	private static final String ALLOW_METHODS = "Access-Control-Allow-Methods";
	private static final String MAX_AGE = "Access-Control-Max-Age";
	private static final int MAX_AGE_VALUE = 3628800;
	private static final String ALLOWED_HEADERS = "Content-Type, Origin, Accept, Set-Cookie, Cookie, Authorization";
	private static final String ORIGIN = "Origin";
	private static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS";

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ContainerResponse filter(ContainerRequest request,
			ContainerResponse response) {
		response.getHttpHeaders().add(MAX_AGE, MAX_AGE_VALUE);
		response.getHttpHeaders().add(ALLOW_HEADERS, ALLOWED_HEADERS);
		response.getHttpHeaders().add(ALLOW_METHODS, ALLOWED_METHODS);
		String origin = request.getHeaderValue(ORIGIN);
		response.getHttpHeaders().add(ALLOW_ORIGIN,
				origin == null ? "*" : origin);
		return response;
	}
}
