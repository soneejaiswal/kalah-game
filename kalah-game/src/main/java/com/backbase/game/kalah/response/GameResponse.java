package com.backbase.game.kalah.response;

import java.util.List;
import java.util.Map;

import com.backbase.game.kalah.Game;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Representation for {@link Game} to render a response after set of activity
 *
 * @author sonee
 */
@JsonInclude(value = Include.NON_DEFAULT)
public class GameResponse {

	private int id;
	private String uri;
	private Map<Integer, Integer> status;

	@JsonIgnore
	private List<String> players;
	@JsonIgnore
	private boolean gameOver;
	@JsonIgnore
	private boolean gameStarted;
	@JsonIgnore
	private String winner;
	@JsonIgnore
	private String currentPlayer;

	/**
	 * 
	 * @param id            game id
	 * @param uri           game uri, which could be used to fetch game details if
	 *                      required
	 * @param status        status of the game borad, representing set of pits &
	 *                      stones in each of the pits
	 * @param players
	 * @param gameOver
	 * @param gameStarted
	 * @param winner
	 * @param currentPlayer the player who is going to move/take turn in the next
	 *                      round
	 */
	private GameResponse(int id, String uri, Map<Integer, Integer> status, List<String> players, boolean gameOver,
			boolean gameStarted, String winner, String currentPlayer) {
		this.id = id;
		this.uri = uri;
		this.status = status;
		this.players = players;
		this.gameOver = gameOver;
		this.gameStarted = gameStarted;
		this.winner = winner;
		this.currentPlayer = currentPlayer;

	}

	public static GameResponseBuilder builder() {
		return new GameResponseBuilder();
	}

	public int getId() {
		return id;
	}

	public String getUri() {
		return uri;
	}

	public Map<Integer, Integer> getStatus() {
		return status;
	}

	public List<String> getPlayers() {
		return players;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public String getWinner() {
		return winner;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Builder class for {@link GameResponse}
	 * 
	 * @author Sonee
	 *
	 */
	public final static class GameResponseBuilder {

		private int id;
		private String uri;
		private Map<Integer, Integer> status;
		private List<String> players;
		private boolean gameOver;
		private boolean gameStarted;
		private String winner;
		private String currentPlayer;

		private GameResponseBuilder() {

		}

		public GameResponseBuilder id(int id) {
			this.id = id;
			return this;
		}

		public GameResponseBuilder uri(String uri) {
			this.uri = uri;
			return this;
		}

		public GameResponseBuilder status(Map<Integer, Integer> status) {
			this.status = status;
			return this;
		}

		public GameResponseBuilder gameOver(boolean gameOver) {
			this.gameOver = gameOver;
			return this;
		}

		public GameResponseBuilder winner(String winner) {
			this.winner = winner;
			return this;
		}

		public GameResponseBuilder started(boolean gameStarted) {
			this.gameStarted = gameStarted;
			return this;
		}

		public GameResponseBuilder currentPlayer(String currentPlayer) {
			this.currentPlayer = currentPlayer;
			return this;
		}

		public GameResponseBuilder players(List<String> players) {
			this.players = players;
			return this;
		}

		public GameResponse build() {
			return new GameResponse(id, uri, status, players, gameOver, gameStarted, winner, currentPlayer);
		}
	}
	
	
}
