package de.ifgi.fmt.model;

import java.util.List;

import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.mongo.Identifiable;
import de.ifgi.fmt.utils.Utils;

@Polymorphic
@Entity(Flashmob.COLLECTION_NAME)
public class Flashmob extends Identifiable {

	public static final String COLLECTION_NAME = "flashmobs";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String START = "start";
	public static final String END = "end";
	public static final String PUBLISH = "publish";
	public static final String PUBLIC = "isPublic";
	public static final String LOCATION = "location";
	public static final String KEY = "key";
	public static final String ACTIVITIES = "activities";
	public static final String COMMENTS = "comments";
	public static final String ROLES = "roles";
	public static final String TRIGGERS = "triggers";

	@Property(Flashmob.TRIGGERS)
	private List<Trigger> triggers = Utils.list();

	@Property(Flashmob.COMMENTS)
	private List<Comment> comments = Utils.list();

	@Property(Flashmob.TITLE)
	private String title;

	@Property(Flashmob.DESCRIPTION)
	private String description;

	@Property(Flashmob.START)
	private DateTime start;

	@Property(Flashmob.END)
	private DateTime end;

	@Property(Flashmob.PUBLISH)
	private DateTime publish;

	@Property(Flashmob.PUBLIC)
	private boolean isPublic;

	@Property(Flashmob.LOCATION)
	private Point location;

	@Property(Flashmob.KEY)
	private String key;

	@Property(Flashmob.ACTIVITIES)
	private List<Activity> activities = Utils.list();

	@Property(Flashmob.ROLES)
	private List<Role> roles = Utils.list();

	public String getDescription() {
		return description;
	}

	public Flashmob setTitle(String title) {
		this.title = title;
		return this;
	}

	public Flashmob setStart(DateTime start) {
		this.start = start;
		return this;
	}

	public Flashmob setEnd(DateTime end) {
		this.end = end;
		return this;
	}

	public Flashmob setPublish(DateTime publish) {
		this.publish = publish;
		return this;
	}

	public Flashmob setPublic(boolean isPublic) {
		this.isPublic = isPublic;
		return this;
	}

	public Flashmob setLocation(Point location) {
		this.location = location;
		return this;
	}

	public Flashmob setKey(String key) {
		this.key = key;
		return this;
	}

	public Flashmob setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public DateTime getStart() {
		return start;
	}

	public DateTime getEnd() {
		return end;
	}

	public DateTime getPublish() {
		return publish;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public Point getLocation() {
		return location;
	}

	public String getKey() {
		return key;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public Flashmob setActivities(List<Activity> activities) {
		this.activities = activities;
		return this;
	}
	
	public Flashmob addActivity(Activity activity) {
		getActivities().add(activity.setFlashmob(this));
		return this;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public Flashmob setComments(List<Comment> comments) {
		this.comments = comments;
		return this;
	}

	public Flashmob addComment(Comment comment) {
		getComments().add(comment.setFlashmob(this));
		return this;
	}

	public List<Role> getRoles() {
		return this.roles;
	}

	public Flashmob setRoles(List<Role> roles) {
		this.roles = roles;
		return this;
	}

	public Flashmob addRole(Role role) {
		getRoles().add(role.setFlashmob(this));
		return this;
	}

	public List<Trigger> getTriggers() {
		return triggers;
	}

	public Flashmob setTriggers(List<Trigger> triggers) {
		this.triggers = triggers;
		return this;
	}

	public Flashmob addTrigger(Trigger trigger) {
		getTriggers().add(trigger.setFlashmob(this));
		return this;
	}

}
