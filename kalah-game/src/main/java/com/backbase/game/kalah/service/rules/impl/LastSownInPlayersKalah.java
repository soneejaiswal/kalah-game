package com.backbase.game.kalah.service.rules.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.backbase.game.kalah.Game;
import com.backbase.game.kalah.service.rules.GameRules;

/**
 * If the last sown stone lands in the player's store, the player gets an
 * additional move. There is no limit on the number of moves a player can make
 * in their turn.
 *
 * @author sonee
 */
@Service
public class LastSownInPlayersKalah implements GameRules {

	private Logger log = LoggerFactory.getLogger(LastSownInPlayersKalah.class);

	/**
	 * Leave current player as it is and set current turn to null to skip next steps
	 * if last stone lands in the kalah
	 *
	 * @param game {@link Game}
	 */
	@Override
	public void applyRule(Game game) {
		if (!game.isGameOver()) {
			int lastSownIndex = game.getCurrentTurn().getLastSownIndex();

			if (game.getCurrentPlayer().getKalahIndex() == lastSownIndex) {
				log.debug("Last sown stone is located at player's kalah. Setting additional move.");
				// set previous turn & leave current player the same so that additional move is
				// guaranteed
				game.setPreviousTurn(game.getCurrentTurn());
				game.setCurrentTurn(null);
				// finishing with current turn. remaining the same player to make additional
				// move
			}
		}
	}

	@Override
	public int getOrder() {
		return 2;
	}
}
