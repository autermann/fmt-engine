/*
 * Copyright (C) 2012  Christian Autermann, Dustin Demuth, Maurin Radtke
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package de.ifgi.fmt.mongo.stores;

import static de.ifgi.fmt.mongo.DaoFactory.getCommentDao;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.query.Query;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.mongo.ExtendedDao;
import de.ifgi.fmt.mongo.Store;
import de.ifgi.fmt.mongo.Store.Queries;

public class Comments implements ExtendedDao<Comment>{
	private static final Logger log = LoggerFactory.getLogger(Comments.class);
	@SuppressWarnings("unused")
	private final Store store;

	public Comments(Store store) {
		this.store = store;
	}

	public Comment get(ObjectId id) {
		log.debug("Getting Comment {}", id);
		Comment c = getCommentDao().get(id);
		if (c == null) {
			throw ServiceError.commentNotFound();
		}
		return c;
	}
	
	@Override
	public Comment save(Comment c) {
		log.debug("Saving comment {}", c);
		getCommentDao().save(c);
		return c;
	}

	@Override
	public void save(Collection<Comment> comments) {
		log.debug("Saving comments");
		for (Comment c : comments) {
			save(c);
		}
	}
	
	@Override
	public void delete(Comment c) {
		log.debug("Deleting Comment {}", c);
		getCommentDao().delete(c);
	}

	public void delete(List<Comment> c) {
		log.debug("Deleting Comments");
		getCommentDao().deleteAll(c);
	}

	public List<Comment> get(Flashmob f) {
		return get(Queries.commentsOfFlashmob(f));
	}

	public void delete(Flashmob f) {
		delete(Queries.commentsOfFlashmob(f));
	}

	public List<Comment> get(User u) {
		log.debug("Getting Comments of User {}", u);
		return get(Queries.commentsOfUser(u));
	}
	
	@Override
	public List<Comment> get(Query<Comment> q) {
		return getCommentDao().find(Store.g(q)).asList();
	}

	@Override
	public Comment getOne(Query<Comment> q) {
		return getCommentDao().find(Store.g(q)).get();
	}

	@Override
	public void delete(Collection<Comment> ts) {
		log.debug("Deleting Comments");
		for (Comment c : ts) {
			delete(c);
		}
	}

	@Override
	public List<Comment> get(int limit) {
		return get(all().limit(limit));
	}

	@Override
	public void delete(Query<Comment> q) {
		getCommentDao().deleteByQuery(Store.r(q));
	}

	@Override
	public Query<Comment> all() {
		return getCommentDao().createQuery();
	}
}