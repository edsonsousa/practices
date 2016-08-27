package br.edson.sousa.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ParkingException extends Exception {

	private static final long serialVersionUID = -5953890882124110394L;

	public ParkingException(String message) {
		super(message);
	}

	public ParkingException(Throwable e, String message) {
		super(message, e);
	}
}