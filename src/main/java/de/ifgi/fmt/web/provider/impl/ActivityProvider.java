package de.ifgi.fmt.web.provider.impl;

import javax.ws.rs.ext.Provider;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.web.provider.AbstractJSONProvider;

@Provider
public class ActivityProvider extends AbstractJSONProvider<Activity> {

	public ActivityProvider() {
		super(Activity.class);
	}

}
