package ch.zhaw.catan;

import java.util.Random;

/**
 * The RandomNumberGenerator Class is responsible for providing pseudo random events
 */
public class RandomNumberGenerator {
	Random rand = new Random();

	/**
	 * This method rolls two dice from 1 to 6 and then adds up the numbers
	 *
	 * @return int between 2 and 12
	 */
	public int throwTwoDice() {
		int result = rand.nextInt(6) + 1 + rand.nextInt(6) + 1;
		return result;
	}

	/**
	 * Returns a random element of Config.Resource
	 * @return Config.Resource by random choice
	 * 
	 */
	public Config.Resource getRandomResource() {
		switch (rand.nextInt(4) + 1) {
		case 1:
			return Config.Resource.LUMBER;
		case 2:
			return Config.Resource.WOOL;
		case 3:
			return Config.Resource.ORE;
		case 4:
			return Config.Resource.GRAIN;
		default:
			return null;
		}
	}
}
