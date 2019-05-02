package com.backbase.game.kalah.exceptions.impl;

import com.backbase.game.kalah.exceptions.KalahException;

/**
 * Exception {@link GameNotFoundException} is thrown when requested game id does
 * not exist in the data set.
 * 
 * @author Sonee
 *
 */
public class GameNotFoundException extends KalahException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public GameNotFoundException(String message) {
		super(message);
	}

}
