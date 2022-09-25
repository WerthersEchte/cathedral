package de.fhkiel.ki.cathedral.gui;

import de.fhkiel.ki.cathedral.ai.Agent;
import de.fhkiel.ki.cathedral.game.Game;
import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Turn;
import java.util.HashSet;
import java.util.Set;

/**
 * A GUI to run Cathedral games. Can be used to test AI agents.
 *
 * @author      Eike Petersen {@literal <eike.petersen@fh-kiel.de>}
 * @version     1.0
 * @since       1.0
 */
public class CathedralGUI {

  private static final Set<Listener> listeners = new HashSet<>();
  private static Game GAME;

  static Game getGame() {
    if (GAME == null) {
      GAME = new Game();
    }
    return GAME;
  }

  static void ignoreRules(boolean ignoreRules) {
    getGame().ignoreRules(ignoreRules);
    listeners.forEach(Listener::gameChanged);
  }

  static void resetGame() {
    GAME = new Game();
    listeners.forEach(Listener::newGame);
  }

  static boolean takeTurn(Placement placement) {
    if (placement != null) {
      boolean success = GAME.takeTurn(placement);
      if (success) {
        listeners.forEach(listener -> listener.newPlacement(placement));
      } else {
        listeners.forEach(listener -> listener.failedPlacement(placement));
      }
      return success;
    }
    GAME.forfeitTurn();
    listeners.forEach(Listener::noPlacement);
    return true;
  }

  static void undoTurn() {
    if (getGame().lastTurn().hasAction()) {
      Turn turn = getGame().lastTurn();
      getGame().undoLastTurn();
      listeners.forEach(listener -> listener.undoTurn(turn));
    } else {
      getGame().undoLastTurn();
      listeners.forEach(Listener::undoTurn);
    }
  }

  static void register(Listener listener) {
    listeners.add(listener);
  }

  static void unregister(Listener listener) {
    listeners.remove(listener);
  }

  private CathedralGUI(){}

  /**
   * Start the gui. Alle AI agents get added an initialized
   *
   * @param agents the AI agents
   */
  public static void start(Agent... agents) {
    Base base = new Base();
    base.create();

    for (Agent agent : agents) {
      base.addAI(agent.name(), new AI(agent));
    }

    base.setVisible(true);
  }

  interface Listener {
    void newGame();

    void newPlacement(Placement placement);

    void undoTurn(Turn turn);

    default void gameChanged() {
    }

    default void failedPlacement(Placement placement) {
    }

    default void noPlacement() {
    }

    default void undoTurn() {
    }
  }
}