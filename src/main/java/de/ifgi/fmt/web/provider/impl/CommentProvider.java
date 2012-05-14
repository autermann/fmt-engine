package de.ifgi.fmt.web.provider.impl;

import javax.ws.rs.ext.Provider;

import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.web.provider.AbstractJSONProvider;

@Provider
public class CommentProvider extends AbstractJSONProvider<Comment> {

	public CommentProvider() {
		super(Comment.class);
	}

}
