package com.backbase.game.kalah.exceptions.impl;

import com.backbase.game.kalah.exceptions.KalahException;

/**
 * Exception {@link FailedToAccessStonesFromPitException} is thrown when stones
 * can not be fetched from pits
 *
 * @author sonee
 */
public class FailedToAccessStonesFromPitException extends KalahException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public FailedToAccessStonesFromPitException(String message) {
		super(message);
	}
}
