package de.fhkiel.ki.cathedral.game;

import static org.assertj.core.api.Assertions.assertThat;

import de.fhkiel.ki.cathedral.game.Building;
import de.fhkiel.ki.cathedral.game.Direction;
import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Position;
import org.junit.jupiter.api.Test;

class PlacementTest {

  @Test
  void createRight() {
    final Position firstTestPosition = new Position(3, 7);
    final Direction firstTestDirection = Direction._90;
    final Building firstTestBuilding = Building.Black_Castle;

    Placement firstPlacementUnderTest = new Placement(firstTestPosition, firstTestDirection, firstTestBuilding);

    assertThat(firstPlacementUnderTest.building()).isEqualTo(firstTestBuilding);
    assertThat(firstPlacementUnderTest.position()).isEqualTo(firstTestPosition);
    assertThat(firstPlacementUnderTest.direction()).isEqualTo(firstTestDirection);

    Placement secondPlacementUnderTest = new Placement(firstTestPosition.x(), firstTestPosition.y(), firstTestDirection, firstTestBuilding);

    assertThat(secondPlacementUnderTest.building()).isEqualTo(firstTestBuilding);
    assertThat(secondPlacementUnderTest.position()).isEqualTo(firstTestPosition);
    assertThat(secondPlacementUnderTest.direction()).isEqualTo(firstTestDirection);
  }

  @Test
  void createMisformedDirection() {
    final Position firstTestPosition = new Position(3, 7);
    final Direction firstTestDirection = Direction._270;
    final Building firstTestBuilding = Building.Black_Abbey;

    Placement firstPlacementUnderTest = new Placement(firstTestPosition, firstTestDirection, firstTestBuilding);

    assertThat(firstPlacementUnderTest.building()).isEqualTo(firstTestBuilding);
    assertThat(firstPlacementUnderTest.position()).isEqualTo(firstTestPosition);
    assertThat(firstPlacementUnderTest.direction()).isEqualTo(Direction._90);
  }

  @Test
  void getCoordinates() {
    final Position firstTestPosition = new Position(3, 7);

    Placement firstPlacementUnderTest = new Placement(firstTestPosition, Direction._90, Building.Black_Castle);

    assertThat(firstPlacementUnderTest.x()).isEqualTo(firstTestPosition.x());
    assertThat(firstPlacementUnderTest.y()).isEqualTo(firstTestPosition.y());

    Placement secondPlacementUnderTest = new Placement(firstTestPosition.x(), firstTestPosition.y(), Direction._90, Building.Black_Castle);

    assertThat(firstPlacementUnderTest.x()).isEqualTo(firstTestPosition.x());
    assertThat(firstPlacementUnderTest.y()).isEqualTo(firstTestPosition.y());
  }

  @Test
  void form() {
    final Direction firstTestDirection = Direction._90;
    final Building firstTestBuilding = Building.Black_Castle;

    Placement firstPlacementUnderTest = new Placement(new Position(3, 7), firstTestDirection, firstTestBuilding);

    assertThat(firstPlacementUnderTest.form())
        .hasSameSizeAs(firstTestBuilding.turn(firstTestDirection))
        .containsAll(firstTestBuilding.turn(firstTestDirection));

    final Direction secondTestDirection = Direction._0;
    final Building secondTestBuilding = Building.White_Infirmary;

    Placement secondPlacementUnderTest = new Placement(new Position(2, 1), secondTestDirection, secondTestBuilding);

    assertThat(secondPlacementUnderTest.form())
        .hasSameSizeAs(secondTestBuilding.turn(secondTestDirection))
        .containsAll(secondTestBuilding.turn(secondTestDirection));

    final Direction thirdTestDirection = Direction._180;
    final Building thirdTestBuilding = Building.Black_Bridge;

    Placement thirdPlacementUnderTest = new Placement(new Position(4, 9), thirdTestDirection, thirdTestBuilding);

    assertThat(thirdPlacementUnderTest.form())
        .hasSameSizeAs(thirdTestBuilding.turn(thirdTestDirection))
        .containsAll(thirdTestBuilding.turn(thirdTestDirection));
  }
}