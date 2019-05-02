package com.backbase.game.kalah.service.rules.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.backbase.game.kalah.Game;
import com.backbase.game.kalah.board.Board;
import com.backbase.game.kalah.player.Player;
import com.backbase.game.kalah.service.rules.GameRules;

/**
 * In case last sown stone is landed in an empty pit owned by the player and the
 * opposite pit contains stones then both the last stone and the opposite stones
 * are collected and placed into the player's store. F
 * 
 * @author sonee
 */
@Service
public class LastSownInEmptyPit implements GameRules {

	private Logger log = LoggerFactory.getLogger(LastSownInEmptyPit.class);

	/**
	 * Try to capture stones from pits of another player if our last stone lands in
	 * our empty pit. Otherwise, opposite player takes the turn. This action is
	 * taken, provided game is not over yet.
	 *
	 * @param game {@link Game}
	 */
	@Override
	public void applyRule(Game game) {
		if (game.getCurrentTurn() != null && !game.isGameOver()) {
			int lastSownIndex = game.getCurrentTurn().getLastSownIndex();

			// if player cannot collect stones, skip collecting and move a turn to another
			// player
			if (!isPlayerAbleToTakeTheTurn(game, lastSownIndex)) {
				log.debug("current player cannot fetch stones from opposite pit");
				wrapTurnUp(game);
				return;
			}

			captureOppositeAndCurrentPitsStonesIntoPlayersKalah(game, lastSownIndex);
			wrapTurnUp(game);
		}
	}

	/**
	 * If {@param lastSownIndex} is not located at one of the kalahs, skip
	 * capturing. If opposite pit contains stones, we can capture them with stones
	 * from our pit and put into the kalah
	 *
	 * @param game          {@link Game}
	 * @param lastSownIndex index of pit, where we put the last stone
	 */
	private void captureOppositeAndCurrentPitsStonesIntoPlayersKalah(Game game, int lastSownIndex) {
		List<Integer> storeIndexes = game.getPlayers().stream().map(Player::getKalahIndex).collect(Collectors.toList());

		if (!storeIndexes.contains(lastSownIndex)
				&& isLastSownPitIsCurrentPlayersPit(lastSownIndex, game.getCurrentPlayer())) {
			log.debug("last sown element is NOT located at one of the stores");

			int lastSownPitValue = game.getBoard().getByIndex(lastSownIndex);

			if (lastSownPitValue == 1) { // the pit was empty before we put last stone there
				int oppositePitIndex = Math.abs(lastSownIndex - 12);
				int numberOfStonesInOppositePit = game.getBoard().getByIndex(oppositePitIndex);
				if (numberOfStonesInOppositePit > 0) {

					Map<Integer, Integer> gameStatusBoard = game.getBoard().getStatus();
					int index = gameStatusBoard.get(game.getCurrentPlayer().getKalahIndex());
					index += numberOfStonesInOppositePit + lastSownPitValue;

					gameStatusBoard.put(game.getCurrentPlayer().getKalahIndex(), index);
					gameStatusBoard.put(oppositePitIndex, 0);
					gameStatusBoard.put(lastSownIndex, 0);
				}
			}
		}
	}

	/**
	 * Check whether last sown stone lands in the current player's kalah
	 *
	 * @param lastSownIndex  index of pit where the last stone lands
	 * @param takeTurnPlayer {@link Player} current player
	 * @return boolean
	 * 
	 */
	private boolean isLastSownPitIsCurrentPlayersPit(int lastSownIndex, Player takeTurnPlayer) {
		int storeIndex = takeTurnPlayer.getKalahIndex();
		return lastSownIndex < storeIndex && lastSownIndex >= storeIndex - Board.NUMBER_OF_PITS_ASSIGNED_TO_PLAYER;
	}

	/**
	 * Prepare for next move by setting up the current turn as previous one and find
	 * another player to take a turn.
	 *
	 * @param game {@link Game}
	 */
	private void wrapTurnUp(Game game) {
		game.setPreviousTurn(game.getCurrentTurn());
		game.setCurrentTurn(null);
		game.setTakeTurnPlayer(findNextTakeTurnPlayer(game));
	}

	/**
	 * Find another player to take a turn
	 *
	 * @param game {@link Game}
	 * @return {@link Player} who will be next
	 */
	private Player findNextTakeTurnPlayer(Game game) {
		return game.getPlayers().stream().filter(player -> !player.equals(game.getCurrentPlayer()))
				.collect(Collectors.toList()).get(0);
	}

	@Override
	public int getOrder() {
		return 3;
	}
}
