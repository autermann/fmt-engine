/*
 * Copyright (C) 2012  Christian Autermann, Dustin Demuth, Maurin Radtke
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package de.ifgi.fmt.model.signal;

import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;

import de.ifgi.fmt.ServiceError;
import de.ifgi.fmt.utils.constants.JSONConstants;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Polymorphic
public class SoundSignal extends Signal {

    /**
     * 
     */
    public static final String LINK = "link";

	@Property(SoundSignal.LINK)
	private URL link;

	/**
	 * 
	 * @param j
	 * @return
	 */
	@Override
	public Signal decode(JSONObject j) {
		String l = j.optString(JSONConstants.HREF_KEY, null);
		if (l != null) {
			try {
				setLink(new URL(l));
			} catch (MalformedURLException e) {
				throw ServiceError.badRequest(e);
			}
		}
		return this;
	}

	/**
	 * 
	 * @param j
	 * @return
	 * @throws JSONException
	 */
	@Override
	public Signal encode(JSONObject j) throws JSONException {
		if (getLink() != null) {
			j.put(JSONConstants.HREF_KEY, getLink());
		}
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public URL getLink() {
		return link;
	}

	/**
	 * 
	 * @param link
	 */
	public void setLink(URL link) {
		this.link = link;
	}

}
