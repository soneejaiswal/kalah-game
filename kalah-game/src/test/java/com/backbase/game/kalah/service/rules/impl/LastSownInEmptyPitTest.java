package com.backbase.game.kalah.service.rules.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.backbase.game.kalah.Game;
import com.backbase.game.kalah.player.Player;
import com.backbase.game.kalah.player.impl.TopPlayer;
import com.backbase.game.kalah.player.impl.BottomPlayer;
import com.backbase.game.kalah.service.rules.impl.LastSownInEmptyPit;
import com.backbase.game.kalah.service.rules.impl.LastSownInPlayersKalah;
import com.backbase.game.kalah.service.rules.impl.SetUpGameBoard;
import com.backbase.game.kalah.service.rules.impl.SowStones;
import com.backbase.game.kalah.turn.Turn;

/**
 * LastSownInPlayerStoreTest
 *
 * @author sonee
 */
public class LastSownInEmptyPitTest {

	private Game game;

	private final SetUpGameBoard fillBoard = new SetUpGameBoard();
	private final SowStones sowStones = new SowStones();
	private final LastSownInPlayersKalah lastSownInPlayerStore = new LastSownInPlayersKalah();
	private final LastSownInEmptyPit lastSownInEmptyPit = new LastSownInEmptyPit();

	@Before
	public void setUp() throws Exception {
		game = new Game(Arrays.asList(new BottomPlayer(), new TopPlayer()));
		fillBoard.applyRule(game);
		game.setCurrentTurn(new Turn(3));
		game.setTakeTurnPlayer(new BottomPlayer());
	}

	/**
	 * 
	 */
	@Test
	public void testCapturing() {
		// changing value to 13 to put the last stone into the pit, where we are
		// starting
		Map<Integer, Integer> gameBoardStatus = game.getBoard().getStatus();
		gameBoardStatus.put(3, 13);
		sowStones.applyRule(game);

		lastSownInPlayerStore.applyRule(game);
		Turn futurePreviousTurn = game.getCurrentTurn();
		Player futurePreviousTakeTurnPlayer = game.getCurrentPlayer();
		lastSownInEmptyPit.applyRule(game);

		int playerStoreValue = game.getBoard().getByIndex(futurePreviousTakeTurnPlayer.getKalahIndex());

		assertEquals("store value should be 9 since we are capturing 1+7 from pits to 1 in the store", 9,
				playerStoreValue);

		assertEquals("Another player should take a turn", "top", game.getCurrentPlayer().getName());
		assertEquals(new Integer(0), gameBoardStatus.get(3));
		assertEquals(new Integer(0), gameBoardStatus.get(9));
		assertNull("Current turn should be null", game.getCurrentTurn());
		assertEquals("Current turn should become previous turn", futurePreviousTurn, game.getPreviousTurn());
	}

	/**
	 * test to check player can not move stones from pits which are not in their
	 * control
	 */
	@Test
	public void testPlayerCannotCaptureFromAnotherPlayersPit() {
		Turn currentTurn = new Turn(1);
		currentTurn.setLastSownIndex(12);
		game.setCurrentTurn(currentTurn);

		lastSownInEmptyPit.applyRule(game);

		assertEquals("Another player should take a turn", "top", game.getCurrentPlayer().getName());
		assertNull("Current turn should be null", game.getCurrentTurn());
		assertEquals("Current turn should become previous turn", currentTurn, game.getPreviousTurn());
	}

	/**
	 * test make move by applying the game rule
	 */
	@Test
	public void testPlayersTakeMove() {
		Turn currentTurn = new Turn(1);
		currentTurn.setLastSownIndex(12);
		game.setCurrentTurn(currentTurn);

		lastSownInEmptyPit.applyRule(game);

		assertEquals("Another player should take a turn", "top", game.getCurrentPlayer().getName());
		assertNull("Current turn should be null", game.getCurrentTurn());
		assertEquals("Current turn should become previous turn", currentTurn, game.getPreviousTurn());

		// do the next move from the last sown pit index
		currentTurn = new Turn(currentTurn.getLastSownIndex());
		currentTurn.setLastSownIndex(2);
		game.setCurrentTurn(currentTurn);

		lastSownInEmptyPit.applyRule(game);

		assertEquals("Another player should take a turn", "bottom", game.getCurrentPlayer().getName());
		assertNull("Current turn should be null", game.getCurrentTurn());
		assertEquals("Current turn should become previous turn", currentTurn, game.getPreviousTurn());

	}

	@Test
	public void testTryingToCaptureWithEmptyOppositePit() throws Exception {
		// changing value to 13 to put the last stone into the pit, where we are
		// starting
		Map<Integer, Integer> gameBoardStatus = game.getBoard().getStatus();
		gameBoardStatus.put(3, 13);
		sowStones.applyRule(game);

		lastSownInPlayerStore.applyRule(game);
		Turn futurePreviousTurn = game.getCurrentTurn();
		Player futurePreviousTakeTurnPlayer = game.getCurrentPlayer();

		// setting opposite pit to 0
		gameBoardStatus.put(9, 0);

		lastSownInEmptyPit.applyRule(game);

		int playerStoreValue = game.getBoard().getByIndex(futurePreviousTakeTurnPlayer.getKalahIndex());

		assertEquals("store value should be 1 since opposite pit is empty", 1, playerStoreValue);

		assertEquals("Another player should take a turn", "top", game.getCurrentPlayer().getName());
		assertEquals(1, (int) gameBoardStatus.get(3));
		assertEquals(0, (int) gameBoardStatus.get(9));
		assertNull("Current turn should be null", game.getCurrentTurn());
		assertEquals("Current turn should become previous turn", futurePreviousTurn, game.getPreviousTurn());

	}

	@Test
	public void applyRuleWithPitNotOwnedByPlayer() throws Exception {
		// changing value to 5 to put the last stone into the pit owned by another
		// player
		Map<Integer, Integer> gameBoardstatus = game.getBoard().getStatus();
		gameBoardstatus.put(3, 5);
		gameBoardstatus.put(8, 0);
		sowStones.applyRule(game);

		lastSownInPlayerStore.applyRule(game);
		Turn futurePreviousTurn = game.getCurrentTurn();
		Player futurePreviousTakeTurnPlayer = game.getCurrentPlayer();
		lastSownInEmptyPit.applyRule(game);

		int playerStoreValue = game.getBoard().getByIndex(futurePreviousTakeTurnPlayer.getKalahIndex());

		assertEquals("store value should NOT be increased by capturing", 1, playerStoreValue);

		assertEquals("Another player should take a turn", "top", game.getCurrentPlayer().getName());
		assertNull("Current turn should be null", game.getCurrentTurn());
		assertEquals("Current turn should become previous turn", futurePreviousTurn, game.getPreviousTurn());
	}
}