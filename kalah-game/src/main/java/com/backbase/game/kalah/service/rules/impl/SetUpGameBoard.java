package com.backbase.game.kalah.service.rules.impl;

import static com.backbase.game.kalah.board.Board.INITIAL_NUMBER_OF_STONES_PER_PIT;

import java.util.LinkedHashMap;
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
 * Setup game board with 6 stones in each pit in the beginning of the game,
 * excluding Kalahs owned by each player
 *
 * @author sonee
 */
@Service
public class SetUpGameBoard implements GameRules {

	private Logger log = LoggerFactory.getLogger(SetUpGameBoard.class);

	/**
	 * If the game is not over, fill the board and prepare the system to start the
	 * game
	 *
	 * @param game {@link Game}
	 */
	@Override
	public void applyRule(Game game) {
		if (!game.isGameOver()) {
			setGameBoardWithInitialStoneValues(game);
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}

	/**
	 * All the pits of the board are filled by
	 * {@link Board#INITIAL_NUMBER_OF_STONES_PER_PIT} numbers excepting 2 Kalahs,
	 * which should be left empty at the beginning.
	 */
	private void setGameBoardWithInitialStoneValues(Game game) {
		Board board = game.getBoard();
		if (!board.isFilled()) {
			log.debug("Board is not set. Filling all the pits with 6 stones each");
			Map<Integer, Integer> initialStatus = new LinkedHashMap<Integer, Integer>();

			List<Integer> storeIndexes = findKalahLocations(game);
			for (int i = 0; i < 14; i++) {
				if (!storeIndexes.contains(i)) {
					initialStatus.put(i, INITIAL_NUMBER_OF_STONES_PER_PIT);
				} else {
					initialStatus.put(i, 0);
				}
			}
			board.setStatus(initialStatus);
			board.setFilled(true);
			game.setGameStarted(true);
			log.debug("Board successfully set!");
		}
	}

	/**
	 * Find indexes of stores for each player to use it further for having them
	 * empty while filling pits
	 *
	 * @param game {@link Game}
	 * @return list of store indexes. size should be equal to number of players
	 */
	private List<Integer> findKalahLocations(Game game) {
		List<Integer> locations = game.getPlayers().stream().map(Player::getKalahIndex).collect(Collectors.toList());
		log.debug("Found kalah at locations {}", locations);
		return locations;
	}
}
