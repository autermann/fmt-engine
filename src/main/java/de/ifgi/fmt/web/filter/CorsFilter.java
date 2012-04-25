package de.ifgi.fmt.web.filter;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class CorsFilter implements ContainerResponseFilter {
	private static final String ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	private static final String ALLOW_HEADERS = "Access-Control-Allow-Headers";
	private static final String ALLOW_METHODS = "Access-Control-Allow-Methods";
	private static final String MAX_AGE = "Access-Control-Max-Age";
	private static final int MAX_AGE_VALUE = 3628800;
	private static final String ALLOWED_HEADERS = "Content-Type, Origin";
	private static final String ORIGIN = "Origin";
	private static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS";

	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
		response.getHttpHeaders().add(MAX_AGE, MAX_AGE_VALUE);
		response.getHttpHeaders().add(ALLOW_HEADERS, ALLOWED_HEADERS);
		response.getHttpHeaders().add(ALLOW_METHODS, ALLOWED_METHODS);
		String origin = request.getHeaderValue(ORIGIN);
		response.getHttpHeaders().add(ALLOW_ORIGIN, origin == null ? "*" : origin);
		return response;
	}
}
