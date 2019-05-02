package com.backbase.game.kalah.service.rules.impl;

import java.util.Arrays;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.backbase.game.kalah.Game;
import com.backbase.game.kalah.board.Board;
import com.backbase.game.kalah.player.impl.TopPlayer;
import com.backbase.game.kalah.player.impl.BottomPlayer;
import com.backbase.game.kalah.service.rules.impl.SetUpGameBoard;
import com.backbase.game.kalah.service.rules.impl.SowStones;
import com.backbase.game.kalah.turn.Turn;

/**
 * SowStonesTest
 *
 * @author sonee
 */
public class SowStonesTest {

	private Game game;
	private final SetUpGameBoard setUpGameBoard = new SetUpGameBoard();
	private final SowStones sowStones = new SowStones();

	@Before
	public void setUp() throws Exception {
		game = new Game(Arrays.asList(new BottomPlayer(), new TopPlayer()));
		setUpGameBoard.applyRule(game);
	}

	/**
	 * test checks if player is able to make a move by selecting pits under his own
	 * control
	 */
	@Test
	public void applyRuleForCorrectPlayer() {
		game.setCurrentTurn(new Turn(3));
		game.setTakeTurnPlayer(new BottomPlayer());

		sowStones.applyRule(game);

		Map<Integer, Integer> intermediateBoardStatus = game.getBoard().getStatus();
		int[] stoneArray = new int[] { 6, 6, 6, 0, 7, 7, 1, 7, 7, 7, 6, 6, 6, 0 };
		intermediateBoardStatus.forEach((pit, stones) -> {
			Assert.assertEquals((long) stones, stoneArray[pit]);
		});

		Assert.assertEquals("After sowing, last sown index for current turn should be kept", 9,
				game.getCurrentTurn().getLastSownIndex());

	}

	/**
	 * 
	 * test checks, player can not take turn/move stones from the opposite player's
	 * pits. This is simply, as those pits are not in its control
	 */
	@Test
	public void applyRuleForIncorrectPlayer() {
		game.setCurrentTurn(new Turn(3));
		game.setTakeTurnPlayer(new TopPlayer());
		Board boardBeforeSowing = game.getBoard();
		sowStones.applyRule(game);
		Assert.assertEquals("Sowing should NOT happen since player cannot take a turn", boardBeforeSowing.getStatus(),
				game.getBoard().getStatus());
	}

	/**
	 * tests, if n(large) number of stones are sown in a correct order & fashion
	 * following all game rules, then the last sown stone falls in correct pit
	 * 
	 * @throws Exception
	 */
	@Test
	public void applyRuleWithNStonesInThePitToGetCorrectPitIndexAfterSowing() throws Exception {
		game.setCurrentTurn(new Turn(3));
		game.setTakeTurnPlayer(new BottomPlayer());
		game.getBoard().getStatus().put(3, 20);

		sowStones.applyRule(game);
		Map<Integer, Integer> intemediateBoardStatus = game.getBoard().getStatus();
		int[] stoneArray = new int[] { 7, 7, 7, 1, 8, 8, 2, 8, 8, 8, 8, 7, 7, 0 };
		intemediateBoardStatus.forEach((pit, stones) -> {
			Assert.assertEquals((long) stones, stoneArray[pit]);
		});

		Assert.assertEquals("After sowing, last sown index for current turn should be kept", 10,
				game.getCurrentTurn().getLastSownIndex());

	}
}