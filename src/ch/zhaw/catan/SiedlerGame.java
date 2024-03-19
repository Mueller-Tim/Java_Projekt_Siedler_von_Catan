package ch.zhaw.catan;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;
import ch.zhaw.hexboard.Label;
import ch.zhaw.structures.City;
import ch.zhaw.structures.Road;
import ch.zhaw.structures.Settlement;
import ch.zhaw.structures.Structure;

import java.awt.Point;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * The SiedlerGame class is responsible for altering the game state of a game of
 * Catan
 *
 */
public class SiedlerGame {
	static final int FOUR_TO_ONE_TRADE_OFFER = 4;
	static final int FOUR_TO_ONE_TRADE_WANT = 1;
	private Player[] players;
	private SiedlerBoard board;
	private SiedlerBoardTextView view;
	private int winPoints;
	private int currentPlayer;
	private RandomNumberGenerator rng;
	private Bank bank;

	/**
	 * Constructs a SiedlerGame game state object.
	 *
	 * @param winPoints       the number of points required to win the game
	 * @param numberOfPlayers the number of players
	 *
	 * @throws IllegalArgumentException if winPoints is lower than three or players
	 *                                  is not between two and four
	 */
	public SiedlerGame(int winPoints, int numberOfPlayers) {
		if (winPoints < Config.REQUIRED_WIN_POINTS || numberOfPlayers > Config.MAX_NUMBER_OF_PLAYERS
				|| numberOfPlayers < Config.MIN_NUMBER_OF_PLAYERS) {
			throw new IllegalArgumentException("Parameters did not match expectations");
		}
		this.winPoints = winPoints;
		this.players = new Player[numberOfPlayers];
		this.board = new SiedlerBoard();
		this.view = new SiedlerBoardTextView(board);
		this.currentPlayer = 0;
		this.rng = new RandomNumberGenerator();
		this.bank = new Bank();
		createPlayers();
		setupBoard();
	}

	private void createPlayers() {
		ArrayList<Config.Faction> factionlist = new ArrayList<Config.Faction>(Arrays.asList(Config.Faction.values()));
		for (int i = 0; i < getNumberOfPlayers(); i++) {
			players[i] = new Player(factionlist.get(i));
		}
	}

	/**
	 * Return the current player
	 *
	 * @return the current player
	 */
	public Player getCurrentPlayer() {
		return players[currentPlayer];
	}

	private void setupBoard() {
		Config.getStandardLandPlacement().forEach(this.board::addField);
		this.board.setDiceNumberPlacement(Config.getStandardDiceNumberPlacement());
		this.board.getDiceNumberPlacement().forEach((point, integer) -> {
			if (!board.getField(point).equals(Config.Land.DESERT)) {
				view.setLowerFieldLabel(point, (integer >= 10) ? new Label('1', integer.toString().charAt(1))
						: new Label('0', integer.toString().charAt(0)));
			}
		});
	}

	/**
	 * Return the amount of players
	 *
	 * @return the amount of players
	 */
	public int getNumberOfPlayers() {
		return players.length;
	}


	public SiedlerBoardTextView getView() {
		return view;
	}

	/**
	 * Switches to the next player in the defined sequence of players.
	 */
	public void switchToNextPlayer() {
		if (currentPlayer < getNumberOfPlayers() - 1) {
			currentPlayer += 1;
		} else {
			currentPlayer = 0;
		}
	}

	/**
	 * Switches to the previous player in the defined sequence of players.
	 */
	public void switchToPreviousPlayer() {
		if (currentPlayer > 0) {
			currentPlayer -= 1;
		} else {
			currentPlayer = getNumberOfPlayers() - 1;
		}
	}

	/**
	 * Returns the {@link Faction}s of the active players.
	 *
	 * <p>
	 * The order of the player's factions in the list must correspond to the oder in
	 * which they play. Hence, the player that sets the first settlement must be at
	 * position 0 in the list etc.
	 * </p>
	 * <p>
	 * <strong>Important note:</strong> The list must contain the factions of active
	 * players only.
	 * </p>
	 *
	 * @return the list with player's factions
	 */
	public List<Faction> getPlayerFactions() {
		List<Faction> factions = new ArrayList<Faction>();
		for (int i = 0; i < getNumberOfPlayers(); i++) {
			factions.add(Config.Faction.values()[i]);
		}
		return factions;
	}

	/**
	 * Returns the game board.
	 *
	 * @return the game board
	 */
	public SiedlerBoard getBoard() {
		return board;
	}

	/**
	 * Returns the {@link Faction} of the current player.
	 *
	 * @return the faction of the current player
	 */
	public Faction getCurrentPlayerFaction() {
		return getCurrentPlayer().getFaction();
	}

	/**
	 * Returns how many resource cards of the specified type the current player
	 * owns.
	 *
	 * @param resource the resource type
	 * @return the number of resource cards of this type
	 */
	public int getCurrentPlayerResourceStock(Resource resource) {
		return getCurrentPlayer().getResources().get(resource);
	}

	/**
	 * Places a settlement in the founder's phase (phase II) of the game.
	 *
	 * <p>
	 * The placement does not cost any resource cards. If payout is set to true, for
	 * each adjacent resource-producing field, a resource card of the type of the
	 * resource produced by the field is taken from the bank (if available) and
	 * added to the players' stock of resource cards.
	 * </p>
	 *
	 * @param position the position of the settlement
	 * @param payout   if true, the player gets one resource card per adjacent
	 *                 resource-producing field
	 * @return true, if the placement was successful
	 */
	public boolean placeInitialSettlement(Point position, boolean payout) {
		if (board.cornerAvailable(position) && board.hasLandBorder(position)) {
			board.setCorner(position, new Settlement(getCurrentPlayer(), position));
			if (payout) {
				board.getFields(position).forEach(land -> {
					if (board.hasLandResource(land)) {
						addResourceToPlayer(getCurrentPlayer(), land.getResource(), Config.SETTLEMENT_RESOURCE_REWARD);
					}
				});
			}
			return true;
		}
		return false;
	}

	/**
	 * Places a road in the founder's phase (phase II) of the game. The placement
	 * does not cost any resource cards.
	 *
	 * @param roadStart position of the start of the road
	 * @param roadEnd   position of the end of the road
	 * @return true, if the placement was successful
	 */
	public boolean placeInitialRoad(Point roadStart, Point roadEnd) {
		Player currentPlayer = getCurrentPlayer();
		if (board.hasEdge(roadStart, roadEnd) && board.hasLandBorder(roadStart) && board.hasLandBorder(roadEnd)) {
			if (board.isCornerOwner(roadStart, getCurrentPlayer())
					|| board.isCornerOwner(roadEnd, getCurrentPlayer())) {
				board.setEdge(roadStart, roadEnd, new Road(currentPlayer, roadStart, roadEnd));
				return true;
			}
		}
		return false;
	}

	/**
	 * This method takes care of actions depending on the dice throw result.
	 * <p>
	 * A key action is the payout of the resource cards to the players according to
	 * the payout rules of the game. This includes the "negative payout" in case a 7
	 * is thrown and a player has more than {@link Config#MAX_CARDS_IN_HAND_NO_DROP}
	 * resource cards.
	 * </p>
	 * <p>
	 * If a player does not get resource cards, the list for this players'
	 * {@link Faction} is <b>an empty list (not null)</b>!.
	 * </p>
	 * <p>
	 * The payout rules of the game take into account factors such as, the number of
	 * resource cards currently available in the bank, settlement types (settlement
	 * or city), and the number of players that should get resource cards of a
	 * certain type (relevant if there are not enough left in the bank).
	 * </p>
	 *
	 * @param diceThrow the resource cards that have been distributed to the players
	 * @return the resource cards added to the stock of the different players
	 */
	public Map<Faction, List<Resource>> throwDice(int diceThrow) {
		Map<Faction, List<Resource>> resourcePayout = new HashMap<>();

		if (diceThrow == 7) {
			handleCardDropping();
		} else {
			resourcePayout = generateResourcePayout(diceThrow);
		}
		return resourcePayout;
	}

	private Map<Faction, List<Resource>> generateResourcePayout(int diceThrow) {
		Map<Faction, List<Resource>> payout = new HashMap<>();
		for (Config.Faction faction : getPlayerFactions()) {
			payout.put(faction, new ArrayList<Resource>());
		}

		for (Point field : board.getFieldsForDiceValue(diceThrow)) {
			if (board.getCornersOfField(field.getLocation()).size() <= bank
					.getAmountOfResource(board.getField(field).getResource())
					|| settlementsListContainAllSameOwner(board.getCornersOfField(field))) {
				for (Settlement settlement : board.getCornersOfField(field.getLocation())) {
					List<Resource> resources = new ArrayList<Resource>();
					for (int i = 0; i < settlement.getResourceReward(); i++) {
						if (addResourceToPlayer(settlement.getOwner(), board.getField(field).getResource(), 1)) {
							resources.add(board.getField(field).getResource());
						}
					}
					payout.get(settlement.getOwner().getFaction()).addAll(resources);
				}
			}
		}
		return payout;
	}

	private boolean settlementsListContainAllSameOwner(List<Settlement> settlementList) {
		for (Settlement settlement : settlementList) {
			if(!settlement.getOwner().equals(settlementList.get(0).getOwner())) {
				return false;
			}
		}

		return true;
	}

	private boolean addResourceToPlayer(Player player, Resource resource, int amount) {
		try {
			player.addResource(bank.removeCards(resource, amount));
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private boolean addResourceFromPlayerToBank(Player player, Resource resource, int amount) {
		try {
			bank.addResource(player.removeCards(resource, amount));
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private void handleCardDropping() {
		for (Player player : players) {
			int amountOfCards = player.countCards();
			if (amountOfCards > 7) {
				int countAfterDropping = amountOfCards - (int) Math.floor(amountOfCards / 2);
				while (player.countCards() > countAfterDropping) {
					Config.Resource randomResource = rng.getRandomResource();
					if (player.getAmountOfResource(randomResource) > 0) {
						addResourceFromPlayerToBank(player, randomResource, 1);
					}
				}
			}
		}
	}

	/**
	 * Builds a settlement at the specified position on the board.
	 *
	 * <p>
	 * The settlement can be built if:
	 * <ul>
	 * <li>the player possesses the required resource cards</li>
	 * <li>a settlement to place on the board</li>
	 * <li>the specified position meets the build rules for settlements</li>
	 * </ul>
	 *
	 * @param position the position of the settlement
	 * @return true, if the placement was successful
	 */
	public boolean buildSettlement(Point position) {
		if (isSettlementBuildLegal(position)) {
			if (payResources(Config.SETTLEMENT_COST)) {
				board.setCorner(position, new Settlement(getCurrentPlayer(), position));
				return true;
			}
		}
		return false;
	}

	private boolean isSettlementBuildLegal(Point position) {
		if (board.isSettlementBuildPointLegal(position, getCurrentPlayer()) && canAfford(Config.SETTLEMENT_COST)) {
			if (!limitReached(Config.Structure.SETTLEMENT.getStockPerPlayer(),
					new ArrayList<>(board.getSettlements()))) {
				return true;
			}
		}
		return false;
	}

	private boolean canAfford(Map<Resource, Integer> structureCost) {
		for (Resource resource : structureCost.keySet()) {
			if (structureCost.get(resource) > (getCurrentPlayer().getResources().get(resource))) {
				return false;
			}
		}
		return true;
	}

	private boolean payResources(Map<Resource, Integer> structureCost) {
		Map<Resource, Integer> alreadyPayedResources = new HashMap<>();
		for (Map.Entry<Resource, Integer> costPerResource : structureCost.entrySet()) {
			if(!addResourceFromPlayerToBank(getCurrentPlayer(), costPerResource.getKey(), costPerResource.getValue())){
				alreadyPayedResources.forEach((resource, amount) -> addResourceToPlayer(getCurrentPlayer(), resource, amount));
				return false;
			}
			alreadyPayedResources.put(costPerResource.getKey(), costPerResource.getValue());
		}
		return true;
	}

	/**
	 * Builds a city at the specified position on the board.
	 *
	 * <p>
	 * The city can be built if:
	 * <ul>
	 * <li>the player possesses the required resource cards</li>
	 * <li>a city to place on the board</li>
	 * <li>the specified position meets the build rules for cities</li>
	 * </ul>
	 *
	 * @param position the position of the city
	 * @return true, if the placement was successful
	 */
	public boolean buildCity(Point position) {
		if (isCityBuildLegal(position)) {
			if (payResources(Config.CITY_COST)) {
				board.setCorner(position, new City(getCurrentPlayer(), position));
				return true;
			}
		}
		return false;
	}

	private boolean isCityBuildLegal(Point position) {
		if (board.isCityBuildPointLegal(position, getCurrentPlayer()) && canAfford(Config.CITY_COST)) {
			if (!limitReached(Config.Structure.CITY.getStockPerPlayer(), new ArrayList<>(board.getCities()))) {
				return true;
			}
		}

		return false;
	}

	private boolean limitReached(int limit, List<Structure> structures) {
		int structureCount = 0;
		for (Structure structure : structures) {
			if (structure.getOwner() == getCurrentPlayer()) {
				structureCount += 1;
			}
		}
		if (limit > structureCount) {
			return false;
		}
		return true;
	}

	/**
	 * Builds a road at the specified position on the board.
	 *
	 * <p>
	 * The road can be built if:
	 * <ul>
	 * <li>the player possesses the required resource cards</li>
	 * <li>a road to place on the board</li>
	 * <li>the specified position meets the build rules for roads</li>
	 * </ul>
	 *
	 * @param roadStart the position of the start of the road
	 * @param roadEnd   the position of the end of the road
	 * @return true, if the placement was successful
	 */
	public boolean buildRoad(Point roadStart, Point roadEnd) {
		if (canBuildRoad(roadStart, roadEnd)) {
			if(payResources(Config.ROAD_COST)) {
				board.setEdge(roadStart, roadEnd, new Road(getCurrentPlayer(), roadStart, roadEnd));
				return true;
			}

		}
		return false;
	}

	private boolean canBuildRoad(Point start, Point end) {
		if (canAfford(Config.ROAD_COST)) {
			if (!limitReached(Config.Structure.ROAD.getStockPerPlayer(), new ArrayList<>(board.getRoads()))) {
				if (board.isRoadBuildLegal(start, end, getCurrentPlayerFaction())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Trades in {@link #FOUR_TO_ONE_TRADE_OFFER} resource cards of the offered type
	 * for {@link #FOUR_TO_ONE_TRADE_WANT} resource cards of the wanted type.
	 * </p>
	 * <p>
	 * The trade only works when bank and player possess the resource cards for the
	 * trade before the trade is executed.
	 * </p>
	 *
	 * @param offer offered type
	 * @param want  wanted type
	 * @return true, if the trade was successful
	 */
	public boolean tradeWithBankFourToOne(Resource offer, Resource want) {
		if (bank.getAmountOfResource(want) >= 1 && getCurrentPlayer().getAmountOfResource(offer) >= 4) {
			if(addResourceFromPlayerToBank(getCurrentPlayer(), offer, 4)
					&& addResourceToPlayer(getCurrentPlayer(), want, 1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the winner of the game, if any.
	 *
	 * @return the winner of the game or null, if there is no winner (yet)
	 */
	public Faction getWinner() {
		for (Map.Entry<Faction, Integer> factionPointsEntry : calculateWinPoints().entrySet()) {
			if (factionPointsEntry.getValue() >= winPoints) {
				return factionPointsEntry.getKey();
			}
		}
		return null;
	}

	private Config.Faction getLongestRoadOwner() {
		Map<Config.Faction, Integer> roadSizes = board.getLongestRoads(getPlayerFactions());
		int longestRoad = Collections.max(roadSizes.values());
		for (Entry<Config.Faction, Integer> entry : roadSizes.entrySet()) {
			if (entry.getValue() == longestRoad) {
				return entry.getKey();
			}
		}
		return null;
	}

	private boolean qualifiesForLongestRoad(Config.Faction faction) {
		if (getLongestRoadOwner() != null && faction == getLongestRoadOwner()) {
			if (board.getLongestRoads(getPlayerFactions()).get(faction) > Config.LONGEST_ROAD_THRESHOLD) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calculates the win points of every player
	 * 
	 * @return map containing the player faction and corresponding amount of win
	 *         points
	 */
	public Map<Config.Faction, Integer> calculateWinPoints() {
		Map<Config.Faction, Integer> winPoints = new HashMap<>();
		for (Player player : players) {
			winPoints.put(player.getFaction(), 0);
			for (Settlement settlement : board.getCorners()) {
				if (settlement.getOwner() == player) {
					winPoints.put(player.getFaction(),
							winPoints.get(player.getFaction()) + settlement.getVictoryPoints());
				}
			}
			if (qualifiesForLongestRoad(player.getFaction())) {
				winPoints.put(player.getFaction(),
						winPoints.get(player.getFaction()) + Config.VICTORY_POINTS_LONGEST_ROAD);
			}
		}
		return winPoints;
	}

	/**
	 * Returns the inventory form the current player
	 *
	 * @return the inventory from the current player
	 */
	public String[] getCurrentPlayerInventory() {
		String[] inventory = new String[] { String.valueOf(getCurrentPlayerResourceStock(Config.Resource.LUMBER)),
				String.valueOf(getCurrentPlayerResourceStock(Config.Resource.BRICK)),
				String.valueOf(getCurrentPlayerResourceStock(Config.Resource.GRAIN)),
				String.valueOf(getCurrentPlayerResourceStock(Config.Resource.WOOL)),
				String.valueOf(getCurrentPlayerResourceStock(Config.Resource.ORE)) };
		return inventory;
	}
}
