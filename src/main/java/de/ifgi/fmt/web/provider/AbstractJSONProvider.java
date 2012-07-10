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
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.core.util.ReaderWriter;

import de.ifgi.fmt.json.JSONDecoder;
import de.ifgi.fmt.json.JSONEncoder;
import de.ifgi.fmt.json.JSONFactory;
import de.ifgi.fmt.model.Viewable;
import de.ifgi.fmt.utils.Utils;

/**
 * 
 * @author Autermann, Demuth, Radtke
 * @param <T>
 */
public class AbstractJSONProvider<T extends Viewable<T>> extends
		AbstractReaderWriterProvider<T> {

	private JSONEncoder<T> enc;
	private JSONDecoder<T> dec;

	/**
	 * 
	 * @param itemClass
	 * @param mt
	 */
	public AbstractJSONProvider(Class<T> itemClass, MediaType mt) {
		super(itemClass, mt);
		this.enc = JSONFactory.getEncoder(itemClass);
		this.dec = JSONFactory.getDecoder(itemClass);
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
	@Override
	public T readFrom(Class<T> t, Type gt, Annotation[] a, MediaType mt,
			MultivaluedMap<String, String> h, InputStream is)
			throws IOException, WebApplicationException {
		try {
			return dec.decode(new JSONObject(IOUtils.toString(is)));
		} catch (JSONException e) {
			throw new WebApplicationException(e, Status.BAD_REQUEST);
		}
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
	public void writeTo(T o, Class<?> t, Type gt, Annotation[] a, MediaType mt,
			MultivaluedMap<String, Object> h, OutputStream os)
			throws IOException, WebApplicationException {
		try {
			JSONObject j = enc.encode(o, getUriInfo());
			ReaderWriter.writeToAsString(Utils.toString(j), os, mt);
		} catch (JSONException e) {
			throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
		}
	}

}
