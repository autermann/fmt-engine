package de.ifgi.fmt.web.servlet.flashmob.role.user;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.USERS_OF_ROLE_OF_FLASHMOB)
public class FlashmobRoleUserServlet extends AbstractServlet {

    /*
     * /flashmobs/{fid}/roles/{rid}/users/{uid}
     * 
     */
    @DELETE
    //Unregister a User from a role
    public Response unregisterUser(
            @PathParam(PathParams.FLASHMOB) ObjectId flashmob,
            @PathParam(PathParams.ROLE) ObjectId role,
            @PathParam(PathParams.USER) ObjectId user) {
        //@ToDo
        return null;
    }
}
