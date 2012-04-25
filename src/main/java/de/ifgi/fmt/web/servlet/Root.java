package de.ifgi.fmt.web.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.utils.Utils;

@Path("/")
public class Root extends AbstractServlet {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getRoot() throws Exception {
		log.debug("Getting Root: GET {}", getUriInfo().getRequestUri());
		return Utils.toString(new JSONObject());
	}

}
