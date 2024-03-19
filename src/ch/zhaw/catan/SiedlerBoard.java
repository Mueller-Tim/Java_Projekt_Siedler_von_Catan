package ch.zhaw.catan;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoard;
import ch.zhaw.structures.City;
import ch.zhaw.structures.Road;
import ch.zhaw.structures.Settlement;
import ch.zhaw.structures.Structure;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * 
 * The SiedlerBoard class is responsible for managing the board state for a game
 * of Catan
 *
 */
public class SiedlerBoard extends HexBoard<Land, Settlement, Road, String> {

	private Map<Point, Integer> diceNumberPlacement;

	/**
	 * Returns the fields associated with the specified dice value.
	 *
	 * @param dice the dice value
	 * @return the fields associated with the dice value
	 */
	public List<Point> getFieldsForDiceValue(int dice) {
		List<Point> fields = new ArrayList<>();
		for (Point field : getDiceNumberPlacement().keySet()) {
			if (diceNumberPlacement.get(field) == dice) {
				fields.add(field);
			}
		}
		return fields;
	}

	/**
	 * Returns the {@link Land}s adjacent to the specified corner.
	 *
	 * @param corner the corner
	 * @return the list with the adjacent {@link Land}s
	 */
	public List<Land> getLandsForCorner(Point corner) {
		return getFields(corner);
	}

	public Map<Point, Integer> getDiceNumberPlacement() {
		return diceNumberPlacement;
	}

	public void setDiceNumberPlacement(Map<Point, Integer> diceNumberPlacement) {
		this.diceNumberPlacement = diceNumberPlacement;
	}

	/**
	 * Returns a HasMap containing the faction as key and the number of road pieces
	 * contained in it's longest road as value
	 * 
	 * @param factions
	 * @return the map containing the road sizes for each faction
	 */
	public HashMap<Config.Faction, Integer> getLongestRoads(List<Config.Faction> factions) {
		HashMap<Config.Faction, Integer> roadSizes = new HashMap<>();
		for (Faction faction : factions) {
			int longestFactionRoad = 0;
			for (Road road : getFactionRoads(faction)) {
				int roadCandidate = countLongestConnectionForRoad(road);
				if (roadCandidate > longestFactionRoad) {
					longestFactionRoad = roadCandidate;
				}
			}
			roadSizes.put(faction, longestFactionRoad);
		}
		return roadSizes;
	}

	private int countLongestConnectionForRoad(Road road) {
		HashSet<Road> roadConnection = new HashSet<>();
		HashSet<Road> roadConnectionReversed = new HashSet<>();
		roadConnection.add(road);
		roadConnectionReversed.add(road);
		roadConnection = recursivelyTravelRoad(roadConnection, road.getEnd(), road.getOwner().getFaction());
		roadConnectionReversed = recursivelyTravelRoad(roadConnection, road.getStart(), road.getOwner().getFaction());
		if (roadConnection.size() > roadConnectionReversed.size()) {
			return roadConnection.size();
		}
		return roadConnectionReversed.size();
	}

	private List<Road> getFactionRoads(Faction faction) {
		List<Road> factionRoads = new ArrayList<>();
		for (Structure road : getRoads()) {
			if (road.getOwner().getFaction() == faction) {
				factionRoads.add((Road) road);
			}
		}
		return factionRoads;
	}

	/**
	 * Returns all settlements of every player
	 * 
	 * @return the list containing every settlement
	 */
	public ArrayList<Structure> getSettlements() {
		ArrayList<Structure> settlements = new ArrayList<>();
		for (Structure corner : getCorners()) {
			if (corner instanceof Settlement) {
				settlements.add(corner);
			}
		}
		return settlements;
	}

	private HashSet<Road> recursivelyTravelRoad(HashSet<Road> roadConnection, Point position, Faction faction) {
		if (isInterruptedBySettlement(faction, position)) {
			return roadConnection;
		}
		List<Road> neighbouringRoads = getNeighbouringFactionRoads(position, faction, roadConnection);
		if (neighbouringRoads.size() == 1) {
			roadConnection.add(neighbouringRoads.get(0));
			return recursivelyTravelRoad(roadConnection, neighbouringRoads.get(0).getOtherEnd(position), faction);
		} else if (neighbouringRoads.size() > 1) {
			List<HashSet<Road>> possibleRoutes = handleJunction(position, faction, roadConnection);
			HashSet<Road> longestCandidate = new HashSet<>();
			for (HashSet<Road> roadCandidate : possibleRoutes) {
				if (roadCandidate.size() > longestCandidate.size()) {
					longestCandidate = roadCandidate;
				}
			}
			roadConnection.addAll(longestCandidate);
		}
		return roadConnection;
	}

	/**
	 * Checks if a given point is occupied by an enemy faction
	 * 
	 * @param faction
	 * @param position
	 * @return boolean whether the given point is occupied
	 */
	public boolean isInterruptedBySettlement(Config.Faction faction, Point position) {
		if (getCorner(position) != null && getCorner(position).getOwner().getFaction() != faction) {
			return true;
		}
		return false;
	}

	private List<Road> getNeighbouringFactionRoads(Point position, Config.Faction faction, HashSet<Road> currentRoad) {
		List<Road> factionRoads = new ArrayList<Road>();
		for (Road road : getAdjacentEdges(position)) {
			if (!currentRoad.contains(road)) {
				if (road.getOwner().getFaction() == faction) {
					factionRoads.add(road);
				}
			}
		}
		return factionRoads;
	}

	private List<HashSet<Road>> handleJunction(Point position, Config.Faction faction, HashSet<Road> currentRoad) {
		List<HashSet<Road>> possibleRoutes = new ArrayList<>();
		for (int i = 0; i < getNeighbouringFactionRoads(position, faction, currentRoad).size(); i++) {
			HashSet<Road> roadCandidate = new HashSet<>();
			Road road = getNeighbouringFactionRoads(position, faction, currentRoad).get(i);
			roadCandidate.add(road);
			roadCandidate = recursivelyTravelRoad(roadCandidate, road.getOtherEnd(position), faction);
			possibleRoutes.add(roadCandidate);
		}
		return possibleRoutes;
	}

	/**
	 * Checks if there is a road of a given player reaches a given point
	 * 
	 * @param position
	 * @param player
	 * @return boolean whether a road exists at the specified point
	 */
	public boolean isRoadAdjoining(Point position, Player player) {
		for (Road road : getAdjacentEdges(position)) {
			if (road.getOwner().equals(player)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns all cities of every player
	 * 
	 * @return the list containing every city
	 */
	public List<Structure> getCities() {
		List<Structure> cities = new ArrayList<>();
		for (Structure corner : getCorners()) {
			if (corner instanceof City) {
				cities.add(corner);
			}
		}
		return cities;
	}

	/**
	 * Returns all roads of every player
	 * 
	 * @return the list containing every road
	 */
	public List<Structure> getRoads() {
		List<Structure> roads = new ArrayList<>();
		for (Structure road : getAllEdges()) {
			if (Objects.nonNull(road)) {
				roads.add(road);
			}
		}
		return roads;
	}

	/**
	 * Checks whether a given point borders a land field
	 *
	 * @param point
	 * @return true if land has border otherwise false if land has no border
	 */
	public boolean hasLandBorder(Point point) {
		for (Config.Land land : getFields(point)) {
			if (hasLandResource(land)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if land has resources
	 *
	 * @param land
	 * @return true if land has resources otherwise false if land has no resources
	 */
	public boolean hasLandResource(Config.Land land) {
		if (land != Config.Land.DESERT && land != Config.Land.WATER) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check if corner is available
	 *
	 * @param position
	 * @return true if point has a corner otherwise false if it has no corner
	 */
	public boolean cornerAvailable(Point position) {
		if (hasCorner(position) && getCorner(position) == null && getNeighboursOfCorner(position).isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Check if building point for settlement is legal for player
	 *
	 * @param position
	 * @param player
	 * @return true if building point for settlement is legal otherwise false if
	 *         it's not legal
	 */
	public boolean isSettlementBuildPointLegal(Point position, Player player) {
		if (cornerAvailable(position)) {
			if (isRoadAdjoining(position, player)) {
				if (hasLandBorder(position)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if building point for city is legal for player
	 *
	 * @param position
	 * @param player
	 * @return true if building point for city is legal otherwise false if it's not
	 *         legal
	 */
	public boolean isCityBuildPointLegal(Point position, Player player) {
		if (hasCorner(position)) {
			if (getCorner(position) != null && getCorner(position).getOwner() == player) {
				if (getCorner(position) instanceof Settlement) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if edge is available
	 *
	 * @param start
	 * @param end
	 * @param player
	 * @return true if edge is available otherwise false if it's not
	 */
	public boolean edgeAvailable(Point start, Point end, Player player) {
		if (hasEdge(start, end)) {
			if (getEdge(start, end) == null) {
				if (((getCorner(start) == null || getCorner(start).getOwner() == player)
						|| (getCorner(end) == null || getCorner(end).getOwner() == player))
						|| isRoadAdjoining(start, player) || isRoadAdjoining(start, player)) {
					if (!isInterruptedBySettlement(player.getFaction(), end)
							&& !isInterruptedBySettlement(player.getFaction(), start)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Check if the corner of a position is owned by specific player
	 *
	 * @param position
	 * @param player
	 * @return true if corner is owned by specific player otherwise false if it's not
	 */
	public boolean isCornerOwner(Point position, Player player) {
		return getCorner(position) != null && getCorner(position).getOwner() == player;
	}

	/**
	 * Check if a road can be built at the provided coordinates by the provided
	 * faction both points need to have a land border, and border either a not
	 * interrupted road of the same faction or a settlement of the same faction
	 * 
	 * @param start
	 * @param end
	 * @param currentPlayerFaction
	 * @return boolean legal
	 */
	public boolean isRoadBuildLegal(Point start, Point end, Faction currentPlayerFaction) {
		if (hasEdge(start, end) && getEdge(start, end) == null) {
			if (hasLandBorder(start) && hasLandBorder(end)) {
				if (getCorner(start) != null && getCorner(start).getOwner().getFaction() == currentPlayerFaction
						|| getCorner(end) != null && getCorner(end).getOwner().getFaction() == currentPlayerFaction) {
					return true;
				}
				if (getNeighbouringFactionRoads(start, currentPlayerFaction, new HashSet<Road>()).size() > 0
						&& !isEnemySettlement(start, currentPlayerFaction)) {
					return true;
				}
				if (getNeighbouringFactionRoads(end, currentPlayerFaction, new HashSet<Road>()).size() > 0
						&& !isEnemySettlement(end, currentPlayerFaction)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isEnemySettlement(Point corner, Config.Faction faction) {
		if (getCorner(corner) == null || getCorner(corner).getOwner().getFaction() == faction) {
			return false;
		}
		return true;
	}
}
