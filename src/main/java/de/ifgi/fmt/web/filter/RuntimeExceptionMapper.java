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

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<Throwable> {
	private static final Logger log = LoggerFactory
			.getLogger(RuntimeExceptionMapper.class);

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
