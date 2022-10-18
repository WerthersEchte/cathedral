package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.game.Color.Black;
import static de.fhkiel.ki.cathedral.game.Color.Blue;
import static de.fhkiel.ki.cathedral.game.Color.White;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.getGame;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.register;
import static de.fhkiel.ki.cathedral.gui.Util.directionToText;
import static de.fhkiel.ki.cathedral.gui.Util.gameColorToFontcolor;
import static de.fhkiel.ki.cathedral.gui.Util.gameColorToPaint;
import static de.fhkiel.ki.cathedral.gui.Util.isMouseWithinComponent;

import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Position;
import de.fhkiel.ki.cathedral.game.Turn;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class Board extends JPanel implements ControlGameProxy.Listener {
  private final List<JLabel> xLabels = new ArrayList<>();
  private final List<JLabel> yLabels = new ArrayList<>();

  private final List<Cell> cells = new ArrayList<>();

  public Board(JPanel parent) {
    register(this);

    setMinimumSize(new Dimension(300, 300));

    parent.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        setPreferredSize(new Dimension(parent.getHeight(), parent.getHeight()));
        setMaximumSize(new Dimension(parent.getHeight(), parent.getHeight()));
        parent.revalidate();
      }
    });

    setLayout(new GridLayout(11, 11));
    for (int i = 0; i < 121; ++i) {
      JLabel cell = new JLabel();
      cell.setHorizontalAlignment(SwingConstants.CENTER);
      cell.setVerticalAlignment(SwingConstants.CENTER);
      if (i > 10 && i % 11 != 0) {
        cell.setFont(new Font(cell.getFont().getName(), Font.PLAIN, 8));
        cell.setBackground(i % 2 == 0 ? Color.DARK_GRAY : Color.lightGray);
        cell.setOpaque(true);

        int x = (i + 10) % 11;
        int y = i / 11 - 1;

        cell.addMouseListener(new MouseAdapter() {
          Color oldColor;

          @Override
          public void mouseEntered(MouseEvent e) {
            oldColor = cell.getBackground();
            cell.setBackground(Color.RED);

            xLabels.get(x).setBackground(Color.RED);
            xLabels.get(x).setOpaque(true);
            yLabels.get(y).setBackground(Color.RED);
            yLabels.get(y).setOpaque(true);
          }

          @Override
          public void mouseExited(MouseEvent e) {
            cell.setBackground(oldColor);

            xLabels.get(x).setBackground(null);
            xLabels.get(x).setOpaque(false);
            yLabels.get(y).setBackground(null);
            yLabels.get(y).setOpaque(false);
          }
        });

        cells.add(new Cell(cell, new Point(x, y), cell.getBackground()));

      } else {
        if (i != 0) {
          cell.setText((i < 11 ? i : i / 11) - 1 + "");
          if (i < 11) {
            xLabels.add(cell);
          } else {
            yLabels.add(cell);
          }
        }
      }


      add(cell);
    }
  }

  public Optional<Point> witchCell() {
    for (Cell cell : cells) {
      if (isMouseWithinComponent(cell.label)) {
        return Optional.of(cell.position);
      }
    }
    return Optional.empty();
  }

  private void stateChanged() {
    for (Cell cell : cells) {
      switch (getGame().getBoard().getField()[cell.position.y][cell.position.x]) {
        case None:
          cell.label.setBackground(cell.base);
          cell.label.setText("");
          break;
        case Blue:
          cell.label.setBackground(gameColorToPaint(Blue));
          cell.label.setText(createText(Blue, cell.position));
          cell.label.setForeground(gameColorToFontcolor(Blue));
          break;
        case Black:
          cell.label.setBackground(gameColorToPaint(Black));
          cell.label.setText(createText(Black, cell.position));
          cell.label.setForeground(gameColorToFontcolor(Black));
          break;
        case Black_Owned:
          cell.label.setBackground(gameColorToPaint(de.fhkiel.ki.cathedral.game.Color.Black_Owned));
          cell.label.setText("");
          break;
        case White:
          cell.label.setBackground(gameColorToPaint(White));
          cell.label.setText(createText(White, cell.position));
          cell.label.setForeground(gameColorToFontcolor(White));
          break;
        case White_Owned:
          cell.label.setBackground(gameColorToPaint(de.fhkiel.ki.cathedral.game.Color.White_Owned));
          cell.label.setText("");
          break;
      }
    }
  }

  private String createText(de.fhkiel.ki.cathedral.game.Color color, Point position) {
    for (Placement placement : getGame().getBoard().getPlacedBuildings()) {
      if (placement.building().getColor() == color) {
        for (Position part : placement.form()) {
          if (Objects.equals(part.plus(placement.position()), new Position(position.x, position.y))) {
            if (part.equals(new Position(0, 0))) {
              return placement.building().getId() + "" + directionToText(placement.direction());
            } else {
              return placement.building().getId() + "";
            }
          }
        }
      }
    }
    return "";
  }

  @Override
  public void newGame() {
    stateChanged();
  }

  @Override
  public void newPlacement(Placement placement) {
    stateChanged();
  }

  @Override
  public void undoTurn(Turn turn) {
    stateChanged();
  }
}
