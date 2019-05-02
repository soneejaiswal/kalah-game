package com.backbase.game.kalah.service.rules;

import org.springframework.core.Ordered;

import com.backbase.game.kalah.Game;
import com.backbase.game.kalah.service.rules.impl.CheckEmptyPitsAndSumResults;
import com.backbase.game.kalah.service.rules.impl.LastSownInEmptyPit;
import com.backbase.game.kalah.service.rules.impl.LastSownInPlayersKalah;
import com.backbase.game.kalah.service.rules.impl.SetUpGameBoard;
import com.backbase.game.kalah.service.rules.impl.SowStones;

/**
 * A set of rules can be defined when playing the game. {@link GameRules} gives
 * us main abstraction for all of rules applier in some chaining fashion using
 * {@link Ordered} starting from 0.
 * 
 * @author sonee
 * 
 * @see SetUpGameBoard
 * @see SowStones
 * @see LastSownInPlayersKalah
 * @see LastSownInEmptyPit
 * @see CheckEmptyPitsAndSumResults
 */
public interface GameRules extends PrePlay, Ordered {

	/**
	 * Apply manifested game rules, terms & conditions when taking a move in current
	 * game
	 * 
	 * @param game {@link Game}
	 */
	void applyRule(Game game);

	/**
	 * Order number signifies a sequence to apply these {@link GameRules} on a
	 * {@link Game}. in this case, the sequence chain order starts from 0
	 *
	 * @return order number
	 * @see Ordered
	 */
	int getOrder();

}
