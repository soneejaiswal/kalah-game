package com.backbase.game.kalah.player.impl;

import com.backbase.game.kalah.player.Player;

/**
 * Assuming we count pits starting with 0 index in right hand direction. Bottom
 * player is located at the bottom of the game board and it has its own kalah at
 * 6th index.
 *
 * @author sonee
 */
public class BottomPlayer implements Player {

	private static final int BOTTOM_KALAH_INDEX = 6;

	private static final String BOTTOM_PLAYER_NAME = "bottom";

	private String name;
	private int bottomKalahIndex;

	public BottomPlayer() {
		this.name = BOTTOM_PLAYER_NAME;
		this.bottomKalahIndex = BOTTOM_KALAH_INDEX;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getKalahIndex() {
		return this.bottomKalahIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bottomKalahIndex;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BottomPlayer other = (BottomPlayer) obj;
		if (bottomKalahIndex != other.bottomKalahIndex)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
