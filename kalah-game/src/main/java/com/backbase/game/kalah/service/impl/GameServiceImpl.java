package com.backbase.game.kalah.service.impl;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backbase.game.kalah.Game;
import com.backbase.game.kalah.exceptions.impl.GameNotFoundException;
import com.backbase.game.kalah.player.Player;
import com.backbase.game.kalah.player.impl.BottomPlayer;
import com.backbase.game.kalah.player.impl.TopPlayer;
import com.backbase.game.kalah.request.GameRequest;
import com.backbase.game.kalah.response.GameResponse;
import com.backbase.game.kalah.service.GameService;
import com.backbase.game.kalah.service.rules.GameRules;
import com.backbase.game.kalah.turn.Turn;

/**
 * Coordinate all the flows of Kalah and monitor rules to be applied for each
 * turn and each player
 *
 * @author sonee
 */
@Service
public class GameServiceImpl implements GameService {

	private Logger log = LoggerFactory.getLogger(GameServiceImpl.class);
	private final List<GameRules> rulesAppliers;
	private static Map<Integer, Game> gameMap = new LinkedHashMap<Integer, Game>();
	private static int count = 0;

	@Autowired
	public GameServiceImpl(List<GameRules> rulesAppliers) {
		this.rulesAppliers = rulesAppliers;
	}

	@Override
	public GameResponse makeMove(GameRequest gameRequest) {

		if (!gameMap.containsKey(gameRequest.getId()))
			throw new GameNotFoundException("Requested game information is not found.");

		log.debug("Received a game request {}. Setting up Game with these attributes...", gameRequest);
		Game game = populateGameWithGameRequest(gameRequest);

		log.debug("start applying game rules to a new move");
		rulesAppliers.forEach(applier -> applier.applyRule(game));
		log.debug("Game rules applied successfully.");

		log.debug("sending response");
		return createGameResponse(game);
	}

	@Override
	public GameResponse gameInfo(int gameId) {

		if (!gameMap.containsKey(gameId))
			throw new GameNotFoundException("Requested game information is not found.");
		log.debug("Received a game information request with game id - {0}", gameId);
		return createGameResponse(gameMap.get(gameId));
	}

	@Override
	public GameResponse createOrRestartGame(String gameUri) {
		log.debug("received a new request for game creation.");
		Game game = setUpInitialGame(gameUri);
		return createGameResponse(game);
	}

	/**
	 * 
	 * @return {@link Game}
	 */
	private Game setUpInitialGame(String gameUri) {

		log.debug("doing initial setup for the game by initializing default attributes.");
		count++;
		Game game = new Game(Arrays.asList(new BottomPlayer(), new TopPlayer()));
		game.setId(count);
		game.setUri(gameUri.concat("/").concat(String.valueOf(count)));
		game.setTakeTurnPlayer(game.getPlayers().get(0)); // setting bottom as default player to start the game
		gameMap.put(count, game);
		log.debug("Initial setup is done for the game - {0}", game);
		return game;
	}

	/**
	 * Convert {@link Game} attributes to {@link GameResponse} using builder class
	 * {@link GameResponseBuilders}
	 *
	 * @return {@link GameResponse}
	 */
	private GameResponse createGameResponse(Game game) {
		log.debug("creating game response...");
		return GameResponse.builder().id(game.getId()).uri(game.getUri()).status(game.getBoard().getStatus())
				.gameOver(game.isGameOver()).started(game.isGameStarted()).winner(game.getWinner())
				.players(game.getPlayers().stream().map(Player::getName).collect(Collectors.toList()))
				.currentPlayer(game.getCurrentPlayer().getName()).build();
	}

	/**
	 * populate {@link Game} with attributes received from {@link GameRequest}
	 *
	 * @param game        {@link Game}
	 * @param gameRequest {@link GameRequest}
	 */
	private Game populateGameWithGameRequest(GameRequest gameRequest) {

		Game game = gameMap.get(gameRequest.getId());
		game.setCurrentTurn(new Turn(gameRequest.getCurrentTurn()));

		return game;
	}
}
