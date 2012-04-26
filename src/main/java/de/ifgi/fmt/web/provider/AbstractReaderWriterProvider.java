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

public abstract class AbstractReaderWriterProvider<T> extends
		AbstractWriterProvider<T> implements MessageBodyReader<T> {

	public AbstractReaderWriterProvider(Class<?> collectionClass,
			Class<?> itemClass, MediaType... type) {
		super(collectionClass, true, type);
	}
	
	public AbstractReaderWriterProvider(Class<?> clazz, MediaType... type) {
		super(clazz, false, type);
	}

	@Override
	public boolean isReadable(Class<?> t, Type gt, Annotation[] a, MediaType mt) {
		
		return isWriteable(t, gt, a, mt);
	}

	public abstract T readFrom(Class<T> t, Type gt, Annotation[] a,
			MediaType mt, MultivaluedMap<String, String> h, InputStream is)
			throws IOException, WebApplicationException;

	@Override
	public abstract void writeTo(T o, Class<?> t, Type gt, Annotation[] a,
			MediaType mt, MultivaluedMap<String, Object> h, OutputStream os)
			throws IOException, WebApplicationException;

}
