package com.eudycontreras.multithreading.exceptions;

public class NoSuchTaskException extends Exception {

	/**
	 * Created by Eudy Contreras on 10/14/2016.
	 */

	private static final long serialVersionUID = 1L;

	public NoSuchTaskException() {
		super();
	}

	public NoSuchTaskException(String message) {
		super(message);
	}

	public NoSuchTaskException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchTaskException(Throwable cause) {
		super(cause);
	}

}
