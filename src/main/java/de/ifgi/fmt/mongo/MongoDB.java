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
package de.ifgi.fmt.mongo;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.converters.DefaultConverters;
import com.google.code.morphia.converters.TypeConverter;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.logging.MorphiaLoggerFactory;
import com.google.code.morphia.logging.slf4j.SLF4JLogrImplFactory;
import com.google.code.morphia.validation.MorphiaValidation;
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

import de.ifgi.fmt.utils.Implementations;
import de.ifgi.fmt.utils.Stringifier;
import de.ifgi.fmt.utils.Utils;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
public class MongoDB {

	private static final Logger log = LoggerFactory.getLogger(MongoDB.class);

	private static final String PROPERTIES_FILE = "/mongo.properties";

	private static final String HOST_PROPERTY = "host";
	private static final String PORT_PROPERTY = "port";
	private static final String AUTH_PROPERTY = "auth";
	private static final String USER_PROPERTY = "user";
	private static final String PASS_PROPERTY = "pass";
	private static final String DATABASE_PROPERTY = "database";
	private static final String DEFAULT_DATABASE = "fmt";

	private static MongoDB instance;

	/**
	 * 
	 * @return
	 */
	public static MongoDB getInstance() {
		return (instance == null) ? instance = new MongoDB() : instance;
	}

	private final Mongo mongo;
	private final Morphia morphia;
	private final Datastore datastore;
	private final String database;

	/**
	 * 
	 */
	protected MongoDB() {
		try {
			MorphiaLoggerFactory.registerLogger(SLF4JLogrImplFactory.class);
			InputStream is = getClass().getResourceAsStream(PROPERTIES_FILE);
			Properties p = new Properties();

			if (is != null) {
				try {
					p.load(is);
				} finally {
					IOUtils.closeQuietly(is);
				}
			}

			String host = p.getProperty(HOST_PROPERTY);
			host = (host == null || host.trim().isEmpty()) ? "localhost" : host;
			String port = p.getProperty(PORT_PROPERTY);
			port = (port == null || port.trim().isEmpty()) ? "27017" : port;

			this.mongo = new Mongo(new ServerAddress(host,
					Integer.valueOf(port)));

			this.morphia = new Morphia();
			DefaultConverters dc = this.morphia.getMapper().getConverters();
			Set<Class<? extends TypeConverter>> classes = Implementations
					.getSubclasses(TypeConverter.class);
			if (log.isDebugEnabled()) {
				log.debug("{} Mongo converter classes found:\n{}",
						classes.size(), Utils.join(new Stringifier() {
							public String toString(Object t) {
								return "  " + t.toString();
							}
						}, "\n", classes));
			}
			
			for (Class<? extends TypeConverter> c : classes) {
				dc.addConverter(c);
			}

			new MorphiaValidation().applyTo(this.morphia);
			
			String auth = p.getProperty(AUTH_PROPERTY);
			auth = (auth == null || auth.trim().isEmpty()) ? "false" : auth;
			String dbna = p.getProperty(DATABASE_PROPERTY);
			this.database = (auth == null || dbna.trim().isEmpty()) ? DEFAULT_DATABASE
					: dbna;

			if (Boolean.valueOf(auth)) {
				this.datastore = this.morphia.createDatastore(this.mongo,
						this.database, p.getProperty(USER_PROPERTY, "mongo"), p
								.getProperty(PASS_PROPERTY, "mongo")
								.toCharArray());
			} else {
				this.datastore = this.morphia.createDatastore(this.mongo,
						this.database);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public Mongo getMongo() {
		return this.mongo;
	}

	/**
	 * 
	 * @return
	 */
	public Morphia getMorphia() {
		return this.morphia;
	}

	/**
	 * 
	 * @return
	 */
	public Datastore getDatastore() {
		return this.datastore;
	}

	/**
	 * 
	 * @return
	 */
	public String getDatabase() {
		return this.database;
	}

	/**
	 * 
	 * @param <T>
	 */
	public static class MongoDao<T> extends BasicDAO<T, ObjectId> {

	    /**
	     * 
	     * @param entityClass
	     */
	    protected MongoDao(Class<T> entityClass) {
			super(entityClass, getInstance().getDatastore());
			getDatastore().setDefaultWriteConcern(WriteConcern.SAFE);
			getDatastore().ensureCaps();
			getDatastore().ensureIndexes();
		}

		/**
		 * 
		 * @param col
		 */
		public void saveAll(Iterable<? extends T> col) {
			for (T t : col) {
				save(t);
			}
		}
		
		/**
		 * 
		 * @param col
		 */
		public void deleteAll(Iterable<? extends T> col) {
			for (T t : col) {
				delete(t);
			}
		}

		/**
		 * 
		 * @param <U>
		 * @param entity
		 * @return
		 */
		public static <U> MongoDao<U> get(Class<U> entity) {
			return new MongoDao<U>(entity);
		}

	}
}
