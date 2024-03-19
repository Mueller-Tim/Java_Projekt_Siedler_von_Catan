package ch.zhaw.catan;

import org.junit.jupiter.api.Test;

import ch.zhaw.catan.games.ThreePlayerStandard;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.*;
import java.util.Map.Entry;

/***
 * The SiedlerGameTest class contains JUnit tests for testing the SiedlerGame class.
 * <p>
 * Note: Have a look at {@link ch.zhaw.catan.games.ThreePlayerStandard}. It can
 * be used to get several different game states.
 * </p>
 */
class SiedlerGameTest {

	private SiedlerGame siedlerGame;
	private final static int DEFAULT_WINPOINTS = 7;
	private final static int DEFAULT_NUMBER_OF_PLAYERS = 4;
	private final static boolean DEFAULT_PAYOUT = false;

	private final static Config.Faction WINNER_PLAYER_FACTION = Config.Faction.RED;

	/**
	 * description: win points from each fraction before the initial phase should be zero
	 * equivalence class: 1
	 * initial condition: SiedlerGame is initialised
	 * type: positive test
	 * input: actual points from each faction
	 * output: points should be zero
	 */
	@Test
	void calculateWinPointsBeforeInitialPhase() {
		SiedlerGame model = new SiedlerGame(DEFAULT_WINPOINTS, DEFAULT_NUMBER_OF_PLAYERS);
		Map<Config.Faction, Integer> winPoints = model.calculateWinPoints();
		winPoints.forEach((faction, actualPoints) -> {
			assertEquals(0, actualPoints);
		});
	}

	/**
	 * description: win points from each fraction after the initial phase should be two
	 * equivalence class: 2
	 * initial condition: After the set-up phase of the three player standard situation
	 * type: positive test
	 * input: actual points from each faction
	 * output: points should be two
	 */
	@Test
	void calculateWinPointsAfterInitialPhase() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhase(DEFAULT_WINPOINTS);
		Map<Config.Faction, Integer> winPoints = model.calculateWinPoints();
		winPoints.forEach((faction, actualPoints) -> {
			assertEquals(2, actualPoints);
		});
	}

	/**
	 * description: the number of player is only one
	 * equivalence class: 1
	 * initial condition: nothing
	 * type: negative test
	 * input: SiedlerGame is initialised with only one player
	 * output: IllegalArgumentException
	 */
	@Test
	void SiedlerGameOnePlayer() {
		int numberOfPlayers = 1;
		assertThrows(IllegalArgumentException.class, () -> new SiedlerGame(DEFAULT_WINPOINTS, numberOfPlayers));
	}

	/**
	 * description: the number of player is five
	 * equivalence class: 2
	 * initial condition: nothing
	 * type: negative test
	 * input: SiedlerGame is initialised with five player
	 * output: IllegalArgumentException
	 */
	@Test
	void SiedlerGameFivePlayer() {
		int numberOfPlayers = 5;
		assertThrows(IllegalArgumentException.class, () -> new SiedlerGame(DEFAULT_WINPOINTS, numberOfPlayers));
	}

	/**
	 * description: the required win points is only two
	 * equivalence class: 3
	 * initial condition: nothing
	 * type: negative test
	 * input: SiedlerGame is initialised with a requirement of two win points
	 * output: IllegalArgumentException
	 */
	@Test
	void SiedlerGameTwoWinPoint() {
		int winPoint = 2;
		assertThrows(IllegalArgumentException.class, () -> new SiedlerGame(winPoint, DEFAULT_NUMBER_OF_PLAYERS));
	}

	/**
	 * description: the number of player is two
	 * equivalence class: 4
	 * initial condition: nothing
	 * type: positive test
	 * input: SiedlerGame is initialised with two player
	 * output: it should not throw any exception
	 */
	@Test
	void SiedlerGameTwoPlayer(){
		int numberOfPlayers = 2;
		assertDoesNotThrow(() -> new SiedlerGame(DEFAULT_WINPOINTS, numberOfPlayers));
	}

	/**
	 * description: the number of player is four
	 * equivalence class: 5
	 * initial condition: nothing
	 * type: positive test
	 * input: SiedlerGame is initialised with four player
	 * output: it should not throw any exception
	 */
	@Test
	void SiedlerGameFourPlayer(){
		int numberOfPlayers = 4;
		assertDoesNotThrow(() -> new SiedlerGame(DEFAULT_WINPOINTS, numberOfPlayers));
	}

	/**
	 * description: the required win points is only three
	 * equivalence class: 6
	 * initial condition: nothing
	 * type: positive test
	 * input: SiedlerGame is with a requirement of three win points
	 * output: it should not throw any exception
	 */
	@Test
	void SiedlerGameThreeWinPoint(){
		int winPoint = 3;
		assertDoesNotThrow(() -> new SiedlerGame(winPoint, DEFAULT_NUMBER_OF_PLAYERS));
	}

	/**
	 * description: get the right amount of players
	 * equivalence class: 1
	 * initial condition: SiedlerGame is initialised with four player
	 * type: positive test
	 * input: the actual amount of player
	 * output: four player
	 */
	@Test
	void getNumberOfPlayersFour() {
		siedlerGame = new SiedlerGame(DEFAULT_WINPOINTS, DEFAULT_NUMBER_OF_PLAYERS);
		assertEquals(DEFAULT_NUMBER_OF_PLAYERS, siedlerGame.getNumberOfPlayers());
	}

	/**
	 * description: can't build initial settlement in water
	 * equivalence class: 1
	 * initial condition: SiedlerGame is initialised with four player and required winning points of seven
	 * type: negative test
	 * input: try to build the initial settlement in water
	 * output: boolean false
	 */
	@Test
	void placeInitialSettlementWater() {
		siedlerGame = new SiedlerGame(DEFAULT_WINPOINTS, DEFAULT_NUMBER_OF_PLAYERS);
		Point water = new Point(3, 1);
		assertFalse(siedlerGame.placeInitialSettlement(water, DEFAULT_PAYOUT));
	}

	/**
	 * description: can't build initial settlement when the corner is already occupied
	 * equivalence class: 2
	 * initial condition: SiedlerGame is initialised with four player and required winning points of seven
	 * type: negative test
	 * input: try to build the initial settlement when the corner is already occupied
	 * output: boolean false
	 */
	@Test
	void placeInitialSettlementOccupied() {
		siedlerGame = new SiedlerGame(DEFAULT_WINPOINTS, DEFAULT_NUMBER_OF_PLAYERS);
		Point occupied = new Point(4, 4);
		siedlerGame.placeInitialSettlement(occupied, DEFAULT_PAYOUT);
		assertFalse(siedlerGame.placeInitialSettlement(occupied, DEFAULT_PAYOUT));
	}

	/**
	 * description: build initial settlement at the coast
	 * equivalence class: 3
	 * initial condition: SiedlerGame is initialised with four player and required winning points of seven
	 * type: positive test
	 * input: try to build the initial settlement at the coast
	 * output: boolean true
	 */
	@Test
	void placeInitialSettlementCoast() {
		siedlerGame = new SiedlerGame(DEFAULT_WINPOINTS, DEFAULT_NUMBER_OF_PLAYERS);
		Point land = new Point(6, 6);
		assertTrue(siedlerGame.placeInitialSettlement(land, DEFAULT_PAYOUT));
	}

	/**
	 * description: build initial settlement in the land
	 * equivalence class: 4
	 * initial condition: SiedlerGame is initialised with four player and required winning points of seven
	 * type: positive test
	 * input: try to build the initial settlement in the land
	 * output: boolean true
	 */
	@Test
	void placeInitialSettlementLand() {
		siedlerGame = new SiedlerGame(DEFAULT_WINPOINTS, DEFAULT_NUMBER_OF_PLAYERS);
		Point coast = new Point(4, 4);
		assertTrue(siedlerGame.placeInitialSettlement(coast, DEFAULT_PAYOUT));
	}

	/**
	 * description: can't build settlement in water
	 * equivalence class: 1
	 * initial condition: After the set-up phase with some roads of the three player standard situation
	 * 					and after building a road to a corner in the water
	 * type: negative test
	 * input: try to build settlement in water
	 * output: boolean false
	 */
	@Test
	void buildSettlementWater() {
		Point settlementWater = new Point(11, 19);
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(DEFAULT_WINPOINTS);
		model.buildRoad(new Point(10, 18), settlementWater);
		assertFalse(model.buildSettlement(settlementWater));
	}

	/**
	 * description: can't build settlement when the corner is already occupied
	 * equivalence class: 2
	 * initial condition: After the set-up phase with some roads of the three player standard situation
	 * 					and after building an enemy settlement on the specific corner
	 * type: negative test
	 * input: try to build settlement when the corner is already occupied
	 * output: boolean false
	 */
	@Test
	void buildSettlementOccupied() {
		Point settlementOccupied = new Point(8, 16);
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(DEFAULT_WINPOINTS);
		model.buildRoad(new Point(9, 15), settlementOccupied);
		model.switchToNextPlayer();
		model.switchToNextPlayer();
		model.buildSettlement(settlementOccupied);
		model.switchToNextPlayer();
		assertFalse(model.buildSettlement(settlementOccupied));
	}

	/**
	 * description: can't afford to build a settlement
	 * equivalence class: 3
	 * initial condition: After the set-up phase of the three player standard situation
	 * 					and after building a road to an empty corner
	 * type: negative test
	 * input: try to build settlement without the required amount of recourses
	 * output: boolean false
	 */
	@Test
	void buildSettlementCantAfford() {
		Point settlementEmpty = new Point(6, 4);
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhase(DEFAULT_WINPOINTS);
		for (int i : List.of(6)) {
			model.throwDice(i);
		}
		model.buildRoad(new Point(6,6), settlementEmpty);
		assertFalse(model.buildSettlement(settlementEmpty));
	}

	/**
	 * description: can't build a settlement over the max amount of settlement of the current player
	 * equivalence class: 4
	 * initial condition: After the set-up phase with already four settlement of the three player standard situation
	 * 					and after building a  fifth settlement with a road to an empty corner
	 * type: negative test
	 * input: try to build settlement over the max amount of settlement of the current player
	 * output: boolean false
	 */
	@Test
	void attemptToBuildSixSettlements() {
		SiedlerGame model = ThreePlayerStandard.getPlayerOneReadyToBuildFifthSettlement(DEFAULT_WINPOINTS);
		model.buildSettlement(new Point(9, 13));
		model.buildRoad(new Point(9, 15), new Point(8, 16));
		assertFalse(model.buildSettlement(new Point(8, 16)));
	}

	/**
	 * description: can't build a settlement without an adjoining road
	 * equivalence class: 5
	 * initial condition: After the set-up phase with some roads of the three player standard situation
	 * type: negative test
	 * input: try to build settlement without an adjoining road
	 * output: boolean false
	 */
	@Test
	void buildSettlementWithoutRoad() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(DEFAULT_WINPOINTS);
		Point settlementEmpty = new Point(4, 16);
		assertFalse(model.buildSettlement(settlementEmpty));
	}

	/**
	 * description: can't build a settlement if the adjoining corner is occupied
	 * equivalence class: 6
	 * initial condition: After the set-up phase with some roads of the three player standard situation
	 * type: negative test
	 * input: try to build settlement when the adjoining corner is occupied
	 * output: boolean false
	 */
	@Test
	void buildSettlementAdjoiningCornerOccupied() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(DEFAULT_WINPOINTS);
		Point settlementEmpty = new Point(10, 12);
		assertFalse(model.buildSettlement(settlementEmpty));
	}

	/**
	 * description: build a settlement at the coast
	 * equivalence class: 7
	 * initial condition: After the set-up phase with an almost empty bank of the three player standard situation
	 * type: positive test
	 * input: try to build a settlement at the coast
	 * output: boolean true
	 */
	@Test
	void buildSettlementCoast() {
		Point settlementCoast = new Point(9, 19);
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseAlmostEmptyBank(DEFAULT_WINPOINTS);
		model.buildRoad(new Point(10, 16), new Point(10, 18));
		model.buildRoad(new Point(10, 18), settlementCoast);
		assertTrue(model.buildSettlement(settlementCoast));
	}

	/**
	 * description: build a settlement in the land
	 * equivalence class: 8
	 * initial condition: After the set-up phase with an almost empty bank of the three player standard situation
	 * type: positive test
	 * input: try to build a settlement in the land
	 * output: boolean true
	 */
	@Test
	void buildSettlementLand() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseAlmostEmptyBank(DEFAULT_WINPOINTS);
		Point land = new Point(9, 13);
		model.buildRoad(new Point(9, 15), new Point(9, 13));
		assertTrue(model.buildSettlement(land));
	}

	/**
	 * description: build a settlement right under the max amount of settlement of the current player
	 * equivalence class: 9
	 * initial condition: After the set-up phase with already four settlement of the three player standard situation
	 * type: positive test
	 * input: try to build a settlement right under the max amount of settlement of the current player
	 * output: boolean true
	 */
	@Test
	void buildSettlementUnderLimit() {
		SiedlerGame model = ThreePlayerStandard.getPlayerOneReadyToBuildFifthSettlement(DEFAULT_WINPOINTS);
		assertTrue(model.buildSettlement(
				ThreePlayerStandard.PLAYER_ONE_READY_TO_BUILD_FIFTH_SETTLEMENT_FIFTH_SETTLEMENT_POSITION));
	}

	/**
	 * description: can't build initial road in water
	 * equivalence class: 1
	 * initial condition: SiedlerGame is initialised with four player and required winning points of seven
	 * 					and after placing the initial settlement at the coast
	 * type: negative test
	 * input: try to build the initial road in water
	 * output: boolean false
	 */
	@Test
	void placeInitialRoadWater() {
		SiedlerGame model = new SiedlerGame(DEFAULT_WINPOINTS, DEFAULT_NUMBER_OF_PLAYERS);
		Point initialSettlement = new Point(4,4);
		model.placeInitialSettlement(initialSettlement, DEFAULT_PAYOUT);
		assertFalse(model.placeInitialRoad(initialSettlement, new Point(3,3)));
	}

	/**
	 * description: can't build initial road without an adjoining settlement
	 * equivalence class: 2
	 * initial condition: SiedlerGame is initialised with four player and required winning points of seven
	 * 					and after placing the initial settlement
	 * type: negative test
	 * input: try to build the initial road without boarding to the initial settlement
	 * output: boolean false
	 */
	@Test
	void initialRoadDoesNotBorderInitialSettlement() {
		SiedlerGame model = new SiedlerGame(DEFAULT_WINPOINTS, DEFAULT_NUMBER_OF_PLAYERS);
		model.placeInitialSettlement(new Point(6, 4), DEFAULT_PAYOUT);
		assertFalse(model.placeInitialRoad(new Point(6, 16), new Point(5, 15)));
	}

	/**
	 * description: build initial road at the land
	 * equivalence class: 3
	 * initial condition: SiedlerGame is initialised with four player and required winning points of seven
	 * 					and after placing the initial settlement
	 * type: positive test
	 * input: try to build the initial road at the coast
	 * output: boolean true
	 */
	@Test
	void placeInitialRoadCoast() {
		SiedlerGame model = new SiedlerGame(DEFAULT_WINPOINTS, DEFAULT_NUMBER_OF_PLAYERS);
		Point initialSettlement = new Point(5,7);
		model.placeInitialSettlement(initialSettlement, DEFAULT_PAYOUT);
		assertTrue(model.placeInitialRoad(initialSettlement, new Point(4, 6)));
	}

	/**
	 * description: can't build a road in water
	 * equivalence class: 1
	 * initial condition: After the set-up phase with some roads of the three player standard situation
	 * type: negative test
	 * input: try to build a road in water
	 * output: boolean false
	 */
	@Test
	void buildRoadWater() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(DEFAULT_WINPOINTS);
		Point coast = new Point(10, 18);
		Point water = new Point(11,19);
		assertFalse(model.buildRoad(coast, water));
	}

	/**
	 * description: can't build a road over an enemy settlement
	 * equivalence class: 2
	 * initial condition: After the set-up phase with some roads of the three player standard situation
	 * 					and after building three roads
	 * type: negative test
	 * input: try to build a road over an enemy settlement
	 * output: boolean false
	 */
	@Test
	void buildRoadOverEnemySettlement() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(DEFAULT_WINPOINTS);
		assertTrue(model.buildRoad(new Point(6,6),new Point(6,4)));
		assertTrue(model.buildRoad(new Point(6,4),new Point(7,3)));
		assertTrue(model.buildRoad(new Point(7,3),new Point(8,4)));
		assertFalse(model.buildRoad(new Point(8,4),new Point(9,3)));
	}

	/**
	 * description: can't build a road without a connection to either a road, settlement or city
	 * equivalence class: 3
	 * initial condition: After the set-up phase with some roads of the three player standard situation
	 * type: negative test
	 * input: try to build a road without a connection to either a road, settlement or city
	 * output: boolean false
	 */
	@Test
	void buildRoadWithoutAnConnection() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(DEFAULT_WINPOINTS);
		assertFalse(model.buildRoad(new Point(8,12),new Point(7,3)));
	}

	/**
	 * description: build a road at the coast
	 * equivalence class: 4
	 * initial condition: After the set-up phase with some roads of the three player standard situation
	 * type: positive test
	 * input: try to build a road at the coast
	 * output: boolean true
	 */
	@Test
	void buildRoadCoast() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(DEFAULT_WINPOINTS);
		Point land = new Point(5, 7);
		Point coast = new Point(4,6);
		assertTrue(model.buildRoad(land, coast));
	}

	/**
	 * description: can't build a city on enemy settlement
	 * equivalence class: 1
	 * initial condition: After the set-up phase with some roads of the three player standard situation
	 * 						and with a die roll of 6
	 * type: negative test
	 * input: try to build a city on enemy settlement
	 * output: boolean false
	 */
	@Test
	void buildCityEnemySettlement() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(DEFAULT_WINPOINTS);
		for (int i : List.of(2)) {
			model.throwDice(i);
		}
		assertFalse(model.buildCity(new Point(7, 19)));
	}

	/**
	 * description: build a city on own settlement
	 * equivalence class: 2
	 * initial condition: After the set-up phase with some roads of the three player standard situation
	 * 					and with a die roll of 6
	 * type: positive test
	 * input: try to build a city on own settlement
	 * output: boolean true
	 */
	@Test
	void buildCityOwnSettlement() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(DEFAULT_WINPOINTS);
		for (int i : List.of(2)) {
			model.throwDice(i);
		}
		assertTrue(model.buildCity(new Point(10, 16)));
	}

	/**
	 * description: can't get a winner, if nobody has the required amount of wining points of 7
	 * equivalence class: 1
	 * initial condition: After the set-up phase of the three player standard situation
	 * type: negative test
	 * input: try to get the right winner fraction
	 * output: null
	 */
	@Test
	void winnerIsNullWhileNobodyHasEnoughPointsToWin() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhase(DEFAULT_WINPOINTS);
		assertNull(model.getWinner());
	}

	/**
	 * description: can't get a winner, even if someone is right under the required amount of wining points
	 * equivalence class: 2
	 * initial condition: After the set-up phase with some roads of the three player standard situation
	 * 						and after one road to get the longest road
	 * type: negative test
	 * input: try to get the right winner fraction
	 * output: null
	 */
	@Test
	void winnerIsNullUnderRequiredWinPoints() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(5);
		model.buildRoad(new Point(6, 4), new Point(6, 6));
		assertNull(model.getWinner());
	}

	/**
	 * description: get the right winner fraction, if the player gets the required amount of wining points of 5
	 * equivalence class: 3
	 * initial condition: After the set-up phase with already four settlement of the three player standard situation
	 * 						and after building a fifth settlement
	 * type: positive test
	 * input: try to get the right winner fraction
	 * output: current player fraction
	 */
	@Test
	void getWinnerFivePoints() {
		SiedlerGame model = ThreePlayerStandard.getPlayerOneReadyToBuildFifthSettlement(5);
		model.buildSettlement(ThreePlayerStandard.PLAYER_ONE_READY_TO_BUILD_FIFTH_SETTLEMENT_FIFTH_SETTLEMENT_POSITION);
		assertEquals(WINNER_PLAYER_FACTION, model.getWinner());
	}

	/**
	 * description: get the right winner fraction, if the player gets the required amount of wining points of 7
	 * 				with two cities and three settlements
	 * equivalence class: 4
	 * initial condition: After the set-up phase with some roads of the three player standard situation
	 * 						and after building three settlements and two cities
	 * type: positive test
	 * input: try to get the right winner fraction
	 * output: current player fraction
	 */
	@Test
	void getWinnerTwoCitiesThreeSettlements() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(7);
		for (int i : List.of(2, 2, 2, 2, 2, 2, 12, 12)) {
			model.throwDice(i);
		}
		model.buildSettlement(new Point(9, 13));
		model.buildSettlement(new Point(3, 9));
		model.buildRoad(new Point(5, 9), new Point(6, 10));
		model.buildSettlement(new Point(6, 10));
		model.buildCity(new Point(10, 16));
		model.buildCity(new Point(5, 7));
		assertEquals(WINNER_PLAYER_FACTION, model.getWinner());
	}

	/**
	 * description: get the right winner fraction, if the player gets the required amount of wining points of 5
	 * 				with three settlements and the longest road
	 * equivalence class: 5
	 * initial condition: After the set-up phase with some roads of the three player standard situation
	 * 						and after building one settlement and one road to get the longest road
	 * type: positive test
	 * input: try to get the right winner fraction
	 * output: current player fraction
	 */
	@Test
	void getWinnerThreeSettlementsLongestRoad() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(5);
		model.buildRoad(new Point(6, 4), new Point(6, 6));
		model.buildSettlement(new Point(6, 4));
		assertEquals(WINNER_PLAYER_FACTION, model.getWinner());
	}

	/**
	 * description: get the right winner fraction, if the player gets the required amount of wining points of 7
	 * 				with two cities, one settlement and the longest road
	 * equivalence class: 6
	 * initial condition: After the set-up phase with some roads of the three player standard situation
	 * 						and after building two cities, one settlement and two road to get the longest road
	 * type: positive test
	 * input: try to get the right winner fraction
	 * output: current player fraction
	 */
	@Test
	void getWinnerTwoCitiesOneSettlementLongestRoad() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(7);
		for (int i : List.of(2, 2, 2, 2, 2)) {
			model.throwDice(i);
		}
		model.buildSettlement(new Point(9, 13));
		model.buildCity(new Point(10, 16));
		model.buildCity(new Point(5, 7));
		model.buildRoad(new Point(5, 7), new Point(4, 6));
		model.buildRoad(new Point(4, 6), new Point(3, 7));
		assertEquals(WINNER_PLAYER_FACTION, model.getWinner());
	}

	/**
	 * description: detect six roads as a ring and not as a loop of rings
	 * equivalence class: 1
	 * initial condition: After the set-up phase of the three player standard situation
	 * 						and after building six road as a ring
	 * type: positive test
	 * input: get the amount of roads from the longest road
	 * output: integer six
	 */
	@Test
	void ringRoadDetection() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhase(DEFAULT_WINPOINTS);
		for (int i : List.of(6, 6, 6, 6, 6, 11, 11, 11, 11, 11)) {
			model.throwDice(i);
		}
		model.buildRoad(new Point(6, 6), new Point(6, 4));
		model.buildRoad(new Point(6, 4), new Point(5, 3));
		model.buildRoad(new Point(5, 3), new Point(4, 4));
		model.buildRoad(new Point(4, 4), new Point(4, 6));
		model.buildRoad(new Point(4, 6), new Point(5, 7));
		assertEquals(6,
				model.getBoard().getLongestRoads(model.getPlayerFactions()).get(model.getCurrentPlayerFaction()));
	}

	/**
	 * description: detect junction in road
	 * equivalence class: 2
	 * initial condition: After the set-up phase with already four settlement of the three player standard situation
	 * 						and building one road
	 * type: positive test
	 * input: get the amount of roads from the longest road
	 * output: integer three
	 */
	@Test
	void junctionInRoadDetection() {
		SiedlerGame model = ThreePlayerStandard.getPlayerOneReadyToBuildFifthSettlement(DEFAULT_WINPOINTS);
		{
			model.buildRoad(new Point(6, 4), new Point(7, 3));
			assertEquals(3, model.getBoard().getLongestRoads(model.getPlayerFactions()).get(Config.Faction.RED));
		}
	}

	/**
	 * description: detect the longest road with roads build end to end
	 * equivalence class: 3
	 * initial condition: After the set-up phase of the three player standard situation
	 * 						and building four road end to end
	 * type: positive test
	 * input: get the amount of roads from the longest road
	 * output: integer five
	 */
	@Test
	void testLongestRoadFunctionalityWithRoadsBuiltEndToEnd() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhase(DEFAULT_WINPOINTS);
		for (int i : List.of(6, 6, 6, 6, 6, 11, 11, 11, 11, 11)) {
			model.throwDice(i);
		}
		model.buildRoad(new Point(6, 4), new Point(6, 6));
		model.buildRoad(new Point(6, 4), new Point(5, 3));
		model.buildRoad(new Point(4, 4), new Point(5, 3));
		model.buildRoad(new Point(4, 4), new Point(4, 6));
		assertEquals(5, model.getBoard().getLongestRoads(model.getPlayerFactions()).get(Config.Faction.RED));
	}

	/**
	 * description: detect the longest road which is interrupted with en enemy settlement
	 * equivalence class: 4
	 * initial condition: After the set-up phase of the three player standard situation
	 * 						and after building own road and after the enemy built one settlement
	 * type: positive test
	 * input: get the amount of roads from the longest road
	 * output: first integer five, second integer four
	 */
	@Test
	void longestRoadInterruptedByEnemySettlement() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(DEFAULT_WINPOINTS);
		model.switchToNextPlayer();
		model.switchToNextPlayer();
		model.buildRoad(new Point(3, 7), new Point(4, 6));
		assertEquals(5, model.getBoard().getLongestRoads(model.getPlayerFactions()).get(Config.Faction.GREEN));
		model.switchToNextPlayer();
		model.buildSettlement(new Point(3, 9));
		assertEquals(4, model.getBoard().getLongestRoads(model.getPlayerFactions()).get(Config.Faction.GREEN));
	}

	/**
	 * description: the longest road is awarded from five adjoining roads upwards
	 * equivalence class: 1
	 * initial condition: After the set-up phase of the three player standard situation
	 * 						and after building four road adjoining roads
	 * type: positive test
	 * input: get the longest road owner
	 * output: faction red, which should be the longest road owner
	 */
	@Test
	void longestRoadDetection() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhase(DEFAULT_WINPOINTS);
		for (int i : List.of(6, 6, 6, 6, 6, 11, 11, 11, 11, 11)) {
			model.throwDice(i);
		}
		model.buildRoad(new Point(6, 6), new Point(6, 4));
		model.buildRoad(new Point(6, 4), new Point(5, 3));
		model.buildRoad(new Point(5, 3), new Point(4, 4));
		model.buildRoad(new Point(4, 6), new Point(5, 7));
		Config.Faction longestRoadOwner = null;
		Map<Config.Faction, Integer> roadSizes = model.getBoard().getLongestRoads(model.getPlayerFactions());
		int longestRoad = Collections.max(roadSizes.values());
		for (Entry<Config.Faction, Integer> entry : roadSizes.entrySet()) {
			if (entry.getValue() == longestRoad) {
				longestRoadOwner = entry.getKey();
			}
		}
		assertEquals(Config.Faction.RED, longestRoadOwner);
	}
	
	/**
	 * description: can't build a road if it is not adjacent to an existing road or settlement
	 * equivalence class: 2
	 * initial condition: After set-up phase with some roads built and resources distributed
	 * input: try to build a road that is not adjacent to any structure of the current player
	 * output: boolean false
	 */
	@Test
	void roadPlacementNotAdjacentToPlayerStructure() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseSomeRoads(DEFAULT_WINPOINTS);
		assertFalse(model.buildRoad(new Point(7, 9), new Point(7, 7)));
	}
	
	/**
	 * description: can't build a road over an enemy settlement
	 * equivalence class: 2
	 * initial condition: After one player is ready to build the fifth settlement
	 * input: try to build a road over an enemy settlement
	 * output: boolean false
	 */
	@Test
	void roadPlacementOverEnemySettlement() {
		SiedlerGame model = ThreePlayerStandard.getPlayerOneReadyToBuildFifthSettlement(DEFAULT_WINPOINTS);
		model.buildRoad(new Point(10, 16), new Point(11, 15));
		model.buildRoad(new Point(11, 15), new Point(11, 13));
		assertFalse(model.buildRoad(new Point(11, 13), new Point(10, 12)));
	}
	

	/**
	 * description: get the inventory of the current player
	 * equivalence class: 1
	 * initial condition: After the set-up phase with already four settlement of the three player standard situation
	 * type: positive test
	 * input: try to get inventory of the current player
	 * output: expected resources of three lumber, three brick, two grain, zero ore and two wool
	 */
	@Test
	void getInventoryOfCurrentPlayer() {
		SiedlerGame model = ThreePlayerStandard.getPlayerOneReadyToBuildFifthSettlement(DEFAULT_WINPOINTS);
		String[] expectedResources = new String[] { String.valueOf(3), String.valueOf(3), String.valueOf(2),
				String.valueOf(2), String.valueOf(0) };
		assertArrayEquals(expectedResources, model.getCurrentPlayerInventory());
	}

	/**
	 * description: can't trade with bank if the player hasn't enough resources
	 * equivalence class: 1
	 * initial condition: After the set-up phase of the three player standard situation
	 * type: negative test
	 * input: try to trade with bank if the player hasn't enough resources
	 * output: boolean false
	 */
	@Test
	void tradeWithBankPlayerNotEnoughResources() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhase(DEFAULT_WINPOINTS);
		assertFalse(model.tradeWithBankFourToOne(Config.Resource.ORE, Config.Resource.BRICK));
	}

	/**
	 * description: can't trade with bank if the bank hasn't enough resources
	 * equivalence class: 2
	 * initial condition: After the set-up phase with an almost empty bank of the three player standard situation
	 * type: negative test
	 * input: try to trade with bank if the bank hasn't enough resources
	 * output: boolean false
	 */
	@Test
	void bankNotEnoughResourceToTrade() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseAlmostEmptyBank(DEFAULT_WINPOINTS);
		assertFalse(model.tradeWithBankFourToOne(Config.Resource.BRICK, Config.Resource.WOOL));
	}

	/**
	 * description: trade with bank if the bank and the player has enough resources
	 * equivalence class: 3
	 * initial condition: After the set-up phase with an almost empty bank of the three player standard situation
	 * type: positive test
	 * input: try to trade with bank if the bank and the player has enough resources
	 * output: boolean true
	 */
	@Test
	void tradeWithBankWithEnoughResources() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseAlmostEmptyBank(DEFAULT_WINPOINTS);
		assertTrue(model.tradeWithBankFourToOne(Config.Resource.GRAIN, Config.Resource.ORE));
	}

	/**
	 * description: if bank has not enough resources for the payout on dice throw, should the bank only give
	 * 				the resources it has for one player
	 * equivalence class: 1
	 * initial condition: After the set-up phase with an almost empty bank of the three player standard situation
	 *                   and building a road and a settlement
	 * type: positive test
	 * input: throw the dice and try to get the payout
	 * output: expected resources payout of lumber
	 */
	@Test
	void notEnoughResourcesInBankForPayoutOnThrowDiceOnePlayer() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseAlmostEmptyBank(DEFAULT_WINPOINTS);
		model.buildRoad(new Point(6, 6), new Point(6, 4));
		model.buildSettlement(new Point(6, 4));
		model.throwDice(6);
		Map<Config.Faction, List<Config.Resource>> expectedResourcesPayout = Map.of(Config.Faction.values()[0],
				List.of(Config.Resource.LUMBER), Config.Faction.values()[1], List.of(), Config.Faction.values()[2],
				List.of());
		assertEquals(expectedResourcesPayout, model.throwDice(6));
	}

	/**
	 * description: if bank has not enough resources for the payout on dice throw, should the bank only give
	 * 				the resources it has evenly to all players.
	 * equivalence class: 1
	 * initial condition: After the set-up phase with an almost empty bank of the three player standard situation
	 * 					 and building a road and a settlement
	 * type: positive test
	 * input: throw the dice and try to get the payout
	 * output: expected empty resources payout
	 */
	@Test void notEnoughResourcesInBankForPayoutOnThrowDiceMultiplePlayers() {
		SiedlerGame model = ThreePlayerStandard.getAfterSetupPhaseAlmostEmptyBank(DEFAULT_WINPOINTS);
		model.buildRoad(new Point(6, 6), new Point(6, 4));
		model.buildSettlement(new Point(6, 4));
		Map<Config.Faction, List<Config.Resource>> expectedResourcesPayout = Map.of(
				Config.Faction.values()[0], List.of(),
				Config.Faction.values()[1], List.of(),
				Config.Faction.values()[2], List.of()
		);
		assertEquals(expectedResourcesPayout, model.throwDice(12));
	}
}