package com.eudycontreras.multithreading.exceptions;

public class NotFutureTaskException extends Exception {

	/**
	 * Created by Eudy Contreras on 10/14/2016.
	 */

	private static final long serialVersionUID = 1L;

	public NotFutureTaskException() {
		super();
	}

	public NotFutureTaskException(String message) {
		super(message);
	}

	public NotFutureTaskException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFutureTaskException(Throwable cause) {
		super(cause);
	}

}
