package de.ifgi.fmt.update.impl;

import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.update.EntityUpdater;
import de.ifgi.fmt.update.UpdateFactory.Updates;

@Updates(Comment.class)
public class CommentUpdater extends EntityUpdater<Comment> {

	@Override
	public Comment update(Comment old, Comment changes) {
		// TODO update comment
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
