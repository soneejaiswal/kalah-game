package com.backbase.game.kalah.request;

import java.io.Serializable;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Game Request to manifest results of current turn
 *
 * @author sonee
 */
@Component
public class GameRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5336837577506628415L;

	/**
	 * current player who took the turn
	 */
	@NonNull
	private String currentPlayerName;
	/**
	 * chosen pit from which move should be made
	 */
	private int currentTurn;
	private int id;

	public GameRequest() {

	}

	public GameRequest(String currentPlayerName) {
		this.currentPlayerName = currentPlayerName;
	}

	public String getCurrentPlayerName() {
		return currentPlayerName;
	}

	public void setCurentPlayerName(String currentPlayerName) {
		this.currentPlayerName = currentPlayerName;
	}

	public int getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((currentPlayerName == null) ? 0 : currentPlayerName.hashCode());
//		result = prime * result + currentTurn;
//		result = prime * result + id;
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		GameRequest other = (GameRequest) obj;
//		if (currentPlayerName == null) {
//			if (other.currentPlayerName != null)
//				return false;
//		} else if (!currentPlayerName.equals(other.currentPlayerName))
//			return false;
//		if (currentTurn != other.currentTurn)
//			return false;
//		if (id != other.id)
//			return false;
//		return true;
//	}
//
//	@Override
//	public String toString() {
//		return "GameRequest [currentPlayerName=" + currentPlayerName + ", currentTurn=" + currentTurn + ", id=" + id
//				+ "]";
//	}

}
