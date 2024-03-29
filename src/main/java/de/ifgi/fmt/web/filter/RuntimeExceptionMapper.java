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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ifgi.fmt.ServiceError;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<Throwable> {
	private static final Logger log = LoggerFactory
			.getLogger(RuntimeExceptionMapper.class);

	/**
	 * 
	 * @param t
	 * @return
	 */
	@Override
	public Response toResponse(Throwable t) {
		if (t instanceof WebApplicationException) {
			log.warn("Mapping Exception", t);
			return ((WebApplicationException) t).getResponse();
		}

		Throwable e = t;
		while (e != null) {
			if (e instanceof ServiceError) {
				return new ServiceErrorMapper().toResponse((ServiceError) e);
			}
			e = e.getCause();
		}

		log.info("Mapping Exception", t);
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			t.printStackTrace(new PrintStream(out));
			return Response.serverError().entity(new String(out.toByteArray()))
					.type(MediaType.TEXT_PLAIN).build();
		} catch (Throwable t2) {
			throw new RuntimeException(t);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
}
