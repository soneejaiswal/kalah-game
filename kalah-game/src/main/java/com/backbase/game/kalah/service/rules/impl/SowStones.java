package com.backbase.game.kalah.service.rules.impl;

import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.backbase.game.kalah.Game;
import com.backbase.game.kalah.player.Player;
import com.backbase.game.kalah.service.rules.GameRules;

/**
 * Players move stones on getting their turns by choosing a pit( Only those pits
 * can be chosen which a player is managing). The player who begins, picks up
 * all the stones in any of their own pits, and sows the stones on to the right,
 * one in each of the following pits, including his own Kalah.
 * 
 * No stones are put in the opponent's' Kalah. If the players last stone lands
 * in his own Kalah, he gets another turn. This can be repeated any number of
 * times before it's the other player's turn.
 * 
 * @see super class CheckTakeTurnPlayerRulesApplier
 * 
 * @author sonee
 */
@Service
public class SowStones implements GameRules {

	Logger log = LoggerFactory.getLogger(SowStones.class);

	/**
	 * Applies general rules for the game to sow the stones from chosen pit in right
	 * hand onwards direction, provided game is not over yet and player is able to
	 * take the turn.
	 *
	 * @param game {@link Game}
	 */
	@Override
	public void applyRule(Game game) {
		if (!game.isGameOver()) {
			int chosenPit = game.getCurrentTurn().getChosenPit();
			if (isPlayerAbleToTakeTheTurn(game, chosenPit)) {
				sowStonesAcrossPits(game, chosenPit);
			}
		}
	}

	/**
	 * Find total number of pits present on the game board including Kalahs.
	 * 
	 * @param game {@link Game}
	 * @return total number of pits present on the game board including Kalahs as
	 *         well.
	 */
	private int totalNumberOfPitsOnTheBoard(Game game) {
		return game.getBoard().getStatus().size();
	}

	/**
	 * Find index of the store of another player
	 *
	 * @param game {@link Game}
	 * @return store index of another player, otherwise throw exception
	 */
	private Integer opponentKalahIndex(Game game) {
		return game.getPlayers().stream().map(Player::getKalahIndex)
				.filter(index -> game.getCurrentPlayer().getKalahIndex() != index).collect(Collectors.toList()).get(0);
	}

	/**
	 * Fetch stones from {@param chosenPit} and sow them in right hand direction
	 * skipping opponent Kalah. If number of stones in {@param chosenPit} is too big
	 * so you can have 2 rounds of sowing, {@param chosenPit} will be sown too on each
	 * round after first one.
	 * <p>
	 * After sowing is finished, last sown index is kept for further steps of
	 * processing based on Kalah rules
	 *
	 * @param game               {@link Game}
	 * @param chosenPit          index of the pit, which has been chosen by current
	 *                           player
	 * @param opponentKalahIndex index of store cell of another player. needed to
	 *                           skip it while sowing
	 */
	private void sowStonesAcrossPits(Game game, int chosenPit) {
		Map<Integer, Integer> newStatusOfBoard = game.getBoard().getStatus();

		// stones should be iterated from next pit
		int startIndexToMoveFromChosenPit = chosenPit + 1;
		int stonesInChosenPit = newStatusOfBoard.get(chosenPit);
		newStatusOfBoard.put(chosenPit, 0); // as all stones would be moved from chosen pit, current number of stones in
											// it should be set to 0

		int lastSownIndex = -1; // as our indexing starts from 0, assigning some non existing number on the board
		// iterate over map and put 1 stone into each next pit/kalah

		int totalNumberOfPitsOnTheBoard = totalNumberOfPitsOnTheBoard(game);
		int opponentKalahIndex = opponentKalahIndex(game);

		for (int i = 0; i < stonesInChosenPit; i++) {
			lastSownIndex = (i + startIndexToMoveFromChosenPit) % totalNumberOfPitsOnTheBoard;

			// skip opponent store and fill next pit
			if (lastSownIndex == opponentKalahIndex) {
				lastSownIndex = (++i + startIndexToMoveFromChosenPit) % totalNumberOfPitsOnTheBoard;
				// add 1 stone to avoid skipping one cell when we are jumping over opposite
				// player's store
				stonesInChosenPit += 1;
			}
			newStatusOfBoard.put(lastSownIndex, newStatusOfBoard.get(lastSownIndex) + 1);
		}

		log.debug("Board's intermediate state after sowing - {}", game.getBoard().getStatus());

		game.getCurrentTurn().setLastSownIndex(lastSownIndex);
	}

	@Override
	public int getOrder() {
		return 1;
	}
}
