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
package de.ifgi.fmt;

public class ServiceError extends RuntimeException {

    private static final long serialVersionUID = 5597652128019474217L;

    /**
     * Enumerator defining HTTP errors
     */
    public enum Status {

	/**
	 * Generic http 400
	 */
	BAD_REQUEST(400),
	/**
	 * Http 404 Resource not found
	 */
	NOT_FOUND(404),
	/**
	 * http 401 not authorized
	 */
	NOT_AUTHORIZED(401),
	/**
	 * http 403 access forbidden
	 */
	FORBIDDEN(403),
	/**
	 * http 500 internal server error
	 */
	INTERNAL_SERVER_ERROR(500);
	private int code;

	private Status(int code) {
	    this.code = code;
	}

	/**
	 * rturns the error code
	 * @return an error code
	 */
	public int getCode() {
	    return this.code;
	}
    }
    private Status errorCode;

    /**
     * Set the Error Code
     * @param status a status from status enum
     * @param message error message
     */
    public ServiceError(Status status, String message) {
	super(message);
	this.setErrorCode(status);
    }

    /**
     * Create a new ServiceError
     * @param code the error code
     * @param cause a throwable cause
     */
    public ServiceError(Status code, Throwable cause) {
	super(cause);
	this.setErrorCode(code);
    }

    /**
     * Returns a code for an error
     * @return an errorcode
     */
    public int getErrorCode() {
	return errorCode.getCode();
    }

    /**
     * Sets the Code of an Error
     * @param errorCode an errorcode
     */
    public void setErrorCode(Status errorCode) {
	this.errorCode = errorCode;
    }

    /**
     * Return a 500
     * @param message a text
     * @return http 500 error
     */
    public static ServiceError internal(String message) {
	return new ServiceError(Status.INTERNAL_SERVER_ERROR, message);
    }

    /**
     * Return a 404
     * @param message a text
     * @return http 404 error
     */
    public static ServiceError notFound(String message) {
	return new ServiceError(Status.NOT_FOUND, message);
    }

    /**
     * Return a 401
     * @param message a text
     * @return http 401 error
     */
    public static ServiceError notAuthorized(String message) {
	return new ServiceError(Status.NOT_AUTHORIZED, message);
    }

    /**
     * Return a 403
     * @param message a text
     * @return http 403 error
     */
    public static ServiceError forbidden(String message) {
	return new ServiceError(Status.FORBIDDEN, message);
    }

    /**
     * Return a 400
     * @param message a text
     * @return http 400 error
     */
    public static ServiceError badRequest(String message) {
	return new ServiceError(Status.BAD_REQUEST, message);
    }

    /**
     * Return a 400
     * @param cause a throwable
     * @return http 400 error
     */
    public static ServiceError badRequest(Throwable cause) {
	if (cause != null && cause instanceof ServiceError) {
	    return (ServiceError) cause;
	}
	return new ServiceError(Status.BAD_REQUEST, cause);
    }

    /**
     * Return a 500
     * @param cause a throwable
     * @return http 500 error
     */
    public static ServiceError internal(Throwable cause) {
	if (cause != null && cause instanceof ServiceError) {
	    return (ServiceError) cause;
	}
	return new ServiceError(Status.INTERNAL_SERVER_ERROR, cause);
    }

    /**
     * Return a 400 with Invalid Parameter text
     * @param name a text
     * @return http 400 error
     */
    public static ServiceError invalidParameter(String name) {
	return badRequest("Invalid parameter '" + name + "'.");
    }

    /**
     * Return a 400 with Invalid Parameter text and param name
     * @param name the name of the param
     * @param message a text
     * @return http 400 error
     */
    public static ServiceError invalidParameter(String name, String message) {
	return badRequest("Invalid parameter '" + name + "': " + message);
    }

    /**
     * RReturn a 400 with Missing Parameter text
     * @param name name of the param
     * @return a http 400 error
     */
    public static ServiceError missingParameter(String name) {
	return badRequest("Missing parameter: '" + name + "'.");
    }

    /**
     * Return a 403 if a user is not a coordinator
     * @return http 403 error
     */
    public static ServiceError notCoordinator() {
	return forbidden("only the coordinator can change the flashmob");
    }

    /**
     * Return a 404 if a fm is no found
     * @return http 404 error
     */
    public static ServiceError flashmobNotFound() {
	return notFound("flashmob not found");
    }

    /**
     * Return a 404 if a fm is no found
     * @return http 404 error
     */
    public static ServiceError triggerNotFound() {
	return notFound("trigger not found");
    }

    /**
     * Return a 404 if a user is not found
     * @return http 404 error
     */
    public static ServiceError userNotFound() {
	return notFound("user not found");
    }

    /**
     * Return a 404 if a role is not found
     * @return http 404 error
     */
    public static ServiceError roleNotFound() {
	return notFound("role not found");
    }

    /**
     * Return a 404 if an activity is not found
     * @return http 404 error
     */
    public static ServiceError activityNotFound() {
	return notFound("activity not found");
    }

    /**
     * Return a 404 if a signal is not found
     * @return http 404 error
     */
    public static ServiceError signalNotFound() {
	return notFound("signal not found");
    }

    /**
     * Return a 404 if a task is not found
     * @return http 404 error
     */
    public static ServiceError taskNotFound() {
	return notFound("no such task");
    }

    /**
     * Return a 404 if a comment is not found
     * @return http 404 error
     */
    public static ServiceError commentNotFound() {
	return notFound("no such comment");
    }
}