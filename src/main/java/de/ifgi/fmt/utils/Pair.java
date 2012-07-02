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
package de.ifgi.fmt.utils;

/**
 * 
 * @author Autermann, Demuth, Radtke
 * @param <U>
 * @param <V>
 */
public class Pair<U, V> {

	private U u;
	private V v;

	/**
	 * 
	 * @param u
	 * @param v
	 */
	public Pair(U u, V v) {
		setOne(u);
		setTwo(v);
	}

	/**
	 * 
	 */
	public Pair() {
	}

	/**
	 * 
	 * @return
	 */
	public U getOne() {
		return u;
	}

	/**
	 * 
	 * @return
	 */
	public V getTwo() {
		return v;
	}

	/**
	 * 
	 * @param u
	 * @return
	 */
	public Pair<U, V> setOne(U u) {
		this.u = u;
		return this;
	}

	/**
	 * 
	 * @param v
	 * @return
	 */
	public Pair<U, V> setTwo(V v) {
		this.v = v;
		return this;
	}

}
