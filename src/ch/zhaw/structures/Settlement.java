package ch.zhaw.structures;


import java.awt.Point;

import ch.zhaw.catan.Config;
import ch.zhaw.catan.Player;

/**
 * 
 * The Settlement class is a data class which represents a single settlement
 *
 */
public class Settlement extends Structure {
	private final Point position;
	private final int resourceReward;
	private final int victoryPoints;

	/**
	 * Constructor passes the owner to the super class and sets the position and victory points
	 * @param owner
	 * @param position
	 * @param victoryPoints
	 */
	public Settlement(Player owner, Point position, int resourceReward, int victoryPoints) {
		super(owner, Config.Structure.SETTLEMENT, Config.SETTLEMENT_COST);
		this.position = position;
		this.resourceReward = resourceReward;
		this.victoryPoints = victoryPoints;
	}
	
	/**
	 * Constructor passes the owner to the super class and sets the position
	 * @param owner
	 * @param position
	 */
	public Settlement(Player owner, Point position) {
		super(owner, Config.Structure.SETTLEMENT, Config.SETTLEMENT_COST);
		this.position = position;
		this.resourceReward = Config.SETTLEMENT_RESOURCE_REWARD;
		this.victoryPoints = Config.VICTORY_POINTS_SETTLEMENT;
	}

	public Point getPosition() {
		return position;
	}

	public int getResourceReward() {
		return resourceReward;
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}
}
