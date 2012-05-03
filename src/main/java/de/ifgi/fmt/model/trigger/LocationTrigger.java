package de.ifgi.fmt.model.trigger;

import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;
import com.vividsolutions.jts.geom.Point;

@Polymorphic
public class LocationTrigger extends Trigger {

	public static final String LOCATION = "location";

	@Property(LocationTrigger.LOCATION)
	private Point location;

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
}
