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
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

/**
 * 
 * @author Autermann, Demuth, Radtke
 * @param <T>
 */
public abstract class AbstractReaderWriterProvider<T> extends
		AbstractWriterProvider<T> implements MessageBodyReader<T> {

	/**
	 * 
	 * @param collectionClass
	 * @param itemClass
	 * @param type
	 */
	public AbstractReaderWriterProvider(Class<?> collectionClass,
			Class<?> itemClass, MediaType... type) {
		super(collectionClass, true, type);
	}

	/**
	 * 
	 * @param clazz
	 * @param type
	 */
	public AbstractReaderWriterProvider(Class<?> clazz, MediaType... type) {
		super(clazz, false, type);
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
	public boolean isReadable(Class<?> t, Type gt, Annotation[] a, MediaType mt) {

		return isWriteable(t, gt, a, mt);
	}

	/**
	 * 
	 * @param t
	 * @param gt
	 * @param a
	 * @param mt
	 * @param h
	 * @param is
	 * @return
	 * @throws IOException
	 * @throws WebApplicationException
	 */
	public abstract T readFrom(Class<T> t, Type gt, Annotation[] a,
			MediaType mt, MultivaluedMap<String, String> h, InputStream is)
			throws IOException, WebApplicationException;

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
