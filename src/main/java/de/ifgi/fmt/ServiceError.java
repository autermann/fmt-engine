package de.ifgi.fmt;


public class ServiceError extends RuntimeException {

	private static final long serialVersionUID = 5597652128019474217L;

	public enum Status {
		BAD_REQUEST(400), 
		NOT_FOUND(404), 
		INTERNAL_SERVER_ERROR(500);
		
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

	
	public int getErrorCode() {
		return errorCode.getCode();
	}

	public void setErrorCode(Status errorCode) {
		this.errorCode = errorCode;
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

	public static ServiceError invalidParameter(String name) {
		return badRequest("Invalid parameter '" + name + "'.");
	}
	
	public static ServiceError invalidParameter(String name, String message) {
		return badRequest("Invalid parameter '" + name + "': " + message);
	}

	public static ServiceError missingParameter(String name) {
		return badRequest("Missing parameter: '" + name + "'.");
	}


	public static ServiceError flashmobNotFound() {
		return notFound("flashmob not found");
	}
	
	public static ServiceError triggerNotFound() {
		return notFound("trigger not found");
	}
	
	public static ServiceError userNotFound() {
		return notFound("user not found");
	}
	
	public static ServiceError roleNotFound() {
		return notFound("role not found");
	}
	
	public static ServiceError activityNotFound() {
		return notFound("activity not found");
	}
	
	public static ServiceError signalNotFound() {
		return notFound("signal not found");
	}

	public static ServiceError taskNotFound() {
		return notFound("no such task");
	}
	
	public static ServiceError commentNotFound() {
		return notFound("no such comment");
	}
	

}