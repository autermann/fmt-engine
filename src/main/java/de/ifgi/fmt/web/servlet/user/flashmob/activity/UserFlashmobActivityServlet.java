/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ifgi.fmt.web.servlet.user.flashmob.activity;

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
import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.model.signal.Signal;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.ACTIVITY_OF_FLASHMOB_OF_USER)
public class UserFlashmobActivityServlet extends AbstractServlet {
    /*
     * /users/{uid}/flashmobs/{fid}/activities/{aid}
     * 
     */

    @GET
    @Produces(MediaTypes.ACTIVITY)
    public Activity getActivity(
	    @PathParam(PathParams.USER) ObjectId user,
	    @PathParam(PathParams.FLASHMOB) ObjectId flashmob,
	    @PathParam(PathParams.ACTIVITY) ObjectId activity){
	//@ToDo
	return getActivity(activity, user, flashmob);
    }


}