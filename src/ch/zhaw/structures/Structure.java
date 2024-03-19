package ch.zhaw.structures;

import java.util.Map;

import ch.zhaw.catan.Config;
import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Player;

/**
 * Abstract super class for structures
 *
 */
public abstract class Structure {
	private Player owner;
	private Config.Structure type;
	private Map<Config.Resource, Integer> cost;
	
	/**
	 * Constructor sets the owner, type and cost of the structure
	 * @param owner
	 * @param type
	 * @param cost
	 */
	public Structure(Player owner, Config.Structure type, Map<Config.Resource, Integer> cost) {
		this.owner = owner;
		this.type = type;
		this.cost = cost;
	}

	public Player getOwner() {
		return owner;
	}

	public Config.Structure getType() {
		return type;
	}

	public Map<Config.Resource, Integer> getCost() {
		return cost;
	}

	/**
	 * @return owner-faction
	 */
	@Override
	public String toString(){
		return getOwner().getFaction().toString();
	}
	
}
