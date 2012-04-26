package de.ifgi.fmt.web.provider.impl;

import javax.ws.rs.ext.Provider;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.web.provider.AbstractJSONListProvider;

@Provider
public class CommentListEncoder extends AbstractJSONListProvider<Activity> {

	public CommentListEncoder() {
		super(Activity.class, JSONConstants.COMMENTS_KEY);
	}

}
