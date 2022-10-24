package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.game.Color.Black;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.getGame;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.register;

import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Turn;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextArea;

class Log extends JTextArea implements ControlGameProxy.Listener {
  private static Log LOG;
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH.mm.ss.SSS");

  private Log() {
    register(this);
    setEditable(false);

    addText("New Game started");
  }

  public static Log getLog() {
    if (LOG == null) {
      LOG = new Log();
    }
    return LOG;
  }

  void addText(String text) {
    append(dateFormat.format(new Date()) + " " + text + "\n");
    setCaretPosition(getDocument().getLength());
  }

  @Override
  public void newGame() {
    addText("New Game started");
  }

  @Override
  public void newPlacement(Placement placement) {
    addText("New Placement: " + placement);
  }

  @Override
  public void undoTurn(Turn turn) {
    addText("Turn undone: " + turn.getTurnNumber() + " " + turn.getAction());
  }

  @Override
  public void failedPlacement(Placement placement) {
    addText("Placement failed: " + placement);
  }

  @Override
  public void noPlacement() {
    addText("Player " + (getGame().getCurrentPlayer() == Black ? "White" : "Black") + " skipped turn");
  }
}
