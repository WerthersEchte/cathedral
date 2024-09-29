package de.fhkiel.ki.cathedral.utility;

import de.fhkiel.ki.cathedral.game.Building;
import de.fhkiel.ki.cathedral.game.Direction;
import de.fhkiel.ki.cathedral.game.Game;
import de.fhkiel.ki.cathedral.game.Placement;
import java.util.Arrays;

// ToDo: test functions
public class GameAsAString {

  public static final String PASS = "Pass";

  private GameAsAString(){}

  public static String placementToString(Placement placement){
    return placement.building().getId() + " " +
        placement.direction().toString().replace("_", "") + " " +
        placement.x() + " " +
        placement.y();
  }

  public static String gameToString(Game game){
    Game gameToLookAt = game.copy();
    StringBuilder gameString = new StringBuilder("");

    while(gameToLookAt.lastTurn().getTurnNumber() > 0){
      gameString.insert(0,"\n");
      if(gameToLookAt.lastTurn().hasAction()) {
        gameString.insert(0, placementToString(gameToLookAt.lastTurn().getAction()));
      } else {
        gameString.insert(0, PASS);
      }
      gameToLookAt.undoLastTurn();
    }
    return gameString.toString();
  }

  public static Placement parseTurn(String turn) {
    try {
      String[] parameter = turn.split(" ");

      int id = Integer.parseInt(parameter[0]);
      int degree = Integer.parseInt(parameter[1]);
      int x = Integer.parseInt(parameter[2]);
      int y = Integer.parseInt(parameter[3]);

      Building building = Arrays.stream(Building.values()).filter(b -> b.getId() == id).findAny().orElseThrow(
          () -> new RuntimeException("No building with id " + id)
      );

      Direction direction = switch (degree) {
        case 0 -> Direction._0;
        case 90 -> Direction._90;
        case 180 -> Direction._180;
        case 270 -> Direction._270;
        default -> throw new RuntimeException("Can not parse direction " + degree);
      };

      return new Placement(x, y, direction, building);
    } catch (Exception exception) {
      throw new RuntimeException("Error parsing turn: " + turn + " -> " + exception.getMessage());
    }
  }
}
