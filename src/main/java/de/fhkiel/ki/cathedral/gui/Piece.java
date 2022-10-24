package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.takeTurn;
import static de.fhkiel.ki.cathedral.gui.Util.directionToText;
import static de.fhkiel.ki.cathedral.gui.Util.gameColorToFontcolor;
import static de.fhkiel.ki.cathedral.gui.Util.gameColorToPaint;
import static de.fhkiel.ki.cathedral.gui.Util.isMouseWithinComponent;
import static java.awt.event.MouseEvent.BUTTON1;
import static java.awt.event.MouseEvent.BUTTON3;

import de.fhkiel.ki.cathedral.game.Building;
import de.fhkiel.ki.cathedral.game.Direction;
import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Position;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

class Piece extends JPanel implements MouseMotionListener, MouseListener {
  public final int cellNumber;
  final Building building;
  private final List<Cell> cells = new ArrayList<>();
  Point pressedPosition = null;
  Point oldPiecePosition = null;
  JPanel glasspane = null;
  JPanel pieces = null;
  Board board = null;
  Direction direction = Direction._0;
  // for blinking bug
  boolean dragged = false;

  public Piece(Building building, JPanel glasspane, JPanel pieces, Board board) {
    this.glasspane = glasspane;
    this.pieces = pieces;
    this.board = board;

    this.building = building;

    if (building.getId() == 23) {
      cellNumber = 5;
    } else if (building.getId() == 1 || building.getId() == 12) {
      cellNumber = 1;
    } else {
      cellNumber = 3;
    }
    setLayout(new GridLayout(cellNumber, cellNumber));

    createBuilding();

    setPreferredSize(new Dimension(cellNumber * 30 - 4, cellNumber * 30 - 4));
    Random r = new Random();
    setBounds(
        r.nextInt(pieces.getWidth() - getPreferredSize().width),
        r.nextInt(pieces.getHeight() - getPreferredSize().height), getPreferredSize().width, getPreferredSize().height);

    setBackground(null);
    setOpaque(false);
  }

  private void createBuilding() {
    cells.clear();
    removeAll();
    boolean[][] field = new boolean[cellNumber][cellNumber];

    for (int i = 0; i < cellNumber * cellNumber; ++i) {
      JLabel gridPoint = new JLabel();
      int x = i % cellNumber - cellNumber / 2;
      int y = i / cellNumber - cellNumber / 2;
      if (building.turn(direction).contains(new Position(x, y))) {
        gridPoint.setFont(new Font(gridPoint.getFont().getName(), Font.PLAIN, 8));
        gridPoint.setBackground(gameColorToPaint(building.getColor()));
        gridPoint.setForeground(gameColorToFontcolor(building.getColor()));
        gridPoint.setOpaque(true);

        if (x == 0 && y == 0) {
          gridPoint.setText(building.getId() + "" + directionToText(direction));
        } else {
          gridPoint.setText(building.getId() + "");
        }
        gridPoint.setHorizontalAlignment(SwingConstants.CENTER);
        gridPoint.setVerticalAlignment(SwingConstants.CENTER);

        gridPoint.addMouseListener(this);
        gridPoint.addMouseMotionListener(this);

        field[i / cellNumber][i % cellNumber] = true;

        cells.add(new Cell(gridPoint, new Point(x, y)));
      }
      add(gridPoint);
    }

    for (Cell cell : cells) {
      cell.label.setBorder(
          BorderFactory.createMatteBorder(
              (cell.position.y == (-cellNumber / 2) || !field[cell.position.y + cellNumber / 2 - 1][cell.position.x + cellNumber / 2] ? 2 : 0),
              (cell.position.x == (-cellNumber / 2) || !field[cell.position.y + cellNumber / 2][cell.position.x + cellNumber / 2 - 1] ? 2 : 0),
              (cell.position.y == (cellNumber / 2) || !field[cell.position.y + cellNumber / 2 + 1][cell.position.x + cellNumber / 2] ? 2 : 0),
              (cell.position.x == (cellNumber / 2) || !field[cell.position.y + cellNumber / 2][cell.position.x + cellNumber / 2 + 1] ? 2 : 0),
              gameColorToFontcolor(building.getColor())));
    }

    revalidate();
  }

  public void mousePressed(MouseEvent e) {
    if (e.getButton() != BUTTON1) {
      return;
    }

    oldPiecePosition = getLocation();
    pressedPosition = e.getPoint();
    pieces.remove(this);
    glasspane.add(this);
    revalidate();
    dragged = false;
  }

  public void mouseDragged(MouseEvent e) {
    if (pressedPosition != null) {
      setLocation(getLocation().x + e.getX() - pressedPosition.x, getLocation().y + e.getY() - pressedPosition.y);
      dragged = true;
    }
  }

  public void mouseReleased(MouseEvent e) {
    if (e.getButton() != BUTTON1) {
      return;
    }

    if (isMouseWithinComponent(pieces) && dragged) {
      glasspane.remove(this);
      pieces.add(this, 0);
      setLocation(
          Math.max(0,
              Math.min(pieces.getWidth() - getWidth(),
                  getLocation().x - (pieces.getLocationOnScreen().x - glasspane.getLocationOnScreen().x))),
          Math.max(0, Math.min(pieces.getHeight() - getHeight(),
              getLocation().y - (pieces.getLocationOnScreen().y - glasspane.getLocationOnScreen().y))));

    } else if (isMouseWithinComponent(board)) {
      Optional<Point> cell = board.witchCell();
      if (cell.isPresent()) {
        Point grip = null;
        for (Cell c : cells) {
          if (c.label == e.getComponent()) {
            grip = c.position;
          }
        }
        if (grip != null) {
          if (takeTurn(new Placement(new Position(-grip.x + cell.get().x, -grip.y + cell.get().y), direction, building))) {
            setVisible(false);
          }
        }
      }
      resetPiece();
    } else {
      resetPiece();
    }
    pressedPosition = null;
    SwingUtilities.getRoot(this).revalidate();
    SwingUtilities.getRoot(this).repaint();
  }

  private void resetPiece() {
    setLocation(oldPiecePosition);
    glasspane.remove(this);
    pieces.add(this, 0);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (e.getButton() == BUTTON3) {
      direction = building.getTurnable().getPossibleDirections().get((direction.getId() + 1) % building.getTurnable().getNumberOfPossibleForms());
      createBuilding();
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // not needed
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // not needed
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    // not needed
  }
}
