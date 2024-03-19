package ch.zhaw.catan;

import java.util.HashMap;

/**
 * The Bank class represents a non playable resource holder
 *
 */
public class Bank extends ResourceHolder {

	/**
	 * Default constructor passes the initial resource cards from a config file to
	 * its super class
	 */
	public Bank() {
		super(new HashMap<Config.Resource, Integer>(Config.INITIAL_RESOURCE_CARDS_BANK));
	}
}
