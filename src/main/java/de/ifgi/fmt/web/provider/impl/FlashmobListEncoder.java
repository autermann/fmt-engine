package de.ifgi.fmt.web.provider.impl;

import javax.ws.rs.ext.Provider;

import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.web.provider.AbstractJSONListProvider;

@Provider
public class FlashmobListEncoder extends AbstractJSONListProvider<Flashmob> {

	public FlashmobListEncoder() {
		super(Flashmob.class, JSONConstants.FLASHMOBS_KEY);
	}

}
