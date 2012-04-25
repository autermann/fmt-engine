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
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;

import de.ifgi.fmt.utils.Implementations;
import de.ifgi.fmt.utils.Stringifier;
import de.ifgi.fmt.utils.Utils;

public class MongoDB {
	
	private static final Logger log = LoggerFactory.getLogger(MongoDB.class);

	private static final String PROPERTIES_FILE = "/mongo.properties";

	private static final String HOST_PROPERTY = "host";
	private static final String PORT_PROPERTY = "port";
	private static final String AUTH_PROPERTY = "auth";
	private static final String USER_PROPERTY = "user";
	private static final String PASS_PROPERTY = "pass";
	private static final String DATABASE_PROPERTY = "database";

	private static MongoDB instance;

	protected static MongoDB getInstance() {
		return (instance == null) ? instance = new MongoDB() : instance;
	}

	private final Mongo mongo;
	private final Morphia morphia;
	private final Datastore datastore;
	private final String database;

	protected MongoDB() {
		try {

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

			this.mongo = new Mongo(new ServerAddress(host, Integer.valueOf(port)));

			this.morphia = new Morphia();
			DefaultConverters dc = this.morphia.getMapper().getConverters();
			Set<Class<? extends TypeConverter>> classes = Implementations.getSubclasses(TypeConverter.class);
			if (log.isDebugEnabled()) {
				log.debug("Mongo converter classes found:\n{}", Utils.join(new Stringifier() {
					public String toString(Object t) { return "  " + t.toString(); }
				},"\n", classes));
			}
			for (Class<? extends TypeConverter> c : classes) {
				dc.addConverter(c);
			}
		

			String auth = p.getProperty(AUTH_PROPERTY);
			auth = (auth == null || auth.trim().isEmpty()) ? "false" : auth;
			String dbna = p.getProperty(DATABASE_PROPERTY);
			this.database = (auth == null || dbna.trim().isEmpty()) ? "happyparents" : dbna;

			if (Boolean.valueOf(auth)) {
				this.datastore = this.morphia.createDatastore(this.mongo,
				    this.database, p.getProperty(USER_PROPERTY, "mongo"), p
				        .getProperty(PASS_PROPERTY, "mongo").toCharArray());
			} else {
				this.datastore = this.morphia
				    .createDatastore(this.mongo, this.database);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	Mongo getMongo() {
		return this.mongo;
	}

	Morphia getMorphia() {
		return this.morphia;
	}

	Datastore getDatastore() {
		return this.datastore;
	}

	String getDatabase() {
		return this.database;
	}
	
	public static class MongoDao<T> extends BasicDAO<T, ObjectId> {

		protected MongoDao(Class<T> entityClass) {
			super(entityClass, getInstance().getDatastore());
			getDatastore().ensureIndexes();
		}

		public static <U> MongoDao<U> get(Class<U> entity) {
			return new MongoDao<U>(entity);
		}
		
	}
}
