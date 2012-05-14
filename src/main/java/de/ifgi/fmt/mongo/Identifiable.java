package de.ifgi.fmt.mongo;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Polymorphic;

@Polymorphic
public abstract class Identifiable {

	@Id
	private ObjectId id = new ObjectId();

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

}
