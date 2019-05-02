package com.backbase.game.kalah.exceptions;

/**
 * Exception {@link KalahException} is a root of kalah game application
 * exception tree hierarchy
 * 
 * @author Sonee
 *
 */
public class KalahException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public KalahException(String message) {
		super(message);
	}
}
