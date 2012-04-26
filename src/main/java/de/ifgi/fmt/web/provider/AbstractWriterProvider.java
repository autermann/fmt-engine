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

public abstract class AbstractWriterProvider<T> implements MessageBodyWriter<T> {
	protected static final Logger log = LoggerFactory.getLogger(AbstractWriterProvider.class);
	private Class<?> collectionClass;
	private Class<?> itemClass;
	private Set<MediaType> mimes;
	private UriInfo uriInfo;

	public AbstractWriterProvider(Class<?> clazz, MediaType... type) {
		this(clazz, false, type);
	}

	public AbstractWriterProvider(Class<?> itemClass, boolean collection, 
			MediaType... type) {
		this.itemClass = itemClass;
		if (collection) {
			this.collectionClass = Iterable.class;
		}
		this.mimes = Utils.set(type);
	}

	@Context
	public void setUriInfo(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	protected UriInfo getUriInfo() {
		return this.uriInfo;
	}

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

	@Override
	public long getSize(T o, Class<?> t, Type gt, Annotation[] a, MediaType mt) {
		return -1;
	}

	@Override
	public abstract void writeTo(T o, Class<?> t, Type gt, Annotation[] a,
			MediaType mt, MultivaluedMap<String, Object> h, OutputStream os)
			throws IOException, WebApplicationException;

}
