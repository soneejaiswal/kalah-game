package com.backbase.game.kalah.turn;

/**
 * {@link Turn} details about selected pit from which move should be made & the
 * end pit index where the last stone was sown.
 *
 * @author sonee
 */
public class Turn {
	private int chosenPit;
	private int lastSownIndex;

	public int getChosenPit() {
		return chosenPit;
	}

	public Turn(int chosenPit) {
		this.chosenPit = chosenPit;
	}

	public int getLastSownIndex() {
		return lastSownIndex;
	}

	public void setLastSownIndex(int lastSownIndex) {
		this.lastSownIndex = lastSownIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + chosenPit;
		result = prime * result + lastSownIndex;
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
		Turn other = (Turn) obj;
		if (chosenPit != other.chosenPit)
			return false;
		if (lastSownIndex != other.lastSownIndex)
			return false;
		return true;
	}

}
