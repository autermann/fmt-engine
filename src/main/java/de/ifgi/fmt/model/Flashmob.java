package de.ifgi.fmt.model;

import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.mongo.Identifiable;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
}
