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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.ClientResponse.Status;

import de.ifgi.fmt.utils.constants.RESTConstants.HeaderParams;

@Provider
public class UnsupportedOperationExceptionMapper implements
		ExceptionMapper<UnsupportedOperationException> {

	private static final Logger log = LoggerFactory
			.getLogger(UnsupportedOperationException.class);

	@Override
	public Response toResponse(UnsupportedOperationException e) {
		StackTraceElement elem = e.getStackTrace()[0];
		log.warn("NOT YET IMPLEMENTED: {}", elem);
		return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.header(HeaderParams.CONTENT_TYPE, MediaType.TEXT_PLAIN)
				.entity("Method " + elem + " not yet implemented")
				.build();
	}

}
