package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.getGame;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.register;
import static de.fhkiel.ki.cathedral.gui.Util.gameColorToFontcolor;
import static de.fhkiel.ki.cathedral.gui.Util.gameColorToPaint;

import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Turn;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class Player extends JPanel implements ControlGameProxy.Listener {
  private final JLabel label;

  Player() {
    register(this);

    setPreferredSize(new Dimension(100, 0));

    setOpaque(true);

    label = new JLabel();
    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setVerticalAlignment(SwingConstants.CENTER);
    add(label);

    setValues();
  }

  private void setValues() {
    if (getGame().ignoreRules()) {
      label.setText("All");
      setBackground(Color.GREEN);
      label.setForeground(Color.BLACK);
    } else {
      label.setText(getGame().getCurrentPlayer() == de.fhkiel.ki.cathedral.game.Color.Blue ? "Blue" :
          getGame().getCurrentPlayer() == de.fhkiel.ki.cathedral.game.Color.Black ? "Black" : "White");
      setBackground(gameColorToPaint(getGame().getCurrentPlayer()));
      label.setForeground(gameColorToFontcolor(getGame().getCurrentPlayer()));
    }
  }

  @Override
  public void newGame() {
    setValues();
  }

  @Override
  public void newPlacement(Placement placement) {
    setValues();
  }

  @Override
  public void undoTurn(Turn turn) {
    setValues();
  }

  @Override
  public void gameChanged() {
    setValues();
  }

  @Override
  public void noPlacement() {
    setValues();
  }
}
