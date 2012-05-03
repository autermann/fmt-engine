package de.ifgi.fmt.model.task;

import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;
import com.vividsolutions.jts.geom.LineString;

@Polymorphic
public class LineTask extends Task {
	
	private static final String LINE = "line";
	
	@Property(LineTask.LINE)
	private LineString line;

	public LineString getLine() {
		return line;
	}

	public void setLine(LineString line) {
		this.line = line;
	}
}