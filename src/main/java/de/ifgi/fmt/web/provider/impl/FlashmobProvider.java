package de.ifgi.fmt.web.provider.impl;

import javax.ws.rs.ext.Provider;

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.web.provider.AbstractJSONProvider;

@Provider
public class FlashmobProvider extends AbstractJSONProvider<Flashmob> {

	public FlashmobProvider() {
		super(Flashmob.class);
	}

}
