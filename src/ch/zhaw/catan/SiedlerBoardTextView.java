package ch.zhaw.catan;

import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoardTextView;
import ch.zhaw.structures.Road;
import ch.zhaw.structures.Settlement;


/**
 * The SiedlerBoardTextView class is a subclass of {@link ch.zhaw.hexboard.HexBoardTextView}.
 * It is responsible for the text view representation of the {@link SiedlerBoard} in the CLI of the game.
 * For the extended class it sets the following parameters:
 * <ul>
 *     <li>Fields as {@link Land}</li>
 *     <li>Corners as {@link Settlement}</li>
 *     <li>Edges as {@link Road}</li>
 *     <li>Annotations as {@link String}</li>
 * </ul>
 */
public class SiedlerBoardTextView extends HexBoardTextView<Land, Settlement, Road, String> {


  /**
   * Creates a text view for a {@link SiedlerBoard}
   * @param board a SiedlerBoard
   */
  public SiedlerBoardTextView(SiedlerBoard board) {
    super(board);
  }

}
