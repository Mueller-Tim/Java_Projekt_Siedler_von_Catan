package ch.zhaw.structures;

import java.awt.Point;

import ch.zhaw.catan.Config;
import ch.zhaw.catan.Player;

/**
 * 
 * The City class is a data class which represents a single city
 *
 */
public class City extends Settlement {

	/**
	 * Constructor which passes the owner and the position to the super class
	 *
	 * @param owner 
	 * @param position
	 */
	public City(Player owner, Point position) {
		super(owner, position, Config.CITY_RESOURCE_REWARD, Config.VICTORY_POINTS_CITY);
	}

	/**
	 * @return owner-faction in upper case
	 */
	@Override
	public String toString(){
		return getOwner().getFaction().toString().toUpperCase();
	}
}
