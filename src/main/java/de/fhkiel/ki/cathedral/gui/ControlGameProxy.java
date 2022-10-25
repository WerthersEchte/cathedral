package de.fhkiel.ki.cathedral.gui;

import de.fhkiel.ki.cathedral.game.Game;
import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Turn;
import java.util.HashSet;
import java.util.Set;

class ControlGameProxy {
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

  interface Listener {
    default void newGame(){};

    default void newPlacement(Placement placement){};

    default void undoTurn(Turn turn){};

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
