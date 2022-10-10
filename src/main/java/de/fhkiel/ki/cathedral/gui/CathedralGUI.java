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

  private CathedralGUI(){}

  /**
   * Start the gui. Alle AI agents get added on initialized
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




    }

  }
}
