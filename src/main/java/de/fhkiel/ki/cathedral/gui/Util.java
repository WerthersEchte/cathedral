package de.fhkiel.ki.cathedral.gui;

import de.fhkiel.ki.cathedral.game.Direction;
import java.awt.Color;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;

class Util {
  private Util() {
  }

  static boolean isMouseWithinComponent(Component c) {
    Point mousePos = MouseInfo.getPointerInfo().getLocation();
    Rectangle bounds = c.getBounds();
    bounds.setLocation(c.getLocationOnScreen());
    return bounds.contains(mousePos);
  }

  static Color gameColorToPaint(de.fhkiel.ki.cathedral.game.Color color) {
    return switch (color) {
      case Blue -> Color.BLUE;
      case Black -> Color.BLACK;
      case Black_Owned -> Color.GRAY;
      case White -> Color.WHITE;
      case White_Owned -> new Color(230, 230, 230);
      default -> null;
    };
  }

  static Color gameColorToFontcolor(de.fhkiel.ki.cathedral.game.Color color) {
    return switch (color) {
      case Blue -> Color.WHITE;
      case Black -> Color.WHITE;
      case White -> Color.BLACK;
      default -> Color.BLACK;
    };
  }

  static String directionToText(Direction direction) {
    return switch (direction) {
      case _0 -> "(0\u00B0)";
      case _90 -> "(90\u00B0)";
      case _180 -> "(180\u00B0)";
      case _270 -> "(270\u00B0)";
    };
  }

}
