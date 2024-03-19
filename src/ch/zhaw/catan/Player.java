package ch.zhaw.catan;

import java.util.HashMap;
import java.util.Map;

import ch.zhaw.catan.Config.Resource;

/**
 * The Player class is a data class which represents a single player
 *
 */
public class Player extends ResourceHolder{
	private Config.Faction faction;


	/**
	 * Sets the faction of the player
	 *
	 * @param faction from the player
	 */
	public Player(Config.Faction faction) {
		super(new HashMap<Resource, Integer>(Config.INITIAL_RESOURCE_CARDS_PLAYER));
		this.faction = faction;
	}


	public Config.Faction getFaction() {
		return faction;
	}
}
