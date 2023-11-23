package BDD.steps;

import static de.fhkiel.ki.cathedral.utility.DataManagement.saveGame;
import static org.assertj.core.api.Assertions.assertThat;

import de.fhkiel.ki.cathedral.game.Game;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SaveAndLoad {

  Game gameToTestWith;
  String gameFileName;
  File saveGameFile;

  @Given("an empty or new Game")
  public void an_empty_new_game() {
    gameToTestWith = new Game();
  }

  @When("it is saved as {string}")
  @When("it has been saved as {string}")
  public void it_is_saved_as(String filename) {
    gameFileName = filename;
    saveGame(gameToTestWith, filename);
  }

  @Then("there should be a file with the name {string}")
  public void there_should_be_a_file_with_the_name(String filename) {
    saveGameFile = new File(filename);
    assertThat(saveGameFile).isFile();
  }

  @Then("it should be empty")
  public void it_should_be_empty() {
    assertThat(readFromFile(saveGameFile)).isEmpty();
  }

  @Given("a Game with {int} turns")
  public void a_game_with_turns(Integer int1) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("it should contain the turns in shorthand")
  public void it_should_contain_the_turns_in_shorthand() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Given("a finished Game")
  public void a_finished_game() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
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
