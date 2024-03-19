package ch.zhaw.userinterface;

import org.beryx.textio.TextIO;

import ch.zhaw.catan.Config;

import java.awt.*;

/**
 * 
 * The InputManager class is responsible for getting validated user input
 *
 */
public class InputManager {

	/**
	 * Asks the user for an amount of player until a valid number is provided
	 * 
	 * @param textIO
	 * @return int specifying the amount of players
	 */
	public int specifyNumberOfPlayers(TextIO textIO) {
		return textIO.newIntInputReader().withMinVal(Config.MIN_NUMBER_OF_PLAYERS)
				.withMaxVal(Config.MAX_NUMBER_OF_PLAYERS).read(Config.PromptMessage.NUMBER_OF_PLAYERS.toString());
	}

	/**
	 * Asks the user for an action until a valid action is provided
	 * 
	 * @param textIO
	 * @return Config.Commands specifying the action
	 */
	public Config.Commands selectAction(TextIO textIO) {
		return textIO.newEnumInputReader(Config.Commands.class).read(Config.PromptMessage.ACTION.toString());
	}

	/**
	 * Asks the user for a build command until a valid build command is provided
	 * 
	 * @param textIO
	 * @return Config.BuildCommands specifying the build command
	 */
	public Config.BuildCommands selectBuildingType(TextIO textIO) {
		return textIO.newEnumInputReader(Config.BuildCommands.class).read(Config.PromptMessage.BUILDING_TYPE.toString());
	}


	/**
	 * This method get the coordinates as a string from the console with the format
	 * x:y and split this values by : in an Array.
	 * 
	 * @param textIO input console
	 * @return Point with two integer values
	 */
	public Point selectCoordinatePoint(TextIO textIO, String keyWord) {
		String[] input = textIO.newStringInputReader().withPattern("[0-9]+:[0-9]+").read(String.format(Config.PromptMessage.COORDINATE.toString(), keyWord)).split(":");
		return new Point(Integer.parseInt(input[0]), Integer.parseInt(input[1]));
	}

	/**
	 * Asks the user to provide a resource
	 * 
	 * @param textIO
	 * @param promptMessage
	 * @return Config.Resource specifying the resource
	 */
	public Config.Resource getResourceInput(TextIO textIO, String promptMessage) {
		return textIO.newEnumInputReader(Config.Resource.class).read(promptMessage);
	}

}
