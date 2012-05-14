package de.ifgi.fmt.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import de.ifgi.fmt.utils.constants.RESTConstants;

public class LogFilter implements ContainerRequestFilter {
	private static final Logger log = LoggerFactory.getLogger(LogFilter.class);

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		if (log.isDebugEnabled()) {
			String ct = request
					.getHeaderValue(RESTConstants.HeaderParams.CONTENT_TYPE);
			if (ct != null) {
				log.debug("{} {}\n\tContent-Type: {}", new Object[] { 
						request.getMethod(), request.getRequestUri(), ct });
			} else {
				log.debug("{} {}", request.getMethod(), request.getRequestUri());
			}
		}
		return request;
	}

}
