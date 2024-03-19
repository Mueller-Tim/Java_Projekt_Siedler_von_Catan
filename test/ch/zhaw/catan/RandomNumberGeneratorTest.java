package ch.zhaw.catan;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The RandomNumberGeneratorTest class contains JUnit tests for testing the RandomNumberGenerator class.
 */
class RandomNumberGeneratorTest {
	private static final RandomNumberGenerator rng = new RandomNumberGenerator();

	/**
	 * description: the dice throw should be always in the range from two to twelve
	 * equivalence class: 1
	 * initial condition: test dice throw in a loop of 1_000 times
	 * type: positive test
	 * input: actual dice throw should be in the range from two to twelve
	 * output: boolean true
	 */
	@Test
	void testRangeOfToDiceThrown() {
		for (int i = 0; i < 1000; i++) {
			int actual = rng.throwTwoDice();
			assertTrue(actual >= 2);
			assertTrue(actual <= 12);
		}
	}

	/**
	 * description: it should always randomly return a valid resource
	 * equivalence class: 1
	 * initial condition: it generates in a loop of 1_000 times randomly resources
	 * type: positive test
	 * input: every randomly generated resource should be valid
	 * output: boolean true
	 */
	@Test
	void testThatResourceMatchesRequirements() {
		for (int i = 0; i < 1000; i++) {
			boolean isOfEnumType = false;
			Config.Resource actual = rng.getRandomResource();
			for (Config.Resource resource : Config.Resource.values()) {
				if (resource.equals(actual)) {
					isOfEnumType = true;
				}
			}
			assertTrue(isOfEnumType);
		}
	}
}