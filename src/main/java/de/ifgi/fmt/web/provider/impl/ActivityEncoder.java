package de.ifgi.fmt.web.provider.impl;

import javax.ws.rs.ext.Provider;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.web.provider.AbstractJSONProvider;

@Provider
public class ActivityEncoder extends AbstractJSONProvider<Activity> {

	public ActivityEncoder() {
		super(Activity.class);
	}

}
