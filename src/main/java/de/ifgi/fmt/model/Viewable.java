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
package de.ifgi.fmt.model;

import com.google.code.morphia.annotations.Transient;

import de.ifgi.fmt.utils.constants.RESTConstants.View;

public abstract class Viewable<T extends Viewable<T>> {

	@Transient
	private View view;

	public View getView() {
		return this.view;
	}

	public boolean hasView() {
		return this.view != null;
	}
	
	public T optSetView(View view) {
		return setView(hasView() ? getView() : view); 
	}

	@SuppressWarnings("unchecked")
	public T setView(View view) {
		this.view = view;
		return (T) this;
	}

}
