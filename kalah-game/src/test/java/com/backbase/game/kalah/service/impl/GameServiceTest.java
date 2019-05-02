package com.backbase.game.kalah.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.backbase.game.kalah.player.impl.TopPlayer;
import com.backbase.game.kalah.exceptions.impl.GameNotFoundException;
import com.backbase.game.kalah.player.impl.BottomPlayer;
import com.backbase.game.kalah.request.GameRequest;
import com.backbase.game.kalah.response.GameResponse;
import com.backbase.game.kalah.service.GameService;
import com.backbase.game.kalah.service.rules.GameRules;
import com.backbase.game.kalah.service.rules.impl.CheckEmptyPitsAndSumResults;
import com.backbase.game.kalah.service.rules.impl.LastSownInEmptyPit;
import com.backbase.game.kalah.service.rules.impl.LastSownInPlayersKalah;
import com.backbase.game.kalah.service.rules.impl.SetUpGameBoard;
import com.backbase.game.kalah.service.rules.impl.SowStones;

/**
 * {@link GameServiceTest} to test {@link GameService}
 * 
 * @see GameServiceImpl
 *
 * @author sonee
 */
public class GameServiceTest {

	private static GameServiceImpl gameService;
	private static GameResponse response;

	private static final String TOP = new TopPlayer().getName();
	private static final String BOTTOM = new BottomPlayer().getName();
	private static String uri = "http://localhost:8080/games";

	@BeforeClass
	public static void setUp() throws Exception {

		CheckEmptyPitsAndSumResults checkEmptyPitsAndSumResults = new CheckEmptyPitsAndSumResults();
		LastSownInEmptyPit lastSownInEmptyPit = new LastSownInEmptyPit();
		LastSownInPlayersKalah lastSownInPlayerStore = new LastSownInPlayersKalah();
		SetUpGameBoard fillBoard = new SetUpGameBoard();
		SowStones sowStones = new SowStones();

		List<GameRules> rulesAppliers = new LinkedList<GameRules>();

		rulesAppliers.add(fillBoard);
		rulesAppliers.add(sowStones);
		rulesAppliers.add(lastSownInPlayerStore);
		rulesAppliers.add(lastSownInEmptyPit);
		rulesAppliers.add(checkEmptyPitsAndSumResults);

		gameService = new GameServiceImpl(rulesAppliers);

		response = gameService.createOrRestartGame(uri);
	}

	/**
	 * test to create a fresh game
	 */
	@Test
	public void testCreateGame() {

		Assert.assertEquals(1, response.getId());
		Assert.assertEquals("http://localhost:8080/games/1", response.getUri());
		Assert.assertNull(response.getStatus());
	}

	/**
	 * test to make a move in the created game
	 */
	@Test
	public void testMakeMove() {

		gameService.createOrRestartGame(uri);

		GameRequest gameRequest = new GameRequest(BOTTOM);
		gameRequest.setCurrentTurn(0);
		gameRequest.setId(2);

		GameResponse response = gameService.makeMove(gameRequest);

		Map<Integer, Integer> intemediateBoardStatus = response.getStatus();
		int[] stoneArray = new int[] { 0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0 };
		intemediateBoardStatus.forEach((pitNumber, stones) -> {
			Assert.assertEquals((long) stones, stoneArray[pitNumber]);
		});

		Assert.assertEquals("Bottom player should be given additional move", BOTTOM, response.getCurrentPlayer());
		Assert.assertNull("No winner yet", response.getWinner());
	}

	/**
	 * test to make a move in the game. Make move again (This turn would be for
	 * another player) & test change in the state of the game like player name,
	 * status of board etc
	 */
	@Test
	public void testMakeMoveAndMakeMoveAgain() {

		// move is made by first player
		GameRequest gameRequest = new GameRequest();
		gameRequest.setCurrentTurn(0);
		gameRequest.setId(1);

		GameResponse response = gameService.makeMove(gameRequest);

		Map<Integer, Integer> intemediateBoardStatus = response.getStatus();
		int[] stoneArray = new int[] { 0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0 };
		intemediateBoardStatus.forEach((pitNumber, stones) -> {
			Assert.assertEquals((long) stones, stoneArray[pitNumber]);
		});

		Assert.assertEquals(BOTTOM, response.getCurrentPlayer());

		// move is made by second player
		GameRequest anotherGameRequest = new GameRequest();
		anotherGameRequest.setId(1);
		anotherGameRequest.setCurrentTurn(7);

		response = gameService.makeMove(anotherGameRequest);
		response = gameService.makeMove(anotherGameRequest);

		int[] newStoneArray = new int[] { 0, 7, 7, 7, 7, 7, 1, 0, 7, 7, 7, 7, 7, 1 };
		response.getStatus().forEach((pitNumber, stones) -> {
			Assert.assertEquals((long) stones, newStoneArray[pitNumber]);
		});

		Assert.assertEquals(TOP, response.getCurrentPlayer());

	}

	/**
	 * test to fetch game details
	 */
	@Test
	public void testGameDetails() {
		GameResponse response = gameService.gameInfo(1);
		Assert.assertNotNull(response);
		Assert.assertEquals(TOP, response.getCurrentPlayer());
		Assert.assertEquals(1, response.getId());
		Assert.assertEquals("http://localhost:8080/games/1", response.getUri());

		int[] newStoneArray = new int[] { 0, 7, 7, 7, 7, 7, 1, 0, 7, 7, 7, 7, 7, 1 };
		response.getStatus().forEach((pitNumber, stones) -> {
			Assert.assertEquals((long) stones, newStoneArray[pitNumber]);
		});
	}

	/**
	 * test to fetch game details of no existing game. An Exception
	 * {@link GameNotFoundException} is expected here
	 */
	@Test(expected = GameNotFoundException.class)
	public void testFailedToReadGameDetails() {
		GameResponse response = gameService.gameInfo(10);
		Assert.assertNull(response);
	}

}