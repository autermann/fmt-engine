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
package de.ifgi.fmt.model.task;

import java.net.URI;

import com.google.code.morphia.annotations.Polymorphic;
import com.google.code.morphia.annotations.Property;

import de.ifgi.fmt.utils.constants.ModelConstants;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Polymorphic
public class LinkTask extends Task {

	/**
     * 
     */
	public enum Type {

		/**
	 * 
	 */
		AUDIO,
		/**
	 * 
	 */
		VIDEO,
		/**
	 * 
	 */
		YOUTUBE;
	}

	@Property(ModelConstants.Task.LINK)
	private URI link;

	@Property(ModelConstants.Task.TYPE)
	private Type type;

	/**
	 * 
	 * @return
	 */
	public URI getLink() {
		return link;
	}

	/**
	 * 
	 * @return
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 
	 * @param link
	 */
	public LinkTask setLink(URI link) {
		this.link = link;
		return this;
	}

	/**
	 * 
	 * @param type
	 */
	public LinkTask setType(Type type) {
		this.type = type;
		return this;
	}

}
