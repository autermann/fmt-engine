package de.ifgi.fmt.web.provider.impl;

import javax.ws.rs.ext.Provider;

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.web.provider.AbstractJSONListProvider;

@Provider
public class FlashmobListProvider extends AbstractJSONListProvider<Flashmob> {

	public FlashmobListProvider() {
		super(Flashmob.class, JSONConstants.FLASHMOBS_KEY);
	}

}
