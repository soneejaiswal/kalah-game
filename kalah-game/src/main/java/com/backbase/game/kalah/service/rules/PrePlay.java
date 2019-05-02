package com.backbase.game.kalah.service.rules;

import com.backbase.game.kalah.Game;
import com.backbase.game.kalah.board.Board;
import com.backbase.game.kalah.player.Player;

/**
 * {@link PrePlay} has only 1 method to check conditions if {@link Player} can
 * the play the {@link Game}. This {@link PrePlay} check can be applied on
 * {@link GameRules}
 * 
 * @author Sonee
 *
 */
public interface PrePlay {

	/**
	 * Check if current player can take turn and choose the pit with
	 * {@param chosenPit}.
	 *
	 * @param game      {@link Game}
	 * @param chosenPit index of the pit player has selected
	 */
	default boolean isPlayerAbleToTakeTheTurn(Game game, int chosenPit) {
		int storeIndex = game.getCurrentPlayer().getKalahIndex();

		return chosenPit < storeIndex && chosenPit >= storeIndex - Board.NUMBER_OF_PITS_ASSIGNED_TO_PLAYER;

	}

}
