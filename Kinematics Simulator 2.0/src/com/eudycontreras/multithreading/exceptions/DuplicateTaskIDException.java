package com.eudycontreras.multithreading.exceptions;

public class DuplicateTaskIDException extends Exception {

	/**
	 * Created by Eudy Contreras on 10/14/2016.
	 */

	private static final long serialVersionUID = 1L;

	public DuplicateTaskIDException() {
		super();
	}

	public DuplicateTaskIDException(String message) {
		super(message);
	}

	public DuplicateTaskIDException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateTaskIDException(Throwable cause) {
		super(cause);
	}

}
