package com.backbase.game.kalah.board;

import java.util.Map;

import com.backbase.game.kalah.exceptions.impl.FailedToAccessStonesFromPitException;

/**
 * Class {@link Board} contains up-to-date state information for all the pits
 * and kalahs. It's able to fetch number of stones per pits/kalah.
 *
 * @author sonee
 */

public class Board {

	public static final int INITIAL_NUMBER_OF_STONES_PER_PIT = 6;
	public static final int NUMBER_OF_PITS_ASSIGNED_TO_PLAYER = 6;

	private Map<Integer, Integer> status;
	private boolean filled;

	public Map<Integer, Integer> getStatus() {
		return status;
	}

	public void setStatus(Map<Integer, Integer> status) {
		this.status = status;
	}

	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
	}

	/**
	 * Get number of stones in a pit index
	 *
	 * @param indexOfPit index to the pit
	 * @return number of stones inside
	 */
	public int getByIndex(int indexOfPit) {
		if (indexOfPit >= status.size() || indexOfPit < 0) {
			throw new FailedToAccessStonesFromPitException("index " + indexOfPit + " is invalid. Check it again.");
		}
		return status.get(indexOfPit);
	}

}
