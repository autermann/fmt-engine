package de.ifgi.fmt.web.provider.impl;

import javax.ws.rs.ext.Provider;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.web.provider.AbstractJSONListProvider;

@Provider
public class ActivityListEncoder extends AbstractJSONListProvider<Activity> {

	public ActivityListEncoder() {
		super(Activity.class, JSONConstants.ACTIVITIES_KEY);
	}

}
