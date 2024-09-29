package BDD.steps;

import static de.fhkiel.ki.cathedral.utility.DataManagement.loadGame;
import static de.fhkiel.ki.cathedral.utility.DataManagement.saveGame;
import static de.fhkiel.ki.cathedral.utility.GameAsAString.gameToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.matchers.text.ValuePrinter.print;

import de.fhkiel.ki.cathedral.game.Building;
import de.fhkiel.ki.cathedral.game.Color;
import de.fhkiel.ki.cathedral.game.Game;
import de.fhkiel.ki.cathedral.game.Placement;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SaveAndLoad {

  Game gameToTestWith;
  Game loadedGame;
  String gameFileName;
  File saveGameFile;

  @After
  public void afterScenario() {
    new File(gameFileName).delete();
  }

  @Given("an empty or new Game")
  public void an_empty_new_game() {
    gameToTestWith = new Game();
  }

  @When("it is saved as {string}")
  @When("it has been saved as {string}")
  @When("a Game has been saved as {string}")
  public void it_is_saved_as(String filename) {
    gameFileName = filename;
    saveGame(gameToTestWith, filename);
  }

  @Then("there exists a file with the name {string}")
  public void there_is_a_file_with_the_name(String filename) {
    saveGameFile = new File(filename);
    assertThat(saveGameFile).isFile();
  }

  @Then("it is empty")
  public void it_is_empty() {
    assertThat(readFromFile(saveGameFile)).isEmpty();
  }

  @When("the file with the name {string} gets loaded")
  public void the_file_with_the_name_gets_loaded(String gameFileName) {
    this.gameFileName = gameFileName;
    loadedGame = loadGame(gameFileName);
  }

  @Then("there is a game")
  public void there_is_a_game() {
    assertThat(loadedGame).isNotNull();
  }

  @Then("it contains no turns")
  public void it_contains_no_turns() {
    assertThat(loadedGame.lastTurn().getTurnNumber()).isZero();
  }

  @Then("it contains {int} turns")
  public void it_contains_turns(Integer turns) {
    assertThat(loadedGame.lastTurn().getTurnNumber()).isEqualTo(turns);
  }

  @Then("it is equal to the given game")
  public void it_is_equal_to_the_given_game() {
    assertThat(loadedGame).isEqualTo(gameToTestWith);
  }

  @Given("a Game with {int} turns")
  public void a_game_with_turns(Integer turns) {
    gameToTestWith = new Game();
    for(int i = 0; i < turns; ++i){
      gameToTestWith.takeTurn(getRandomPlacement(gameToTestWith));
    }
    assertThat(gameToTestWith.lastTurn().getTurnNumber()).isEqualTo(turns);
  }

  @Then("it contains the turns in shorthand")
  public void it_contains_the_turns_in_shorthand() {
    final String gameFileContent = readFromFile(saveGameFile);
    assertThat(gameFileContent).isEqualTo(gameToString(gameToTestWith));
  }

  @Given("a finished Game")
  public void a_finished_game() {
    gameToTestWith = new Game();
    do{
      Placement next = getRandomPlacement(gameToTestWith);
      if(next != null) {
        gameToTestWith.takeTurn(next);
      } else {
        gameToTestWith.passTurn();
      }
    } while(!gameToTestWith.isFinished());
  }

  @Then("it contains the turns")
  public void it_contains_the_turns() {
    assertThat(loadedGame.lastTurn().getTurnNumber()).isEqualTo(gameToTestWith.lastTurn().getTurnNumber());
  }

  @Then("it is finished")
  public void it_is_finished() {
    assertThat(loadedGame.isFinished()).isTrue();
  }

  private Placement getRandomPlacement(Game game){
    List<Placement> allPossiblePlacementsForTurn = new ArrayList<>();
    for(Building b : game.getPlacableBuildings()){
      allPossiblePlacementsForTurn.addAll(b.getPossiblePlacements(game));
    }
    if(allPossiblePlacementsForTurn.isEmpty()){
      return null;
    } else {
      return allPossiblePlacementsForTurn.get(new Random().nextInt(allPossiblePlacementsForTurn.size()));
    }
  }

  private String readFromFile(File file) {
    StringBuilder resultStringBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
      String line;
      while ((line = br.readLine()) != null) {
        resultStringBuilder.append(line).append("\n");
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return resultStringBuilder.toString();
  }
}
