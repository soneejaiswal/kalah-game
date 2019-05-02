package com.backbase.game.kalah.player.impl;

import com.backbase.game.kalah.player.Player;

/**
 * Assuming we count pits starting with 0 index in right hand direction. Top
 * player, which is located at the top and it has its own kalah at 13th index.
 *
 * @author sonee
 */
public class TopPlayer implements Player {

	private static final int TOP_KALAH_INDEX = 13;

	private static final String TOP_PLAYER_NAME = "top";

	private String name;
	private int topKalahIndex;

	public TopPlayer() {
		this.name = TOP_PLAYER_NAME;
		this.topKalahIndex = TOP_KALAH_INDEX;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getKalahIndex() {
		return this.topKalahIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + topKalahIndex;
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
		TopPlayer other = (TopPlayer) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (topKalahIndex != other.topKalahIndex)
			return false;
		return true;
	}

}
