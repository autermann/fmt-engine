package de.ifgi.fmt.mongo;

import static de.ifgi.fmt.mongo.DaoFactory.getCommentDao;

import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.query.Query;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.mongo.Store.Queries;

public class Comments implements ExtendedDao<Comment>{
	@SuppressWarnings("unused")
	private final Store store;

	Comments(Store store) {
		this.store = store;
	}

	public Comment get(ObjectId id) {
		Store.log.debug("Getting Comment {}", id);
		Comment c = getCommentDao().get(id);
		if (c == null) {
			throw ServiceError.commentNotFound();
		}
		return c;
	}

	public Comment save(Comment c) {
		Store.log.debug("Saving comment {}", c);
		getCommentDao().save(c);
		return c;
	}

	public void save(Iterable<Comment> comments) {
		Store.log.debug("Saving comments");
		for (Comment c : comments) {
			save(c);
		}
	}

	public void delete(Comment c) {
		Store.log.debug("Deleting Comment {}", c);
		getCommentDao().delete(c);
	}

	public void delete(List<Comment> c) {
		Store.log.debug("Deleting Comments");
		getCommentDao().deleteAll(c);
	}

	public List<Comment> get(Flashmob f) {
		return get(Queries.commentsOfFlashmob(f));
	}

	public void delete(Flashmob f) {
		delete(Queries.commentsOfFlashmob(f));
	}

	public List<Comment> get(User u) {
		Store.log.debug("Getting Comments of User {}", u);
		return get(Queries.commentsOfUser(u));
	}

	public List<Comment> get(Query<Comment> q) {
		return getCommentDao().find(Store.g(q)).asList();
	}

	public Comment getOne(Query<Comment> q) {
		return getCommentDao().find(Store.g(q)).get();
	}

	@Override
	public void delete(Iterable<Comment> ts) {
		Store.log.debug("Deleting Comments");
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