package de.ifgi.fmt.update;

public abstract class EntityUpdater<T> {
	public abstract T update(T old, T changes);
}
