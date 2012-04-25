package de.ifgi.fmt.web.filter;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sun.jersey.api.ParamException.QueryParamException;

@Provider
public class QueryParamExceptionMapper implements
		ExceptionMapper<QueryParamException> {

	@Override
	public Response toResponse(QueryParamException e) {
		return Response.fromResponse(e.getResponse())
				.status(Status.BAD_REQUEST).build();
	}

}
