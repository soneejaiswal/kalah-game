package com.backbase.game.kalah.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backbase.game.kalah.exceptions.impl.GameNotFoundException;
import com.backbase.game.kalah.request.GameRequest;
import com.backbase.game.kalah.response.GameResponse;
import com.backbase.game.kalah.service.GameService;

/**
 * Main entry point for all the manipulations around the game. It can be used to
 * make a move, get information about intermediate state of the game and create
 * or start the new game
 *
 * @author sonee
 */
@RestController
public class KalahGameController {

	private final GameService gameService;
	private static final String BASE_URI = "/games";

	@Value("${server.port}")
	private String port;
	@Value("${server.address}")
	private String host;

	@Autowired
	public KalahGameController(GameService gameService) {
		this.gameService = gameService;
	}

	@Autowired
	GameRequest gameRequest;

	/**
	 * Create a new Game/ Restart a Game.
	 * 
	 * @return {@link GameResponse}
	 */
	@PostMapping(value = BASE_URI)
	public ResponseEntity<GameResponse> createOrRestartGame() {

		String gameUri = "http://".concat(host).concat(":").concat(port).concat(BASE_URI);
		return ResponseEntity.created(URI.create(BASE_URI)).body(gameService.createOrRestartGame(gameUri));
	}

	/**
	 * Make a new move in the game by supplying gameId and selecting a pitId/pit
	 * index {in the range of 1-14, where 7 & 14 are kalah locations (Kalah
	 * locations should be omitted here) }
	 * 
	 * @param gameId
	 * @param pitId
	 * @return {@link GameResponse}
	 */
	@PutMapping(value = "/games/{gameId}/pits/{pitId}")
	public ResponseEntity<GameResponse> makeMove(@PathVariable int gameId, @PathVariable int pitId) {

		try {
			gameRequest.setCurrentTurn(pitId);
			gameRequest.setId(gameId);
			return ResponseEntity.accepted().body(gameService.makeMove(gameRequest));
		} catch (GameNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Fetch game details/information by providing gameId
	 * 
	 * @param gameId
	 * @return {@link GameResponse}
	 */
	@GetMapping(value = "/games/{gameId}")
	public ResponseEntity<GameResponse> getGameInfo(@PathVariable int gameId) {

		try {
			return ResponseEntity.ok(gameService.gameInfo(gameId));
		} catch (GameNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
