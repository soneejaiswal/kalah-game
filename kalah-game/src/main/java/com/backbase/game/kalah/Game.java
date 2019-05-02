package com.backbase.game.kalah;

import java.util.List;

import com.backbase.game.kalah.board.Board;
import com.backbase.game.kalah.player.Player;
import com.backbase.game.kalah.turn.Turn;

/**
 * {@link Game} represents all of the attributes for kalah game including
 * {@link Board}, {@link Player}, id, game-information uri etc.
 *
 * @author sonee
 */
public class Game {

	private int id;
	private String uri;
	private final Board board = new Board();
	private List<Player> players;
	private boolean gameOver;
	private boolean gameStarted;
	private String winner;
	private Turn currentTurn;
	private Turn previousTurn;
	private Player currentPlayer;
	
	public Game(List<Player> players) {
		this.players = players;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public Turn getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(Turn currentTurn) {
		this.currentTurn = currentTurn;
	}

	public Turn getPreviousTurn() {
		return previousTurn;
	}

	public void setPreviousTurn(Turn previousTurn) {
		this.previousTurn = previousTurn;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setTakeTurnPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Board getBoard() {
		return board;
	}

	public List<Player> getPlayers() {
		return players;
	}

}
