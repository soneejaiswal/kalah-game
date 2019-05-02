package com.backbase.game.kalah.service;

import com.backbase.game.kalah.request.GameRequest;
import com.backbase.game.kalah.response.GameResponse;

/**
 * Synchronizing up all the flows of Kalah and monitor rules to be applied for
 * each move and each player
 *
 * @author sonee
 */

public interface GameService {

	/**
	 * create or restart a {@link Game}
	 *
	 * @return {@link GameResponse}
	 */
	GameResponse createOrRestartGame(String gameUri);

	/**
	 * Loop through all the {@link GameRules} implementations and apply all the
	 * appropriate rules to the current move
	 *
	 * @param gameRequest {@link GameRequest} - information about current game and
	 *                    move
	 * @return {@link GameResponse}
	 */
	GameResponse makeMove(GameRequest gameRequest);

	/**
	 * Short overview of all the current game details (board, players, etc.)
	 *
	 * @return {@link GameResponse}
	 */
	GameResponse gameInfo(int gameId);

}
