package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.gui.Log.getLog;

import de.fhkiel.ki.cathedral.game.Building;
import de.fhkiel.ki.cathedral.game.Direction;
import de.fhkiel.ki.cathedral.game.Placement;
import java.awt.Color;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import javax.imageio.ImageIO;

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

  static String gameColorToString(de.fhkiel.ki.cathedral.game.Color color) {
    return switch (color) {
      case None -> "none";
      case Blue -> "blue";
      case Black -> "black";
      case Black_Owned -> "owned by black";
      case White -> "white";
      case White_Owned -> "owned by white";
    };
  }

  static int directionToNumber(Direction direction) {
    return switch (direction) {
      case _0 -> 0;
      case _90 -> 90;
      case _180 -> 180;
      case _270 -> 270;
    };
  }

  public static InputStream asInputStream(BufferedImage bi) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(bi, "png", baos);
      return new ByteArrayInputStream(baos.toByteArray());
    } catch (IOException e) {
      System.out.println(e.getLocalizedMessage());
    }
    return InputStream.nullInputStream();
  }

  public static Placement parseTurn(String trim) {
    try {
      String[] commands = trim.split(" ");

      Building building = null;
      if (commands.length == 4) {
        try {
          int id = Integer.parseInt(commands[0]);
          Optional<Building> pB = Arrays.stream(Building.values()).filter(b -> b.getId() == id).findAny();
          if (pB.isPresent()) {
            building = pB.get();
          } else {
            getLog().addText("No building with id " + id);
            return new Placement(-1, -1, Direction._0, Building.Blue_Cathedral);
          }
        } catch (NumberFormatException exception) {
          getLog().addText("Can not parse: " + commands[0]);
          return new Placement(-1, -1, Direction._0, Building.Blue_Cathedral);
        }
      } else if (commands.length == 5) {
        String bString = commands[0].toLowerCase(Locale.ROOT) + " " + commands[1].toLowerCase(Locale.ROOT);
        Optional<Building> pB = Arrays.stream(Building.values()).filter(b -> b.getName().toLowerCase(Locale.ROOT).equals(bString)).findAny();
        if (pB.isPresent()) {
          building = pB.get();
        } else {
          getLog().addText("No building with color and name " + commands[0] + " " + commands[1]);
          return new Placement(-1, -1, Direction._0, Building.Blue_Cathedral);
        }
      } else {
        getLog().addText("Not a valid command: " + trim);
        return new Placement(-1, -1, Direction._0, Building.Blue_Cathedral);
      }

      Direction direction;
      switch (commands[commands.length - 3].toLowerCase(Locale.ROOT)) {
        case "0":
          direction = Direction._0;
          break;
        case "90":
          direction = Direction._90;
          break;
        case "180":
          direction = Direction._180;
          break;
        case "270":
          direction = Direction._270;
          break;
        default:
          getLog().addText("Can not parse direction " + commands[commands.length - 3]);
          return new Placement(-1, -1, Direction._0, Building.Blue_Cathedral);
      }

      int x;
      try {
        x = Integer.parseInt(commands[commands.length - 2]);
      } catch (NumberFormatException exception) {
        getLog().addText("Can not parse x: " + commands[commands.length - 2]);
        return new Placement(-1, -1, Direction._0, Building.Blue_Cathedral);
      }

      int y;
      try {
        y = Integer.parseInt(commands[commands.length - 1]);
      } catch (NumberFormatException exception) {
        getLog().addText("Can not parse y: " + commands[commands.length - 1]);
        return new Placement(-1, -1, Direction._0, Building.Blue_Cathedral);
      }

      return new Placement(x, y, direction, building);

    } catch (Exception exception) {
      getLog().addText("Error parsing command: " + trim + " -> " + exception.getMessage());
    }
    return new Placement(-1, -1, Direction._0, Building.Blue_Cathedral);
  }

}
