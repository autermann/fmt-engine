package de.ifgi.fmt.model.task;

import java.net.URI;

import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;

@Polymorphic
public class LinkTask extends Task {

	public static final String LINK = "link";
	public static final String TYPE = "type";

	public enum Type {
		AUDIO, VIDEO, YOUTUBE;
	}

	@Property(LinkTask.TYPE)
	private Type type;

	@Property(LinkTask.LINK)
	private URI link;

	public URI getLink() {
		return link;
	}

	public void setLink(URI link) {
		this.link = link;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
