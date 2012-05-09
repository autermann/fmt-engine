package de.ifgi.fmt.model.signal;

import com.google.code.morphia.annotations.Property;

public class TextSignal extends Signal {

	public static final String TEXT = "text";

	@Property(TextSignal.TEXT)
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
