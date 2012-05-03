package de.ifgi.fmt.model.trigger;

import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;

@Polymorphic
public class TimeTrigger extends Trigger {

	public static final String TIME = "time";

	@Property(TimeTrigger.TIME)
	private DateTime time;

	public DateTime getTime() {
		return time;
	}

	public void setTime(DateTime time) {
		this.time = time;
	}
}
