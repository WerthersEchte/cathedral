package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.getGame;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.register;
import static de.fhkiel.ki.cathedral.gui.Log.getLog;
import static de.fhkiel.ki.cathedral.gui.Util.gameColorToFontcolor;
import static de.fhkiel.ki.cathedral.gui.Util.gameColorToPaint;

import de.fhkiel.ki.cathedral.game.Color;
import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Turn;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class Info extends JPanel implements ControlGameProxy.Listener {

  private final JLabel black;
  private final JLabel white;

  Info() {
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

    JPanel blackPanel = new JPanel();
    black = new JLabel("47");
    black.setHorizontalAlignment(SwingConstants.CENTER);
    black.setVerticalAlignment(SwingConstants.CENTER);
    blackPanel.setBackground(gameColorToPaint(Color.Black));
    black.setForeground(gameColorToFontcolor(Color.Black));
    blackPanel.add(black);
    add(blackPanel);

    JPanel whitePanel = new JPanel();
    white = new JLabel("47");
    white.setHorizontalAlignment(SwingConstants.CENTER);
    white.setVerticalAlignment(SwingConstants.CENTER);
    whitePanel.setBackground(gameColorToPaint(Color.White));
    white.setForeground(gameColorToFontcolor(Color.White));
    whitePanel.add(white);
    add(whitePanel);

    register(this);
  }

  void setScore() {
    int b = getGame().score().get(Color.Black);
    int w = getGame().score().get(Color.White);
    black.setText("" + b);
    white.setText("" + w);
    if (getGame().isFinished()) {
      if (b == w) {
        getLog().addText("Game is finished: draw");
      } else {
        getLog().addText("Game is finished: " + (b < w ? "Black wins" : "White wins"));
      }
    }
  }

  @Override
  public void newGame() {
    setScore();
  }

  @Override
  public void newPlacement(Placement placement) {
    setScore();
  }

  @Override
  public void undoTurn(Turn turn) {
    setScore();
  }

  @Override
  public void undoTurn() {
    setScore();
  }
}
