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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a BoundingBox
 * 
 * @author Autermann, Demuth, Radtke
 */
public class BoundingBox {

	/**
	 * A Logging Factory
	 */
	protected static final Logger log = LoggerFactory
			.getLogger(BoundingBox.class);
	/**
	 * Constant String
	 */
	private static final String PARSE_ERROR = "Invalid BoundingBox: syntax is: left,bottom,right,top";

	/**
	 * Creates a BBox from a String
	 * 
	 * @param s
	 *            a String
	 * @return a BBox
	 */
	public static BoundingBox valueOf(String s) {
		log.debug("Parsing BoundingBox: {}", s);
		if (s == null || s.trim().isEmpty()) {
			throw new IllegalArgumentException(PARSE_ERROR);
		}
		String a[] = s.split(",");
		if (a.length != 4) {
			throw new IllegalArgumentException(PARSE_ERROR);
		}

		return new BoundingBox(Double.valueOf(a[0]), Double.valueOf(a[1]),
				Double.valueOf(a[2]), Double.valueOf(a[3]));
	}

	private final double left, bottom, right, top;

	/**
	 * Constructor of a BBox
	 * 
	 * @param left
	 *            east extent
	 * @param bottom
	 *            south extent
	 * @param right
	 *            west extent
	 * @param top
	 *            north extent
	 */
	public BoundingBox(double left, double bottom, double right, double top) {
		this.left = left;
		this.bottom = bottom;
		this.right = right;
		this.top = top;
	}

	/**
	 * Returns the bottom of a bbox
	 * 
	 * @return bottom south extent
	 */
	public double getBottom() {
		return bottom;
	}

	/**
	 * retunrs the left of a bbox
	 * 
	 * @return left east extent
	 */
	public double getLeft() {
		return left;
	}

	/**
	 * retunrs the right of a bbox
	 * 
	 * @return west extent
	 */
	public double getRight() {
		return right;
	}

	/**
	 * returns the top of a bbox
	 * 
	 * @return north extent
	 */
	public double getTop() {
		return top;
	}
}