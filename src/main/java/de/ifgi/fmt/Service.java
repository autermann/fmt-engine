package de.ifgi.fmt;

public class Service {

	private static Service service;

	public static Service getInstance() {
		return (service == null) ? service = new Service() : service;
	}
}
