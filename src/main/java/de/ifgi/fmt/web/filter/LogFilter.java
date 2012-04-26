package de.ifgi.fmt.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class LogFilter implements ContainerRequestFilter {
	private static final Logger log = LoggerFactory.getLogger(LogFilter.class);
	
	@Override
	public ContainerRequest filter(ContainerRequest request) {
		log.debug("{} {}", request.getMethod(), request.getRequestUri());
		return request;
	}

}
