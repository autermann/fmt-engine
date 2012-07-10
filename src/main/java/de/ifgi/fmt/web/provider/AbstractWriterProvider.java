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
package de.ifgi.fmt.web.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ifgi.fmt.utils.Utils;

/**
 * 
 * @author Autermann, Demuth, Radtke
 * @param <T>
 */
public abstract class AbstractWriterProvider<T> implements MessageBodyWriter<T> {
	/**
     * 
     */
	protected static final Logger log = LoggerFactory
			.getLogger(AbstractWriterProvider.class);
	private Class<?> collectionClass;
	private Class<?> itemClass;
	private Set<MediaType> mimes;
	private UriInfo uriInfo;

	/**
	 * 
	 * @param clazz
	 * @param type
	 */
	public AbstractWriterProvider(Class<?> clazz, MediaType... type) {
		this(clazz, false, type);
		log.debug("Instantiation {}.", new Object[] { getClass() });
	}

	/**
	 * 
	 * @param itemClass
	 * @param collection
	 * @param type
	 */
	public AbstractWriterProvider(Class<?> itemClass, boolean collection,
			MediaType... type) {
		this.itemClass = itemClass;
		if (collection) {
			this.collectionClass = Iterable.class;
		}
		this.mimes = Utils.set(type);
	}

	/**
	 * 
	 * @param uriInfo
	 */
	@Context
	public void setUriInfo(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	/**
	 * 
	 * @return
	 */
	protected UriInfo getUriInfo() {
		return this.uriInfo;
	}

	/**
	 * 
	 * @param t
	 * @param gt
	 * @param a
	 * @param mt
	 * @return
	 */
	@Override
	public boolean isWriteable(Class<?> t, Type gt, Annotation[] a, MediaType mt) {
		if (collectionClass != null) {
			if (!Utils.isParameterizedWith(gt, collectionClass, itemClass)) {
				return false;
			}
		} else if (!itemClass.isAssignableFrom(t)) {
			return false;
		}
		for (MediaType m : this.mimes) {
			if (m.isCompatible(mt)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param o
	 * @param t
	 * @param gt
	 * @param a
	 * @param mt
	 * @return
	 */
	@Override
	public long getSize(T o, Class<?> t, Type gt, Annotation[] a, MediaType mt) {
		return -1;
	}

	/**
	 * 
	 * @param o
	 * @param t
	 * @param gt
	 * @param a
	 * @param mt
	 * @param h
	 * @param os
	 * @throws IOException
	 * @throws WebApplicationException
	 */
	@Override
	public abstract void writeTo(T o, Class<?> t, Type gt, Annotation[] a,
			MediaType mt, MultivaluedMap<String, Object> h, OutputStream os)
			throws IOException, WebApplicationException;

}
