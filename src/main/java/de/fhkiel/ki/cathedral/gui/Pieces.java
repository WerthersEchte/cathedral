package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.getGame;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.register;
import static java.util.function.Predicate.not;

import de.fhkiel.ki.cathedral.game.Building;
import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Turn;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;

class Pieces extends JPanel implements ControlGameProxy.Listener {

  private final List<Piece> buildings = new ArrayList<>();

  public Pieces(JPanel parent) {
    register(this);

    setLayout(null);

    parent.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        resizePieces(parent);
      }
    });
  }

  public void add(Piece piece) {
    super.add(piece);
    buildings.add(piece);
  }

  public void resizePieces(JPanel parent) {
    for (Piece piece : buildings) {
      piece.setPreferredSize(new Dimension(parent.getHeight() * piece.cellNumber / 11, parent.getHeight() * piece.cellNumber / 11));
      piece.setMaximumSize(new Dimension(parent.getHeight() * piece.cellNumber / 11, parent.getHeight() * piece.cellNumber / 11));
      piece.setBounds(
          Math.max(0, Math.min(getWidth() - piece.getWidth(), piece.getLocation().x)),
          Math.max(0, Math.min(getHeight() - piece.getHeight(), piece.getLocation().y)),
          parent.getHeight() * piece.cellNumber / 11, parent.getHeight() * piece.cellNumber / 11);
    }
    parent.revalidate();
  }

  @Override
  public void newGame() {
    for (Piece piece : buildings) {
      piece.setVisible(true);
    }
  }

  @Override
  public void newPlacement(Placement placement) {
    for (Piece piece : buildings) {
      if (piece.building.getId() == placement.building().getId() && piece.isVisible()) {
        piece.setVisible(false);
        placedPieces();
        return;
      }
    }
  }

  @Override
  public void undoTurn(Turn turn) {
    for (Piece piece : buildings) {
      if (piece.building.getId() == turn.getAction().building().getId() && !piece.isVisible()) {
        piece.setVisible(true);
        placedPieces();
        return;
      }
    }
  }

  private void placedPieces(){
    for(Map.Entry<Building, Integer> pB : getGame().getBoard().getFreeBuildings().entrySet()){
      List<Piece> pieces = buildings.stream().filter(piece -> piece.building.getId() == pB.getKey().getId()).toList();
      long visible = pieces.stream().filter(Component::isVisible).count();
      if(visible > pB.getValue()){
        pieces.stream().filter(JComponent::isVisible).limit(visible-pB.getValue()).forEach(piece -> piece.setVisible(false));
      } else if(visible < pB.getValue()){
        pieces.stream().filter(not(JComponent::isVisible)).limit(pB.getValue()-visible).forEach(piece -> piece.setVisible(true));
      }
    }
  }

}
