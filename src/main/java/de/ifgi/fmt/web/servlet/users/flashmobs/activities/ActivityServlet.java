/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ifgi.fmt.web.servlet.users.flashmobs.activities;

import de.ifgi.fmt.ServiceError;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.web.servlet.AbstractServlet;

@Path(Paths.ACTIVITY_OF_FLASHMOB_OF_USER)
public class ActivityServlet extends AbstractServlet {
    /*
     * /users/{uid}/flashmobs/{fid}/activities/{aid}
     */

    @GET
    @Produces(MediaTypes.ACTIVITY)
    public Activity getActivity(@PathParam(PathParams.USER) ObjectId user,
	    @PathParam(PathParams.FLASHMOB) ObjectId flashmob,
	    @PathParam(PathParams.ACTIVITY) ObjectId activity) {

	if (!getService().getUser(user).getFlashmobs().getId().equals(flashmob)) {
	    throw ServiceError.flashmobNotFound();
	}
	
	if (!getService().getUser(user).getFlashmob(flashmob).getActivities().getId().equals(activity)) {
	    throw ServiceError.activityNotFound();
	}
	return getActivity(activity, user, flashmob);
    }
}
