package de.ifgi.fmt.model;

import java.util.Set;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.mongo.Identifiable;

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

	public enum Category {
		EASY, HARD, ULTRA;
	}
	
	@Property(Role.ITEMS)
	private Set<String> items;

	@Property(Role.USERS)
	private Set<User> users;
	
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

	public Set<String> getItems() {
		return items;
	}

	public void setItems(Set<String> items) {
		this.items = items;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public int getMinCount() {
		return minCount;
	}

	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
