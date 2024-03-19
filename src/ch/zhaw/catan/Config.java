package ch.zhaw.catan;

import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class specifies the most important and basic parameters of the game
 * Catan.
 * <p>
 * The class provides definitions such as for the type and number of resource
 * cards or the number of available road elements per player. Furthermore, it
 * provides a dice number to field and a field to land type mapping for the
 * standard setup detailed <a href=
 * "https://www.catan.de/files/downloads/4002051693602_catan_-_das_spiel_0.pdf">here</a>
 * </p>
 *
 * @author tebe
 */
public class Config {
	public static int getVictoryPointsSettlement() {
		return VICTORY_POINTS_SETTLEMENT;
	}

	// Minimum number of players
	// Note: The max. number is equal to the number of factions (see Faction enum)
	public static final int MIN_NUMBER_OF_PLAYERS = 2;
	public static final int MAX_NUMBER_OF_PLAYERS = Faction.values().length;
	public static final int MIN_BUILDING_TYPE_SELECTION = 1;
	public static final int MAX_BUILDING_TYPE_SELECTION = 4;
	public static final int MIN_ACTION_SELECTION = 1;
	public static final int MAX_ACTION_SELECTION = 4;
	public static final int REQUIRED_WIN_POINTS = 3;
	public static final int WIN_POINTS = 7;
	public static final int VICTORY_POINTS_CITY = 2;
	public static final int VICTORY_POINTS_SETTLEMENT = 1;
	public static final int VICTORY_POINTS_LONGEST_ROAD = 2;
	public static final int LONGEST_ROAD_THRESHOLD = 4;
	public static final Map<Resource, Integer> SETTLEMENT_COST = Map.ofEntries(Map.entry(Resource.LUMBER, 1),
			Map.entry(Resource.GRAIN, 1), Map.entry(Resource.WOOL, 1), Map.entry(Resource.BRICK, 1));
	public static final Map<Resource, Integer> CITY_COST = Map.ofEntries(Map.entry(Resource.ORE, 3),
			Map.entry(Resource.GRAIN, 2));
	public static final Map<Resource, Integer> ROAD_COST = Map.ofEntries(Map.entry(Resource.LUMBER, 1),
			Map.entry(Resource.BRICK, 1));
	public static final int SETTLEMENT_RESOURCE_REWARD = 1;
	public static final int CITY_RESOURCE_REWARD = 2;

	/**
	 * This {@link Enum} specifies the available info messages  in the game.
	 */
	public enum InfoMessage {
		GREETING("=================\nSettlers of CATAN\n================="),
		DICE_THROW("Your dice roll was: %s"),
		GAME_STATE("Current game state:\n%s"),
		TURN_ANNOUNCEMENT("It is now Player %s's turn"), WINNER("Player %s has won!"),
		RESOURCE_DISTRIBUTION("%s got: %s"),
		WIN_POINT_STANDING("%s has %s win points"),
		PLAYER_INVENTORY("Inventory:\n%s Lumber\n%s Brick\n%s Grain\n%s Wool\n%s Ore");

		private String value;

		InfoMessage(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	/**
	 * This {@link Enum} specifies the available success messages in the game.
	 */
	public enum SuccessMessage {
		BUILD("%s has been built successfully"), TRADE_WITH_BANK("Trade with bank was successful.");

		private String value;

		SuccessMessage(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	/**
	 * This {@link Enum} specifies the available error messages in the game.
	 */
	public enum ErrorMessage {
		BUILDING(
				"Your %s couldn't be built.\nCheck if you've got the required ressources and that the coordinates are valid & legal"),
		ACTION("This is an invalid action"), BUILDING_TYPE("This is an invalid building type"),
		NUMBER_OF_PLAYERS("Your input was not a number between 2 and 4."),
		TRADE_WITH_BANK("Trade with bank is not possible."),
		INVALID_COORDINATES("The Coordinates you supplied did not match the expected format of x:y");

		private String value;

		ErrorMessage(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	/**
	 * This {@link Enum} specifies the available prompt messages in the game.
	 */
	public enum PromptMessage {
		BUILDING_TYPE("What do you want to build?"),
		ACTION("What action would you like to perform?"),
		NUMBER_OF_PLAYERS("Please enter the number of players"),
		ROAD_BUILDING_POINT("Where do you want to build the road?"),
		COORDINATE("Please enter the %s coordinates: "),
		TRADE_RESOURCE_WANT("Which resource do you want?"),
		TRADE_RESOURCE_OFFER("Which resource are you offering?");

		private String value;

		PromptMessage(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}

	// Initial thief position (on the desert field)
	public static final Point INITIAL_THIEF_POSITION = new Point(7, 11);

	/**
	 * This {@link Enum} specifies the available commands in the game.
	 */
	public enum Commands {
		BUILD, TRADE, SHOW_INVENTORY, END_TURN;
	}

	/**
	 * This {@link Enum} specifies the available build commands in the game.
	 */
	public enum BuildCommands {
		SETTLEMENT, CITY, ROAD, CANCEL;
	}

	/**
	 * This {@link Enum} specifies the available colors in the game.
	 */
	public enum Faction {
		RED("rr"), BLUE("bb"), GREEN("gg"), YELLOW("yy");

		private final String name;

		private Faction(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	// RESOURCE CARD DECK
	public static final Map<Resource, Integer> INITIAL_RESOURCE_CARDS_BANK = Map.of(Resource.LUMBER, 19, Resource.BRICK,
			19, Resource.WOOL, 19, Resource.GRAIN, 19, Resource.ORE, 19);

	public static final Map<Resource, Integer> INITIAL_RESOURCE_CARDS_PLAYER = Map.of(Resource.LUMBER, 0,
			Resource.BRICK, 0, Resource.WOOL, 0, Resource.GRAIN, 0, Resource.ORE, 0);

	// SPECIFICATION OF AVAILABLE RESOURCE TYPES

	/**
	 * This {@link Enum} specifies the available resource types in the game.
	 *
	 * @author tebe
	 */
	public enum Resource {
		GRAIN("GR"), WOOL("WL"), LUMBER("LU"), ORE("OR"), BRICK("BR");

		private final String name;

		private Resource(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	// SPECIFICATION OF AVAILABLE LAND TYPES

	/**
	 * This {@link Enum} specifies the available lands in the game. Some land types
	 * produce resources (e.g., {@link Land#FOREST}, others do not (e.g.,
	 * {@link Land#WATER}.
	 *
	 * @author tebe
	 */
	public enum Land {
		FOREST(Resource.LUMBER), PASTURE(Resource.WOOL), FIELDS(Resource.GRAIN), MOUNTAIN(Resource.ORE),
		HILLS(Resource.BRICK), WATER("~~"), DESERT("--");

		private Resource resource = null;
		private final String name;

		private Land(Resource resource) {
			this(resource.toString());
			this.resource = resource;
		}

		private Land(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}

		/**
		 * Returns the {@link Resource} that this land provides or null, if it does not
		 * provide any.
		 *
		 * @return the {@link Resource} or null
		 */
		public Resource getResource() {
			return resource;
		}
	}

	// STRUCTURES (with costs)
	private static final int NUMBER_OF_ROADS_PER_PLAYER = 15;
	private static final int NUMBER_OF_SETTLEMENTS_PER_PLAYER = 5;
	private static final int NUMBER_OF_CITIES_PER_PLAYER = 4;
	public static final int MAX_CARDS_IN_HAND_NO_DROP = 7;

	/**
	 * This enum models the different structures that can be built.
	 * <p>
	 * The enum provides information about the cost of a structure and how many of
	 * these structures are available per player.
	 * </p>
	 */
	public enum Structure {
		SETTLEMENT(List.of(Resource.LUMBER, Resource.BRICK, Resource.WOOL, Resource.GRAIN),
				NUMBER_OF_SETTLEMENTS_PER_PLAYER),
		CITY(List.of(Resource.ORE, Resource.ORE, Resource.ORE, Resource.GRAIN, Resource.GRAIN),
				NUMBER_OF_CITIES_PER_PLAYER),
		ROAD(List.of(Resource.LUMBER, Resource.BRICK), NUMBER_OF_ROADS_PER_PLAYER);

		private final List<Resource> costs;
		private final int stockPerPlayer;

		private Structure(List<Resource> costs, int stockPerPlayer) {
			this.costs = costs;
			this.stockPerPlayer = stockPerPlayer;
		}

		/**
		 * Returns the build costs of this structure.
		 * <p>
		 * Each list entry represents a resource card. The value of an entry (e.g.,
		 * {@link Resource#LUMBER}) identifies the resource type of the card.
		 * </p>
		 *
		 * @return the build costs
		 */
		public List<Resource> getCosts() {
			return costs;
		}

		/**
		 * Returns the build costs of this structure.
		 *
		 * @return the build costs in terms of the number of resource cards per resource
		 *         type
		 */
		public Map<Resource, Long> getCostsAsMap() {
			return costs.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		}

		/**
		 * Returns the number of pieces that are available of a certain structure (per
		 * player). For example, there are {@link Config#NUMBER_OF_ROADS_PER_PLAYER}
		 * pieces of the structure {@link Structure#ROAD} per player.
		 *
		 * @return the stock per player
		 */
		public int getStockPerPlayer() {
			return stockPerPlayer;
		}
	}

	// STANDARD FIXED DICE NUMBER TO FIELD SETUP

	/**
	 * Returns a mapping of the dice values per field.
	 *
	 * @return the dice values per field
	 */
	public static final Map<Point, Integer> getStandardDiceNumberPlacement() {

		return Map.ofEntries(Map.entry(new Point(4, 8), 2), Map.entry(new Point(7, 5), 3),
				Map.entry(new Point(8, 14), 3), Map.entry(new Point(6, 8), 4), Map.entry(new Point(7, 17), 4),
				Map.entry(new Point(3, 11), 5), Map.entry(new Point(8, 8), 5), Map.entry(new Point(5, 5), 6),
				Map.entry(new Point(9, 11), 6), Map.entry(new Point(7, 11), 7), Map.entry(new Point(9, 5), 8),
				Map.entry(new Point(5, 17), 8), Map.entry(new Point(5, 11), 9), Map.entry(new Point(11, 11), 9),
				Map.entry(new Point(4, 14), 10), Map.entry(new Point(10, 8), 10), Map.entry(new Point(6, 14), 11),
				Map.entry(new Point(9, 17), 11), Map.entry(new Point(10, 14), 12));
	}

	// STANDARD FIXED LAND SETUP

	/**
	 * Returns the field (coordinate) to {@link Land} mapping for the <a href=
	 * "https://www.catan.de/files/downloads/4002051693602_catan_-_das_spiel_0.pdf">standard
	 * setup</a> of the game Catan.
	 *
	 * @return the field to {@link Land} mapping for the standard setup
	 */
	public static final Map<Point, Land> getStandardLandPlacement() {
		Map<Point, Land> assignment = new HashMap<>();
		Point[] water = { new Point(4, 2), new Point(6, 2), new Point(8, 2), new Point(10, 2), new Point(3, 5),
				new Point(11, 5), new Point(2, 8), new Point(12, 8), new Point(1, 11), new Point(13, 11),
				new Point(2, 14), new Point(12, 14), new Point(3, 17), new Point(11, 17), new Point(4, 20),
				new Point(6, 20), new Point(8, 20), new Point(10, 20) };

		for (Point p : water) {
			assignment.put(p, Land.WATER);
		}

		assignment.put(new Point(5, 5), Land.FOREST);
		assignment.put(new Point(7, 5), Land.PASTURE);
		assignment.put(new Point(9, 5), Land.PASTURE);

		assignment.put(new Point(4, 8), Land.FIELDS);
		assignment.put(new Point(6, 8), Land.MOUNTAIN);
		assignment.put(new Point(8, 8), Land.FIELDS);
		assignment.put(new Point(10, 8), Land.FOREST);

		assignment.put(new Point(3, 11), Land.FOREST);
		assignment.put(new Point(5, 11), Land.HILLS);
		assignment.put(new Point(7, 11), Land.DESERT);
		assignment.put(new Point(9, 11), Land.MOUNTAIN);
		assignment.put(new Point(11, 11), Land.FIELDS);

		assignment.put(new Point(4, 14), Land.FIELDS);
		assignment.put(new Point(6, 14), Land.MOUNTAIN);
		assignment.put(new Point(8, 14), Land.FOREST);
		assignment.put(new Point(10, 14), Land.PASTURE);

		assignment.put(new Point(5, 17), Land.PASTURE);
		assignment.put(new Point(7, 17), Land.HILLS);
		assignment.put(new Point(9, 17), Land.HILLS);

		return Collections.unmodifiableMap(assignment);
	}

}
