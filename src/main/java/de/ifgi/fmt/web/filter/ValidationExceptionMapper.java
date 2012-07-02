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

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.validation.VerboseJSR303ConstraintViolationException;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.utils.constants.RESTConstants.MediaTypes;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Provider
public class ValidationExceptionMapper implements
		ExceptionMapper<VerboseJSR303ConstraintViolationException> {
	private static final Logger log = LoggerFactory
			.getLogger(ValidationExceptionMapper.class);

	/**
	 * 
	 * @param ex
	 * @return
	 */
	@Override
	public Response toResponse(VerboseJSR303ConstraintViolationException ex) {
		try {
			JSONArray a = new JSONArray();
			for (ConstraintViolation<?> cv : ex.getConstraintViolations()) {
				JSONObject e = new JSONObject();
				e.put(JSONConstants.CLASS, cv.getRootBeanClass().getSimpleName());
				e.put(JSONConstants.PROPERTY, cv.getPropertyPath());
				e.put(JSONConstants.VALUE, cv.getInvalidValue());
				e.put(JSONConstants.MESSAGE, cv.getMessage());
				a.put(e);
			}
			JSONObject j = new JSONObject().put(JSONConstants.ERRORS_KEY, a);
			log.warn("Validation: {}", ex.getMessage());
			return Response.status(Status.BAD_REQUEST)
					.type(MediaTypes.ERRORS_TYPE).entity(j).build();
		} catch (JSONException e) {
			throw ServiceError.internal(e);
		}
	}

}
