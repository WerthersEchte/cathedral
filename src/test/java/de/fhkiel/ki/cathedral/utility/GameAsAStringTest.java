package de.fhkiel.ki.cathedral.utility;

import static de.fhkiel.ki.cathedral.utility.GameAsAString.gameToString;
import static de.fhkiel.ki.cathedral.utility.GameAsAString.placementToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import de.fhkiel.ki.cathedral.game.Building;
import de.fhkiel.ki.cathedral.game.Direction;
import de.fhkiel.ki.cathedral.game.Game;
import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.gui.CathedralGUI;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GameAsAStringTest {

  private static Stream<Arguments> placementsForTest() {
    return Stream.of(
        Arguments.of(new Placement(5, 5, Direction._90, Building.Blue_Cathedral ), "Cathedral 90 5 5"),
        Arguments.of(new Placement(-5, 5, Direction._0, Building.Black_Tavern ), "Black_Tavern 0 -5 5"),
        Arguments.of(new Placement(5, 555, Direction._180, Building.Black_Manor ), "Black_Manor 180 5 555"),
        Arguments.of(new Placement(0, 0, Direction._270, Building.White_Academy ), "White_Academy 270 0 0")
    );
  }

  @ParameterizedTest
  @MethodSource("placementsForTest")
  void givenAPlacementAndAString_whenConvertingThePlacementToString_thenItShouldBeEqualToTheGivenString(final Placement givenPlacement, final String givenPlacementString) {
    final String createdString = placementToString(givenPlacement);

    assertThat(createdString).isEqualTo(givenPlacementString);
  }

  @Test
  void givenAnEmptyGame_whenConvertingItToString_TheStringShouldBeEmpty() {
    final Game givenGame = new Game();

    final String createdString = gameToString(givenGame);

    assertThat(createdString).isEmpty();
  }

  @Test
  void givenAGame_whenConvertingItToString_TheStringShouldContainAllTurns() {
    final Game givenGame = new Game();
    givenGame.takeTurn(new Placement(7, 3, Direction._90, Building.Blue_Cathedral ));
    givenGame.takeTurn(new Placement(2, 4, Direction._0, Building.Black_Manor ));
    givenGame.takeTurn(new Placement(7, 7, Direction._270, Building.White_Academy ));
    givenGame.passTurn();
    givenGame.passTurn();
    givenGame.takeTurn(new Placement(9, 9, Direction._0, Building.Black_Tavern ));

    final String expected = "Cathedral 90 7 3\n" +
        "Black_Manor 0 2 4\n" +
        "White_Academy 270 7 7\n" +
        "Pass\n" +
        "Pass\n" +
        "Black_Tavern 0 9 9\n";
    final String createdString = gameToString(givenGame);

    assertThat(createdString).isNotEmpty().isEqualTo(expected);
  }
}