package com.backbase.game.kalah.service.rules.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.backbase.game.kalah.Game;
import com.backbase.game.kalah.player.impl.BottomPlayer;
import com.backbase.game.kalah.player.impl.TopPlayer;

/**
 * CheckEmptyPitsAndSumResultsTest
 *
 * @author sonee
 */
public class CheckEmptyPitsAndSumResultsTest {

	private CheckEmptyPitsAndSumResults checkEmptyPitsAndSumResults = new CheckEmptyPitsAndSumResults();
	private SetUpGameBoard setUpGameBoard = new SetUpGameBoard();
	private Game game;

	@Before
	public void setUp() throws Exception {
		game = new Game(Arrays.asList(new BottomPlayer(), new TopPlayer()));
		setUpGameBoard.applyRule(game);
	}

	/**
	 * pits are empty
	 * 
	 */
	@Test
	public void applyRuleWithoutEmptyPits() {
		checkEmptyPitsAndSumResults.applyRule(game);

		assertFalse(game.isGameOver());
		assertNull(game.getWinner());
	}

	/**
	 * test bottom winner condition with empty pits
	 */
	@Test
	public void applyRuleWithEmptyPitsAndBottomPlayerAsWinner() throws Exception {
		Map<Integer, Integer> gameBoardStatus = game.getBoard().getStatus();
		for (int i = 0; i < 14; i++)
			gameBoardStatus.put(i, 0);
		gameBoardStatus.put(6, 50);
		gameBoardStatus.put(13, 22);

		checkEmptyPitsAndSumResults.applyRule(game);

		assertTrue(game.isGameOver());
		assertEquals(new BottomPlayer().getName(), game.getWinner());

		//
		Map<Integer, Integer> testStatusMap = new LinkedHashMap<Integer, Integer>();
		for (int i = 0; i < 14; i++)
			testStatusMap.put(i, 0);
		testStatusMap.put(6, 50);
		testStatusMap.put(13, 22);

		Assert.assertEquals("After game is finished, all the pits should be empty", testStatusMap,
				game.getBoard().getStatus());
	}

	/**
	 * test top winner conditions with empty pits
	 */
	@Test
	public void applyRuleWithEmptyAndTopPlayerAsWinner() {
		Map<Integer, Integer> gameStatusBoard = game.getBoard().getStatus();
		for (int i = 0; i < 14; i++)
			gameStatusBoard.put(i, 0);
		gameStatusBoard.put(6, 32);
		gameStatusBoard.put(13, 40);

		checkEmptyPitsAndSumResults.applyRule(game);

		assertTrue(game.isGameOver());

		//
		Map<Integer, Integer> testStatusMap = new LinkedHashMap<Integer, Integer>();
		for (int i = 0; i < 14; i++)
			testStatusMap.put(i, 0);
		testStatusMap.put(6, 32);
		testStatusMap.put(13, 40);

		Assert.assertEquals("After game is finished, all the pits should be empty", testStatusMap,
				game.getBoard().getStatus());
	}

	/**
	 * test draw condition with empty pits
	 * 
	 */
	@Test
	public void applyRuleWithEmptyPitsAndDraw() {
		Map<Integer, Integer> gameStatusBoard = game.getBoard().getStatus();
		for (int i = 0; i < 14; i++) {
			gameStatusBoard.put(i, 0);
		}
		gameStatusBoard.put(6, 36);// setting 36 to bottom store
		gameStatusBoard.put(13, 36); // the same should be in the top store

		checkEmptyPitsAndSumResults.applyRule(game);

		assertTrue("Game should be over", game.isGameOver());
		assertEquals("DRAW should be set since game is over", "DRAW", game.getWinner());

		Map<Integer, Integer> testStatusMap = new LinkedHashMap<Integer, Integer>();
		for (int i = 0; i < 14; i++)
			testStatusMap.put(i, 0);
		testStatusMap.put(6, 36);
		testStatusMap.put(13, 36);
		Assert.assertEquals("After game is finished, all the pits should be empty", testStatusMap,
				game.getBoard().getStatus());
	}
}