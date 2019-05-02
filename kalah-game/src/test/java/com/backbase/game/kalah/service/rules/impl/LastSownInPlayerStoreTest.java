package com.backbase.game.kalah.service.rules.impl;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.backbase.game.kalah.Game;
import com.backbase.game.kalah.player.Player;
import com.backbase.game.kalah.player.impl.TopPlayer;
import com.backbase.game.kalah.player.impl.BottomPlayer;
import com.backbase.game.kalah.service.rules.impl.LastSownInPlayersKalah;
import com.backbase.game.kalah.service.rules.impl.SetUpGameBoard;
import com.backbase.game.kalah.service.rules.impl.SowStones;
import com.backbase.game.kalah.turn.Turn;

/**
 * LastSownInPlayerStoreTest
 *
 * @author sonee
 */
public class LastSownInPlayerStoreTest {

	private Game game;

	private final SetUpGameBoard fillBoard = new SetUpGameBoard();
	private final SowStones sowStones = new SowStones();
	private final LastSownInPlayersKalah lastSownInPlayerStore = new LastSownInPlayersKalah();

	@Before
	public void setUp() throws Exception {
		game = new Game(Arrays.asList(new BottomPlayer(), new TopPlayer()));
		fillBoard.applyRule(game);
		game.setCurrentTurn(new Turn(3));
		game.setTakeTurnPlayer(new BottomPlayer());
	}

	/**
	 * sow set of stones & check for last sown stone
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLastSownInTheKalah() throws Exception {
		// changing value to 3 to put the last stone into the player store
		game.getBoard().getStatus().put(3, 3);
		sowStones.applyRule(game);
		Turn currentTurn = game.getCurrentTurn();
		Player takeTurnPlayer = game.getCurrentPlayer();
		lastSownInPlayerStore.applyRule(game);

		Assert.assertEquals("take-turn-player should be the same when the last stone is put into the player's store",
				takeTurnPlayer, game.getCurrentPlayer());
		Assert.assertNull("current turn should be null to force to quit from chain", game.getCurrentTurn());
		Assert.assertEquals("current turn value should be set to previous turn variable", currentTurn,
				game.getPreviousTurn());
	}

	@Test
	public void testGameIsOver() {
		game.setGameOver(true);

		// changing value to 3 to put the last stone into the player store
		game.getBoard().getStatus().put(3, 3);
		sowStones.applyRule(game);
		Turn currentTurn = game.getCurrentTurn();
		/* Player takeTurnPlayer = */ game.getCurrentPlayer();
		lastSownInPlayerStore.applyRule(game);

		Assert.assertNotNull("if game is over, current turn should NOT be null", game.getCurrentTurn());
		Assert.assertNotEquals("If game is over, previous turn should NOT be set with current turn",
				game.getPreviousTurn(), currentTurn);
	}

}