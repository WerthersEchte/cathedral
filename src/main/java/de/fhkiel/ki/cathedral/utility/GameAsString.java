package de.fhkiel.ki.cathedral.utility;

import static de.fhkiel.ki.cathedral.gui.Log.getLog;

import de.fhkiel.ki.cathedral.game.Building;
import de.fhkiel.ki.cathedral.game.Direction;
import de.fhkiel.ki.cathedral.game.Placement;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class GameAsString {
  private GameAsString(){}

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
