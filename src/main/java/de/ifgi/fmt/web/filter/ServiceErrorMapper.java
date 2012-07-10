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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.utils.constants.RESTConstants.MediaTypes;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Provider
public class ServiceErrorMapper implements ExceptionMapper<ServiceError> {
	private static final Logger log = LoggerFactory
			.getLogger(ServiceErrorMapper.class);

	/**
	 * 
	 * @param e
	 * @return
	 */
	@Override
	public Response toResponse(ServiceError e) {
		if (e.getMessage() != null) {
			try {
				log.info("Mapping Exception: HTTP {}: {}", e.getErrorCode(),
						e.getMessage());
				JSONObject j = new JSONObject().put(JSONConstants.ERRORS_KEY,
						new JSONArray().put(new JSONObject().put(
								JSONConstants.MESSAGE, e.getMessage())));
				return Response.status(e.getErrorCode()).entity(j)
						.type(MediaTypes.ERRORS).build();
			} catch (JSONException e1) {
				throw ServiceError.internal(e);
			}
		} else {
			log.warn("Mapping Exception", e);
			ByteArrayOutputStream out = null;
			try {
				out = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(out));
				return Response.status(e.getErrorCode())
						.entity(new String(out.toByteArray()))
						.type(MediaType.TEXT_PLAIN).build();
			} catch (Throwable t) {
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(out);
			}

		}

	}

}
