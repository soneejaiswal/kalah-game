package com.backbase.game.kalah.player;

import com.backbase.game.kalah.player.impl.BottomPlayer;
import com.backbase.game.kalah.player.impl.TopPlayer;

/**
 * {@link Player} details
 * 
 * @author sonee
 * 
 * @see TopPlayer
 * @see BottomPlayer
 */
public interface Player {

	/**
	 * Fetch players name
	 * 
	 * @return {@link String}
	 */
	String getName();

	/**
	 * Fetch index for Kalah
	 * 
	 * @return int
	 */
	int getKalahIndex();
}
