package de.ifgi.fmt.model;

import java.util.Set;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.mongo.Identifiable;
import de.ifgi.fmt.utils.Utils;

@Polymorphic
@Entity(Role.COLLECTION_NAME)
public class Role extends Identifiable {

	public static final String COLLECTION_NAME = "roles";
	public static final String ITEMS = "items";
	public static final String USERS = "users";
	public static final String MIN_COUNT = "minCount";
	public static final String MAX_COUNT = "maxCount";
	public static final String START_POINT = "startPoint";
	public static final String CATEGORY = "category";
	public static final String DESCRIPTION = "description";
	public static final String FLASHMOB = "flashmob";
	public static final String ACTIVITIES = "activities";

	public enum Category {
		EASY, HARD, ULTRA;
	}

	@Property(Role.ITEMS)
	private Set<String> items;
	
	@Property(Role.MIN_COUNT)
	private int minCount;
	
	@Property(Role.MAX_COUNT)
	private int maxCount;
	
	@Property(Role.START_POINT)
	private Point startPoint;
	
	@Property(Role.CATEGORY)
	private Category category;
	
	@Property(Role.DESCRIPTION)
	private String description;

	@Reference(Role.ACTIVITIES)
	private Set<Activity> activites;
	
	@Reference(Role.FLASHMOB)
	private Flashmob flashmob;
	
	@Reference(Role.USERS)
	private Set<User> users = Utils.set();

	public Set<String> getItems() {
		return items;
	}

	public Role setItems(Set<String> items) {
		this.items = items;
		return this;
	}

	public Set<User> getUsers() {
		return users;
	}

	public Role setUsers(Set<User> users) {
		this.users = users;
		return this;
	}
	
	public Role addUser(User u) {
		getUsers().add(u);
		return this;
	}
	
	public Role removeUser(User u) {
		getUsers().remove(u);
		return this;
	}

	public int getMinCount() {
		return minCount;
	}

	public Role setMinCount(int minCount) {
		this.minCount = minCount;
		return this;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public Role setMaxCount(int maxCount) {
		this.maxCount = maxCount;
		return this;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public Role setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
		return this;
	}

	public Category getCategory() {
		return category;
	}

	public Role setCategory(Category category) {
		this.category = category;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Role setDescription(String description) {
		this.description = description;
		return this;
	}

	public Flashmob getFlashmob() {
		return flashmob;
	}

	public Role setFlashmob(Flashmob flashmob) {
		this.flashmob = flashmob;
		return this;
	}

	public Set<Activity> getActivities() {
		return activites;
	}

	public Role setActivities(Set<Activity> activities) {
		this.activites = activities;
		return this;
	}

	public Role addAcitivity(Activity activity) {
		getActivities().add(activity);
		return this;
	}

	public Role removeActivity(Activity activity) {
		getActivities().remove(activity);
		return this;
	}

}
