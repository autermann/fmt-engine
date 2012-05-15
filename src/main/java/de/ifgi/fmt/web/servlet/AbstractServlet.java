package de.ifgi.fmt.web.servlet;

import java.lang.reflect.Method;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.Service;
import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.utils.constants.RESTConstants;

public abstract class AbstractServlet implements RESTConstants {

	protected static final String DEFAULT_LIMIT = "20";

	protected static final Logger log = LoggerFactory.getLogger(Root.class);

	private UriInfo uriInfo;
	private final Service service = Service.getInstance();
	private final GeometryFactory geomFactory = new GeometryFactory();

	protected static Method getMethod(Class<?> c, String name) {
		for (Method m : c.getMethods()) {
			if (name.equals(m.getName())) {
				return m;
			}
		}
		throw ServiceError.internal("No method called " + name);
	}

	@Context
	public void setUriInfo(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	protected UriInfo getUriInfo() {
		return this.uriInfo;
	}

	protected Service getService() {
		return this.service;
	}

	protected Point parsePoint(String position) {
		if (position == null || position.isEmpty()) {
			return null;
		}
		String[] lonLat = position.split(",");
		if (lonLat.length != 2) {
			throw ServiceError.invalidParameter(QueryParams.POSITION);
		}
		double lon = Double.parseDouble(lonLat[0]);
		double lat = Double.parseDouble(lonLat[1]);
		return geomFactory.createPoint(new Coordinate(lon, lat));
	}
}
