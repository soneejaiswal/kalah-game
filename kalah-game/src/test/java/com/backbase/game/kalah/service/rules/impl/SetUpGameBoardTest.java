package com.backbase.game.kalah.service.rules.impl;

import java.util.Arrays;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.backbase.game.kalah.Game;
import com.backbase.game.kalah.player.impl.TopPlayer;
import com.backbase.game.kalah.player.impl.BottomPlayer;
import com.backbase.game.kalah.service.rules.impl.SetUpGameBoard;

/**
 * 
 * SetUpGameBoardTest
 * 
 * @author sonee
 */
public class SetUpGameBoardTest {

	private Game game;

	private final SetUpGameBoard setUpGameBoard = new SetUpGameBoard();

	@Before
	public void setUp() throws Exception {
		game = new Game(Arrays.asList(new TopPlayer(), new BottomPlayer()));
	}

	/**
	 * apply {@link SetUpGameBoard} for game over check
	 */
	@Test
	public void testIfGameIsOvered() {
		game.setGameOver(true);
		setUpGameBoard.applyRule(game);
		Assert.assertNull(game.getBoard().getStatus());
		Assert.assertTrue(game.isGameOver());
	}

	/**
	 * apply {@link SetUpGameBoard} for game not overed yet check
	 */
	@Test
	public void testIfGameIsNotOvered() {
		setUpGameBoard.applyRule(game);

		Map<Integer, Integer> initialStatusOfGameBoard = game.getBoard().getStatus();

		initialStatusOfGameBoard.forEach((pitNumber, stones) -> {
			Assert.assertEquals((long) stones, (pitNumber == 6 || pitNumber == 13) ? 0 : 6);
		});
		Assert.assertTrue("Board should be filled", game.getBoard().isFilled());
		Assert.assertTrue("Game should be started", game.isGameStarted());
		Assert.assertFalse(game.isGameOver());
	}
}