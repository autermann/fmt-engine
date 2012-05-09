package de.ifgi.fmt.model.signal;

import java.net.URL;

import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;

@Polymorphic
public class SoundSignal extends Signal {

	public static final String LINK = "link";
	
	@Property(SoundSignal.LINK)
	private URL link;

	public URL getLink() {
		return link;
	}

	public void setLink(URL link) {
		this.link = link;
	}
}
