package de.fhkiel.ki.cathedral.gui;

import de.fhkiel.ki.cathedral.ai.Agent;

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
    base.create( Settings.Builder().build() );

    for (Agent agent : agents) {
      base.addAI(agent.name(), new AI(agent));
    }

    base.setVisible(true);
  }


  /**
   * Start the gui. Alle AI agents get added on initialized
   * A Settings-Object can be set to configure the application
   *
   * @param settings the wanted settings
   * @param agents the AI agents
   */
  public static void start(Settings settings, Agent... agents) {
    Base base = new Base();
    base.create(settings);

    for (Agent agent : agents) {
      base.addAI(agent.name(), new AI(agent));
    }

    base.setVisible(true);
  }

}
