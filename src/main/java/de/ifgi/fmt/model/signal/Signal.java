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

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Polymorphic;

import de.ifgi.fmt.mongo.Identifiable;

@Polymorphic
@Entity(Signal.COLLECTION_NAME)
public abstract class Signal extends Identifiable {
	
	public static final String COLLECTION_NAME = "signals";

	public Signal(ObjectId id) {
		super(id);
	}

	public Signal(String id) {
		super(id);
	}

	public Signal() {
		super();
	}
	
	public String getType() {
		return getClass().getName()
				.replace(getClass().getPackage().getName() + ".", "")
				.replace("Signal", "");
	}
	
	public abstract Signal encode(JSONObject j) throws JSONException;
	public abstract Signal decode(JSONObject j);

}
