package de.ifgi.fmt.web.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONObject;

import de.ifgi.fmt.utils.Utils;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;

@Path(Paths.ROOT)
public class RootServlet extends AbstractServlet {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getRoot() throws Exception {
		JSONObject j = new JSONObject()
			.put(JSONConstants.USERS_KEY, getUriInfo().getBaseUriBuilder().path(Paths.USERS))
			.put(JSONConstants.FLASHMOBS_KEY, getUriInfo().getBaseUriBuilder().path(Paths.FLASHMOBS));
		return Utils.toString(j);
	}

}
