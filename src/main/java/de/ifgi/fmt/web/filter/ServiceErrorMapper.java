package de.ifgi.fmt.web.filter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ifgi.fmt.ServiceError;

@Provider
public class ServiceErrorMapper implements ExceptionMapper<ServiceError> {
	private static final Logger log = LoggerFactory
			.getLogger(ServiceErrorMapper.class);

	@Override
	public Response toResponse(ServiceError e) {
		if (e.getMessage() != null) {
			log.info("Mapping Exception: HTTP {}: {}", e.getErrorCode(),e.getMessage());
			return Response.status(e.getErrorCode())
					.entity(e.getMessage()).type(MediaType.TEXT_PLAIN)
					.build();
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
