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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import de.ifgi.fmt.utils.constants.RESTConstants.HeaderParams;

public class LogFilter implements ContainerRequestFilter {
	private static final Logger log = LoggerFactory.getLogger(LogFilter.class);

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder("Logging request")
				.append(request.getMethod())
				.append(" ")
				.append(request.getRequestUri());
			appendHeaders(request, sb, HeaderParams.CONTENT_TYPE, HeaderParams.AUTHORIZATION, HeaderParams.COOKIE);
			log.debug(sb.toString());
		}
		return request;
	}

	private void appendHeaders(ContainerRequest rq, StringBuilder sb, String... headers) {
		for (String s : headers) {
			String val = rq.getHeaderValue(s);
			if (val != null) {
				sb.append("\n\t").append(s).append(": ").append(val);
			}
		}
	}
}
