package de.fhkiel.ki.cathedral.utility;

import static de.fhkiel.ki.cathedral.utility.GameAsAString.PASS;
import static de.fhkiel.ki.cathedral.utility.GameAsAString.gameToString;
import static de.fhkiel.ki.cathedral.utility.GameAsAString.parseTurn;

import de.fhkiel.ki.cathedral.game.Game;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

public class DataManagement {
  private DataManagement(){}

  public static void saveGame(Game game, String fileName, PrintStream out){
    try(BufferedWriter gamefile = new BufferedWriter(new FileWriter(fileName))){
      gamefile.write(gameToString(game));
    } catch (IOException e) {
      out.println(e.toString());
      throw new CouldNotWriteFileRuntimeException(e);
    }
  }

  public static void saveGame(Game game, String fileName){
    saveGame(game, fileName, System.out);
  }

  public static Game loadGame(String fileName, PrintStream out){
    Game game = new Game();
    try(BufferedReader gamefile = new BufferedReader(new FileReader(fileName))){
      gamefile.lines().forEachOrdered(
          s -> {
            if(s.equals(PASS)){
              game.passTurn();
            } else {
              game.takeTurn(parseTurn(s));
            }
          }
      );
    } catch (Exception e) {
      out.println(e.toString());
    }
    return game;
  }

  public static Game loadGame(String filename){
    return loadGame(filename, System.out);
  }
}
