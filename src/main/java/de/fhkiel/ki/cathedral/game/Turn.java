package de.fhkiel.ki.cathedral.game;

import java.util.Map;
import java.util.Objects;

/**
 * The class Turn is a storage class to store turns in a {@link Game}.
 * <br><br>
 * A Turn is a self-contained class to store all the necessary information to describe what
 * happened in a given turn.<br>
 * It contains the turn-number, the {@link Placement} done this turn and the
 * resulting {@link Board}.
 *
 * @author Eike Petersen {@literal <eike.petersen@fh-kiel.de>}
 * @version 1.0
 * @since 1.0
 */
public class Turn {
  private final int turnNumber;
  private final Board board;

  private final Placement action;

  /**
   * Instantiates a new Turn.
   *
   * @param turnNumber the turn number
   * @param board      the resulting board
   * @param action     the {@link Placement} done
   */
  public Turn(int turnNumber, Board board, Placement action) {
    this.turnNumber = turnNumber;
    this.board = board;
    this.action = action;
  }

  /**
   * Instantiates a new Turn without a {@link Placement}.
   * Is used for the first and forfeit turns.
   *
   * @param turnNumber the turn number
   * @param board      the board
   */
  public Turn(int turnNumber, Board board) {
    this.turnNumber = turnNumber;
    this.board = board;
    this.action = null;
  }

  /**
   * Creates a deep copy of the turn.
   *
   * @return the copy
   */
  public Turn copy() {
    return new Turn(turnNumber, board.copy(), action);
  }

  /**
   * A delegate method for {@link Board#score()}.
   *
   * @return a map containing the scores
   */
  public Map<Color, Integer> score() {
    return board.score();
  }

  /**
   * Gets turn number.
   *
   * @return the turn number
   */
  public int getTurnNumber() {
    return turnNumber;
  }

  /**
   * Gets the resulting {@link Board}.
   *
   * @return the resulting {@link Board} from this turn
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Returns if a building got placed in this turn.
   *
   * @return true if a building got placed, false otherwise
   */
  public boolean hasAction() {
    return action != null;
  }

  /**
   * Gets the executed {@link Placement}. Can be null.
   *
   * @return the {@link Placement} or null when no {@link Placement} happened
   */
  public Placement getAction() {
    return action;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Turn turn = (Turn) o;
    return turnNumber == turn.turnNumber
        && Objects.equals(board, turn.board)
        && Objects.equals(action, turn.action);
  }

  @Override
  public int hashCode() {
    return Objects.hash(turnNumber, board, action);
  }

  /**
   * Creates a string in the form
   * 'Turn: &#60;turnNumber&#62; Action: {@link Placement} \n
   * Score: B &#60;scoreBlack&#62; | &#60;scoreWhite&#62; W\n
   * {@link Placement}'.
   *
   * @return the string representing the turn
   */
  @Override
  public String toString() {
    return
        "Turn: " + turnNumber
        + " Action: " + (action!=null?action:"No Action")
        + "\nScore: B " + score().get(Color.Black) + " | " + score().get(Color.White)
        + " W\n" + board;
  }
}
