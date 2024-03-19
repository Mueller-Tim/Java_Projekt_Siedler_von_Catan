package ch.zhaw.catan;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;
import ch.zhaw.userinterface.InputManager;
import ch.zhaw.userinterface.OutputWriter;

/**
 * 
 * The Catan class is responsible for game structure for a game of Catan
 *
 */
public class Catan {
	private SiedlerGame siedlerGame;
	private static final TextIO textIO = TextIoFactory.getTextIO();
	private static final TextTerminal<?> terminal = textIO.getTextTerminal();
	private InputManager inputManager;
	private OutputWriter outputWriter;
	private static final RandomNumberGenerator rng = new RandomNumberGenerator();

	/**
	 * Main method to start the application
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new Catan().startGame();
	}

	/**
	 * Default constructor for class Catan.java instantiates several data fields
	 */
	public Catan() {
		inputManager = new InputManager();
		outputWriter = new OutputWriter();
	}

	private void startGame() {
		outputWriter.print(terminal, Config.InfoMessage.GREETING.toString());
		int numberOfPlayers = inputManager.specifyNumberOfPlayers(textIO);
		siedlerGame = new SiedlerGame(Config.WIN_POINTS, numberOfPlayers);
		foundingPhase();
	}

	private void foundingPhase() {
		for (int i = 0; i < siedlerGame.getPlayerFactions().size(); i++) {
			printBoard();
			placeInitialStructures(false);
			siedlerGame.switchToNextPlayer();
		}
		for (int i = 0; i < siedlerGame.getPlayerFactions().size(); i++) {
			printBoard();
			siedlerGame.switchToPreviousPlayer();
			placeInitialStructures(true);
		}
		nextTurn();
	}

	private void placeInitialStructures(boolean payoutResources) {
		outputWriter.print(terminal, Config.InfoMessage.TURN_ANNOUNCEMENT.toString(),
				new String[] { String.valueOf(siedlerGame.getCurrentPlayerFaction()) });
		initialRoad(initialSettlement(payoutResources));
	}

	private Point initialSettlement(boolean payoutResources) {
		Point buildingPoint = setPointByCoordinate("settlement");
		if (siedlerGame.placeInitialSettlement(buildingPoint, payoutResources)) {
			outputWriter.print(terminal, Config.SuccessMessage.BUILD.toString(), new String[] { "Settlement" });
			return buildingPoint;
		}
		outputWriter.print(terminal, Config.ErrorMessage.BUILDING.toString(), new String[] { "settlement" });
		return initialSettlement(payoutResources);
	}

	private void initialRoad(Point settlementBuildingPoint) {
		Point roadEndPoint = setPointByCoordinate("road endpoint");
		if (!(siedlerGame.placeInitialRoad(settlementBuildingPoint, roadEndPoint))) {
			outputWriter.print(terminal, Config.ErrorMessage.BUILDING.toString(), new String[] { "road" });
			initialRoad(settlementBuildingPoint);
		} else {
			outputWriter.print(terminal, Config.SuccessMessage.BUILD.toString(), new String[] { "Road" });
		}
	}

	private void printBoard() {
		outputWriter.print(terminal, Config.InfoMessage.GAME_STATE.toString(),
				new String[] { siedlerGame.getView().toString() });
	}

	private void nextTurn() {
		printBoard();
		outputWriter.print(terminal, Config.InfoMessage.TURN_ANNOUNCEMENT.toString(),
				new String[] { String.valueOf(siedlerGame.getCurrentPlayerFaction()) });
		handleDiceThrow();
		handleActions();
		if (isGameOver()) {
			endGame();
		} else {
			siedlerGame.switchToNextPlayer();
			nextTurn();
		}
	}

	private void handleDiceThrow() {
		int diceThrow = rng.throwTwoDice();
		outputWriter.print(terminal, Config.InfoMessage.DICE_THROW.toString(),
				new String[] { String.valueOf(diceThrow) });
		visualizeResourceDistribution(siedlerGame.throwDice(diceThrow));
	}

	private void visualizeResourceDistribution(Map<Faction, List<Resource>> distribution) {
		for (Config.Faction faction : distribution.keySet()) {
			if (distribution.get(faction).size() != 0) {
				outputWriter.print(terminal, Config.InfoMessage.RESOURCE_DISTRIBUTION.toString(),
						new String[] { faction.toString(), distribution.get(faction).toString() });
			}
		}
	}

	private void handleActions() {
		switch (inputManager.selectAction(textIO)) {
		case BUILD:
			buildingSelection();
			handleActions();
			break;
		case TRADE:
			tradingSelection();
			handleActions();
			break;
		case SHOW_INVENTORY:
			outputWriter.print(terminal, Config.InfoMessage.PLAYER_INVENTORY.toString(),
					siedlerGame.getCurrentPlayerInventory());
			handleActions();
			break;
		case END_TURN:
			printWinPointStandings();
			break;
		default:
			throw new IllegalStateException("This is not a valid action.");
		}
	}

	private void buildingSelection() {
		boolean turn = true;
		while (turn) {
			switch (inputManager.selectBuildingType(textIO)) {
			case SETTLEMENT:
				buildSettlement();
				break;
			case ROAD:
				buildRoad();
				break;
			case CITY:
				buildCity();
				break;
			case CANCEL:
				turn = false;
				break;
			default:
				throw new IllegalArgumentException("This is not a valid building type");
			}

		}
	}

	private void buildSettlement() {
		if (siedlerGame.buildSettlement(setPointByCoordinate("settlement"))) {
			outputWriter.print(terminal, Config.SuccessMessage.BUILD.toString(), new String[] { "Settlement" });
		} else {
			outputWriter.print(terminal, Config.ErrorMessage.BUILDING.toString(), new String[] { "Settlement" });
		}
	}

	private void buildRoad() {
		Point startpoint = setPointByCoordinate("road starting point");
		Point endpoint = setPointByCoordinate("road endpoint");
		if (siedlerGame.buildRoad(startpoint, endpoint)) {
			outputWriter.print(terminal, Config.SuccessMessage.BUILD.toString(), new String[] { "Road" });
		} else {
			outputWriter.print(terminal, Config.ErrorMessage.BUILDING.toString(), new String[] { "Road" });
		}
	}

	private void buildCity() {
		if (siedlerGame.buildCity(setPointByCoordinate("city"))) {
			outputWriter.print(terminal, Config.SuccessMessage.BUILD.toString(), new String[] { "City" });
		} else {
			outputWriter.print(terminal, Config.ErrorMessage.BUILDING.toString(), new String[] { "City" });
		}
	}

	private Point setPointByCoordinate(String keyWord) {
		return new Point(inputManager.selectCoordinatePoint(textIO, keyWord));
	}

	private void tradingSelection() {
		Config.Resource wantedResource = inputManager.getResourceInput(textIO,
				Config.PromptMessage.TRADE_RESOURCE_WANT.toString());
		Config.Resource offeredResource = inputManager.getResourceInput(textIO,
				Config.PromptMessage.TRADE_RESOURCE_OFFER.toString());
		if (siedlerGame.tradeWithBankFourToOne(offeredResource, wantedResource)) {
			outputWriter.print(textIO.getTextTerminal(), Config.SuccessMessage.TRADE_WITH_BANK.toString());
		} else {
			outputWriter.print(textIO.getTextTerminal(), Config.ErrorMessage.TRADE_WITH_BANK.toString());
		}
	}

	private void printWinPointStandings() {
		Map<Config.Faction, Integer> points = siedlerGame.calculateWinPoints();
		for (Config.Faction faction : points.keySet()) {
			outputWriter.print(terminal, Config.InfoMessage.WIN_POINT_STANDING.toString(),
					new String[] { faction.toString(), String.valueOf(points.get(faction)) });
		}

	}

	private boolean isGameOver() {
		if (!Objects.isNull(siedlerGame.getWinner())) {
			return true;
		}
		return false;
	}

	private void endGame() {
		outputWriter.print(terminal, Config.InfoMessage.WINNER.toString(),
				new String[] { siedlerGame.getWinner().toString() });
	}
}
