package de.fhkiel.ki.cathedral.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Core class used to handle a full game of cathedral.
 *
 * The Game class is the core of this framework. It stores and handles all interactions and keeps
 * track of all players and the rules pertaining them.These rules can be toggled with
 * {@link Game#ignoreRules(boolean)}.<br>
 * Example usage:
 *
 * <pre>
 *     // Instantiate the game
 *     Game game = new Game();
 *     // print the empty game to the console
 *     System.out.println(game);
 *
 *     // Place the cathedral
 *     // create the placement
 *     Placement cathedral = new Placement(3, 3, Direction._0, Building.Blue_Cathedral);
 *     // make the placement
 *     game.takeTurn(cathedral);
 *     // print the game to the console
 *     System.out.println(game);
 *
 *     // Place the black academy
 *     // create the placement
 *     Placement blackAcademy = new Placement(8, 3, Direction._270, Building.Black_Academy);
 *     // make the placement
 *     game.takeTurn(blackAcademy);
 *     // print the game to the console
 *     System.out.println(game);
 * </pre>
 * @author Eike Petersen {@literal <eike.petersen@fh-kiel.de>}
 * @version 1.0
 * @since 1.0
 */
public class Game {
  private final List<Turn> turns = new ArrayList<>();
  private boolean ignoreRules = false;

  /**
   * Instantiates a new Game with all needed settings and an empty {@link Board}.
   */
  public Game() {
    this(new Board());
  }

  /**
   * Instantiates a new Game with default settings and the given {@link Board}.
   *
   * @param starter the starting {@link Board}
   */
  public Game(Board starter) {
    turns.add(new Turn(0, starter, null));
  }

  /**
   * Deep copy of the full game (EXPENSIVE).
   *
   * @return the new game-copy
   */
  public Game copy() {
    Game gameCopy = new Game(null);
    gameCopy.turns.clear();
    gameCopy.ignoreRules = ignoreRules;

    turns.forEach(turn -> gameCopy.turns.add(turn.copy()));

    return gameCopy;
  }

  /**
   * Make a {@link Placement} on the current {@link Board}.
   * The placement has to be valid and, if the rules are not ignored
   * ({@link Game#ignoreRules(boolean)}), from the current player {@link Color}
   *
   *
   * @param placement the {@link Placement} to be made
   * @return if the {@link Placement} could be successfully placed on the current {@link Board}
   */
  public boolean takeTurn(Placement placement) {
    return takeTurn(placement, false);
  }

  /**
   * Make a {@link Placement} on the current {@link Board}.
   * The placement has to be valid and, if the rules are not ignored
   * ({@link Game#ignoreRules(boolean)}), from the current player {@link Color}.
   *
   * @param placement the {@link Placement} to be made
   * @param fast      if expensive region-computations should be skipped
   *                  (can lead to wrong boardstates)
   * @return if the {@link Placement} could be successfully placed on the current {@link Board}
   */
  public boolean takeTurn(Placement placement, boolean fast) {
    if (placement == null
        || placement.building() == null
        || placement.position() == null
        || !placement.position().isViable()
    ) {
      return false;
    }

    if (!ignoreRules && placement.building().getColor() != getCurrentPlayer()) {
      return false;
    }
    Board nextBoardState = lastTurn().getBoard().copy();

    if (!nextBoardState.placeBuilding(placement, fast)) {
      return false;
    }

    turns.add(new Turn(turns.size(), nextBoardState, placement));

    return true;
  }

  /**
   * Gets the last {@link Turn}.
   * If no turn has been taken, a turn with the starting {@link Board} and
   * no {@link Turn#getAction()} will be returned.
   *
   * @return the last {@link Turn}
   */
  public Turn lastTurn() {
    return turns.get(turns.size() - 1);
  }

  /**
   * Undo last the last turn.
   * All changes will be reverted. Can be used to get back to the starting {@link Board}.
   */
  public void undoLastTurn() {
    if (turns.size() > 1) {
      turns.remove(turns.size() - 1);
    }
  }

  /**
   * Forfeit the current turn.
   * An no action {@link Turn} will be taken and the next player will be able to play.
   */
  public void forfeitTurn() {
    turns.add(new Turn(turns.size(), lastTurn().getBoard().copy()));
  }

  /**
   * Gets the current player {@link Color}.
   * Can be the {@link Color#Blue}, {@link Color#Black} or {@link Color#White}
   *
   * @return the current player {@link Color}
   */
  public Color getCurrentPlayer() {
    if (turns.size() == 1) {
      return Color.Blue;
    } else {
      if (turns.size() % 2 == 0) {
        return Color.Black;
      }
      return Color.White;
    }
  }

  /**
   * Gets a list of unplaced {@link Building}s for the current player.
   * if rules are ignored ({@link Game#ignoreRules(boolean)} all unplaced buildings are returned.
   *
   * @return the list of unplaced {@link Building}s
   */
  public List<Building> getPlacableBuildings() {
    if (ignoreRules) {
      return getAllUnplacedBuildings();
    }
    return getPlacableBuildings(getCurrentPlayer());
  }

  /**
   * Gets a list of unplaced {@link Building}s for the player {@link Color}.
   *
   * @param player the {@link Color} of the player
   * @return the list of unplaced {@link Building}s
   */
  public List<Building> getPlacableBuildings(Color player) {
    return lastTurn().getBoard().getPlacableBuildings(player);
  }

  /**
   * Gets all currently unplaced {@link Building}s in a list.
   * There are no numbers for {@link Building}s. The buildings can be all {@link Color}s.
   *
   * @return a list of all currently unplaced {@link Building}s
   */
  public List<Building> getAllUnplacedBuildings() {
    return lastTurn().getBoard().getAllUnplacedBuildings();
  }

  /**
   * Gets the current {@link Board}.
   * It is equivalent with the {@link Board} of the last turn.
   *
   * @return the current board
   */
  public Board getBoard() {
    return lastTurn().getBoard();
  }

  /**
   * The score contained in a map.
   * There are always scores for the {@link Color#Black} and the {@link Color#White} contained. <br>
   * They range from 47 to 0 and show the aggregate of all unplaced buildings of the chosen color.
   *
   * @return the map with the score for {@link Color#Black} and {@link Color#White}
   */
  public Map<Color, Integer> score() {
    return lastTurn().score();
  }


  /**
   * Is the game finished.
   *
   * A game is finished if no {@link Building}s can be placed.
   *
   * @return true if no buildings can be placed, false otherwise
   */
  public boolean isFinished(){
    for(Building unplacedBuilding: getBoard().getAllUnplacedBuildings()){
      if(!unplacedBuilding.getPossiblePlacements(this).isEmpty()){
        return false;
      }
    }
    return true;
  }

  /**
   * Enables or disables checks witch player can place stones
   * If set to true any building can be placed anytime. It still has to be a valid position.
   *
   * @param ignoreRules should certain rules be ignored
   */
  public void ignoreRules(boolean ignoreRules) {
    this.ignoreRules = ignoreRules;
  }

  /**
   * Returns if certain rules can be ignored
   * If set to true any building can be placed anytime. It still has to be a valid position.
   *
   * @return returns if rules are being ignored
   */
  public boolean ignoreRules() {
    return ignoreRules;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Game game = (Game) o;
    return ignoreRules == game.ignoreRules && Objects.equals(turns, game.turns);
  }

  @Override
  public int hashCode() {
    return Objects.hash(turns, ignoreRules);
  }

  @Override
  public String toString() {
    return "Current Turn: " + turns.size() + (ignoreRules ? " (Rules ignored)" : "") + "\nLast Turn and Boardstate:\n" + lastTurn();
  }
}
