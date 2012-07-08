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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;

import de.ifgi.fmt.utils.constants.JSONConstants;
import de.ifgi.fmt.utils.constants.ModelConstants;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Polymorphic
public class TextSignal extends Signal {

	@Property(ModelConstants.Signal.TEXT)
	private String text;

	/**
	 * 
	 * @param j
	 * @return
	 */
	@Override
	public Signal decode(JSONObject j) {
		setText(j.optString(JSONConstants.TEXT_KEY, null));
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
		if (getText() != null) {
			j.put(JSONConstants.TEXT_KEY, getText());
		}
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

}
