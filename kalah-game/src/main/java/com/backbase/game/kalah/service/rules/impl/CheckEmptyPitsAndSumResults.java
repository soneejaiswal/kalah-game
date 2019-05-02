package com.backbase.game.kalah.service.rules.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.backbase.game.kalah.Game;
import com.backbase.game.kalah.board.Board;
import com.backbase.game.kalah.player.Player;
import com.backbase.game.kalah.service.rules.GameRules;

/**
 * Game is over when one player has 0 number of stones in all of their pits. The
 * other player moves all remaining stones to their kalah and the player with
 * the most stones wins the game!
 *
 * @author sonee
 */
@Service
public class CheckEmptyPitsAndSumResults implements GameRules {

	private Logger log = LoggerFactory.getLogger(CheckEmptyPitsAndSumResults.class);

	/**
	 * If the game is not over then check whether one of the player has all pits
	 * empty. If its all pits are empty, move other player's stones to his kalah,
	 * count stones in each player's kalah and find the winner based on the highest
	 * count.
	 *
	 * @param game {@link Game}
	 */
	@Override
	public void applyRule(Game game) {
		if (!game.isGameOver()) {
			boolean needToFinishTheGame = checkIfGameNeedsToBeFinishedAndMove(game);
			if (needToFinishTheGame) {
				findWinner(game);
			}
		}
	}

	/**
	 * Find winner with the game basis protocols as : 
	 * 1. Calculate the number of stones in kalah for both players and sort them 
	 *    in an ascending order by number of stones. The last is a winner. 
	 * 2. If both players have the same number of stones in their respective kalah then its DRAW. 
	 * 3. Otherwise, last player in the map is the winner. Set his name to {@link Game} and game is over. 
	 *    No further processing is needed after that.
	 *
	 * @param game {@link Game}
	 */
	private void findWinner(Game game) {
		log.debug("Overall player names with values of their stores");
		TreeMap<Integer, String> storeValue2PlayerName = game.getPlayers().stream()
				.collect(Collectors.toMap(player -> game.getBoard().getByIndex(player.getKalahIndex()), Player::getName,
						(a, b) -> a, // merging function in case of duplicate we accept first
						TreeMap::new));

		log.debug("result of the game {}", storeValue2PlayerName);

		if (storeValue2PlayerName.size() == 1) {
			// if there is only one entry in map, it means number of stones in Kalah of both the [layers is same
			log.debug("both players have the same result. setting as a draw");
			game.setWinner("DRAW");
			game.setGameOver(true);
		} else {
			String winner = storeValue2PlayerName.pollLastEntry().getValue();
			log.debug("winner is {}. finishing the game" + winner);
			game.setWinner(winner);
			game.setGameOver(true);
		}
	}

	/**
	 * Check both players one-by-one whether one of them has all the empty pits on the board. 
	 * 1. If so, stones from the pits of another player are moved to his kalah.
	 * 2. After that, pits are set up to zero - clean up activity
	 * 3. If one has all the empty pits, game should be finished, 
	 *    otherwise, game should proceed.
	 *
	 * @param game {@link Game}
	 * @return true if game needs be finished, otherwise false
	 */
	private boolean checkIfGameNeedsToBeFinishedAndMove(Game game) {
		log.trace("try to check whether one of the players has all the empty pits");

		boolean needToFinishTheGame = false;

		List<Integer> sortedStoreIndexes = game.getPlayers().stream().map(Player::getKalahIndex).sorted()
				.collect(Collectors.toList());

		Integer bottomPlayersKalahIndex = sortedStoreIndexes.get(0); //6
		Integer topPlayersKalahIndex = sortedStoreIndexes.get(1); //13

		boolean isBottomPlayerFinished = checkWhetherPlayerHasAllPitsEmpty(game, bottomPlayersKalahIndex);

		if (isBottomPlayerFinished) {
			moveRemainingStonesToAnotherPlayerStore(game, topPlayersKalahIndex);
			needToFinishTheGame = true;
		} else {
			boolean isTopPlayerFinished = checkWhetherPlayerHasAllPitsEmpty(game, topPlayersKalahIndex);

			if (isTopPlayerFinished) {
				moveRemainingStonesToAnotherPlayerStore(game, bottomPlayersKalahIndex);
				needToFinishTheGame = true;
			}
		}
		return needToFinishTheGame;
	}

	/**
	 * Move remaining stones from the pits of another player to his kalah and clean pits up
	 *
	 * @param game                         {@link Game}
	 * @param whoHasNonEmptyPitsKalahIndex index to the kalah of another player (who doesn't have all the empty pits)
	 */
	private void moveRemainingStonesToAnotherPlayerStore(Game game, Integer whoHasNonEmptyPitsKalahIndex) {
		Map<Integer, Integer> boardStatus = game.getBoard().getStatus();

		log.debug("get pits of another player (who still has non-empty pits)");

		int[] playerPits = Arrays.copyOfRange(boardStatus.values().stream().mapToInt(i -> i).toArray(),
				whoHasNonEmptyPitsKalahIndex - Board.NUMBER_OF_PITS_ASSIGNED_TO_PLAYER, whoHasNonEmptyPitsKalahIndex);

		log.debug("count sum of remaining stones in non-empty pits for another user and add them to kalah");
		int sumOfStonesInNonEmptyPitsForAnotherPlayer = Arrays.stream(playerPits).sum();
		boardStatus.put(whoHasNonEmptyPitsKalahIndex,
				boardStatus.get(whoHasNonEmptyPitsKalahIndex) + sumOfStonesInNonEmptyPitsForAnotherPlayer);

		cleanUpPitsAfterMovingThemToKalah(whoHasNonEmptyPitsKalahIndex,
				boardStatus.values().stream().mapToInt(i -> i).toArray());
	}

	private void cleanUpPitsAfterMovingThemToKalah(Integer whoHasNonEmptyPitsKalahIndex, int[] boardStatus) {
		log.debug("erase pits of another player after adding them to the kalah");
		for (int i = whoHasNonEmptyPitsKalahIndex
				- Board.NUMBER_OF_PITS_ASSIGNED_TO_PLAYER; i < whoHasNonEmptyPitsKalahIndex; i++) {
			boardStatus[i] = 0;
		}
	}

	/**
	 * Find all the pits of the player and check whether the sum of them is 0.
	 *
	 * @param game       {@link Game}
	 * @param storeIndex index of the store cell for player
	 * @return true if all the pit are empty
	 */
	private boolean checkWhetherPlayerHasAllPitsEmpty(Game game, Integer storeIndex) {
		int[] playerPits = Arrays.copyOfRange(game.getBoard().getStatus().values().stream().mapToInt(i -> i).toArray(),
				storeIndex - Board.NUMBER_OF_PITS_ASSIGNED_TO_PLAYER, storeIndex);
		return Arrays.stream(playerPits).sum() == 0;
	}

	@Override
	public int getOrder() {
		return 4;
	}
}
