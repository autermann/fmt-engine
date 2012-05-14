package de.ifgi.fmt.utils;

public class Pair<U, V> {

	private U u;
	private V v;

	public Pair(U u, V v) {
		setOne(u);
		setTwo(v);
	}

	public Pair() {
	}

	public U getOne() {
		return u;
	}

	public V getTwo() {
		return v;
	}

	public Pair<U, V> setOne(U u) {
		this.u = u;
		return this;
	}

	public Pair<U, V> setTwo(V v) {
		this.v = v;
		return this;
	}

}
