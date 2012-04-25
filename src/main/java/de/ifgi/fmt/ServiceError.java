package de.ifgi.fmt;


public class ServiceError extends RuntimeException {

	private static final long serialVersionUID = 5597652128019474217L;

	public enum Status {
		BAD_REQUEST(400), NOT_FOUND(404), INTERNAL_SERVER_ERROR(500);
		
		private int code;

		private Status(int code) {
			this.code = code;
		}

		public int getCode() {
			return this.code;
		}

	}

	private Status errorCode;

	public ServiceError(Status status, String message) {
		super(message);
		this.setErrorCode(status);
	}

	public ServiceError(Status code, Throwable cause) {
		super(cause);
		this.setErrorCode(code);
	}

	public static ServiceError internal(String message) {
		return new ServiceError(Status.INTERNAL_SERVER_ERROR, message);
	}

	public static ServiceError notFound(String message) {
		return new ServiceError(Status.NOT_FOUND, message);
	}

	public static ServiceError badRequest(String message) {
		return new ServiceError(Status.BAD_REQUEST, message);
	}
	public static ServiceError badRequest(Throwable cause) {
		if (cause != null && cause instanceof ServiceError)
			return (ServiceError) cause;
		return new ServiceError(Status.BAD_REQUEST, cause);
	}

	public static ServiceError internal(Throwable cause) {
		if (cause != null && cause instanceof ServiceError)
			return (ServiceError) cause;
		return new ServiceError(Status.INTERNAL_SERVER_ERROR, cause);
	}

	public static ServiceError noSuchPointOfInterest() {
		return notFound("No such PointOfInterest!");
	}

	public static ServiceError noSuchImage() {
		return notFound("No such Image!");
	}

	public static ServiceError noSuchUser() {
		return notFound("No such User!");
	}

	public static ServiceError noSuchComment() {
		return notFound("No such Comment!");
	}

	public static ServiceError noSuchCategory() {
		return notFound("No such Category!");
	}

	public static ServiceError invalidParameter(String name) {
		return badRequest("Invalid parameter '" + name + "'.");
	}
	
	public static ServiceError invalidParameter(String name, String message) {
		return badRequest("Invalid parameter '" + name + "': " + message);
	}

	public static ServiceError missingParameter(String name) {
		return badRequest("Missing parameter: '" + name + "'.");
	}

	public int getErrorCode() {
		return errorCode.getCode();
	}

	public void setErrorCode(Status errorCode) {
		this.errorCode = errorCode;
	}

}