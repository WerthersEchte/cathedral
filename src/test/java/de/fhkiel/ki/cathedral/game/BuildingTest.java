package de.fhkiel.ki.cathedral.game;

import static org.assertj.core.api.Assertions.assertThat;

import de.fhkiel.ki.cathedral.game.Board;
import de.fhkiel.ki.cathedral.game.Building;
import de.fhkiel.ki.cathedral.game.Direction;
import de.fhkiel.ki.cathedral.game.Game;
import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Position;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class BuildingTest {

  private List<Position> generateListFromValues(int[][] values){
    return Arrays
        .stream(values)
        .map(ints -> new Position(ints[0], ints[1]))
        .collect(Collectors.toList());
  }

  @Test
  void turn() {
    final List<Position> formOfTavern = generateListFromValues(new int[][] {{0, 0}});
    assertThat(Building.Black_Tavern.turn(Direction._0))
        .containsAll(Building.Black_Tavern.turn(Direction._90))
        .containsAll(Building.Black_Tavern.turn(Direction._180))
        .containsAll(Building.Black_Tavern.turn(Direction._270))
        .containsAll(formOfTavern);

    final List<Position> formOfBridge = generateListFromValues(new int[][] {{0, -1}, {0, 0}, {0, 1}});
    assertThat(Building.White_Bridge.turn(Direction._0))
        .containsAll(Building.White_Bridge.turn(Direction._180))
        .containsAll(formOfBridge);

    final List<Position> formOfBridge_90 = generateListFromValues(new int[][] {{-1, 0}, {0, 0}, {1, 0}});
    assertThat(Building.White_Bridge.turn(Direction._90))
        .containsAll(Building.White_Bridge.turn(Direction._270))
        .containsAll(formOfBridge_90);

    final List<Position> formOfCastle = generateListFromValues(new int[][] {{-1, 0}, {-1, 1}, {0, 0}, {1, 0}, {1, 1}});
    assertThat(Building.Black_Castle.turn(Direction._0))
        .containsAll(formOfCastle);

    final List<Position> formOfCastle_90 = generateListFromValues(new int[][] {{-1, -1}, {0, -1}, {0, 0}, {-1, 1}, {0, 1}});
    assertThat(Building.Black_Castle.turn(Direction._90))
        .containsAll(formOfCastle_90);

    final List<Position> formOfCastle_180 = generateListFromValues(new int[][] {{-1, -1}, {-1, 0}, {0, 0}, {1, 0}, {1, -1}});
    assertThat(Building.Black_Castle.turn(Direction._180))
        .containsAll(formOfCastle_180);

    final List<Position> formOfCastle_270 = generateListFromValues(new int[][] {{1, -1}, {0, -1}, {0, 0}, {1, 1}, {0, 1}});
    assertThat(Building.Black_Castle.turn(Direction._270))
        .containsAll(formOfCastle_270);
  }

  @Test
  void corners() {
    final List<Position> cornersOfTavern = generateListFromValues(new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}});
    assertThat(Building.Black_Tavern.corners(Direction._0))
        .doesNotContain(Building.Black_Tavern.turn(Direction._0).toArray(Position[]::new))
        .containsAll(Building.Black_Tavern.corners(Direction._90))
        .containsAll(Building.Black_Tavern.corners(Direction._180))
        .containsAll(Building.Black_Tavern.corners(Direction._270))
        .containsAll(cornersOfTavern)
        .hasSameSizeAs(cornersOfTavern);

    final List<Position> cornersOfBridge = generateListFromValues(new int[][] {{-1, -1}, {1, 0}, {1, -1}, {0, -2}, {0, 2}, {-1, 1}, {-1, 0}, {1, 1}});
    assertThat(Building.White_Bridge.corners(Direction._0))
        .doesNotContain(Building.White_Bridge.turn(Direction._0).toArray(Position[]::new))
        .containsAll(Building.White_Bridge.corners(Direction._180))
        .containsAll(cornersOfBridge)
        .hasSameSizeAs(cornersOfBridge);

    final List<Position> cornersOfBridge_90 = generateListFromValues(new int[][] {{-1, 1}, {1, -1}, {0, 1}, {1, 1}, {2, 0}, {-2, 0}, {-1, -1}, {0, -1}});
    assertThat(Building.White_Bridge.corners(Direction._90))
        .doesNotContain(Building.White_Bridge.turn(Direction._90).toArray(Position[]::new))
        .containsAll(Building.White_Bridge.corners(Direction._270))
        .containsAll(cornersOfBridge_90)
        .hasSameSizeAs(cornersOfBridge_90);

    final List<Position> cornersOfCastle = generateListFromValues(new int[][] {{2, 1}, {2, 0}, {0, 1}, {-2, 1}, {0, -1}, {-2, 0}, {1, 2}, {-1, 2}, {1, -1}, {-1, -1}});
    assertThat(Building.Black_Castle.corners(Direction._0))
        .doesNotContain(Building.Black_Castle.turn(Direction._0).toArray(Position[]::new))
        .containsAll(cornersOfCastle)
        .hasSameSizeAs(cornersOfCastle);

    final List<Position> cornersOfCastle_90 = generateListFromValues(new int[][] {{-1, 2}, {0, 2}, {-1, 0}, {-1, -2}, {1, 0}, {0, -2}, {-2, 1}, {-2, -1}, {1, 1}, {1, -1}});
    assertThat(Building.Black_Castle.corners(Direction._90))
        .doesNotContain(Building.Black_Castle.turn(Direction._90).toArray(Position[]::new))
        .containsAll(cornersOfCastle_90)
        .hasSameSizeAs(cornersOfCastle_90);

    final List<Position> cornersOfCastle_180 = generateListFromValues(new int[][] {{-1, 1}, {1, -2}, {-1, -2}, {2, 0}, {0, 1}, {2, -1}, {0, -1}, {-2, 0}, {-2, -1}, {1, 1}});
    assertThat(Building.Black_Castle.corners(Direction._180))
        .doesNotContain(Building.Black_Castle.turn(Direction._180).toArray(Position[]::new))
        .containsAll(cornersOfCastle_180)
        .hasSameSizeAs(cornersOfCastle_180);

    final List<Position> cornersOfCastle_270 = generateListFromValues(new int[][] {{-1, -1}, {2, 1}, {2, -1}, {0, 2}, {-1, 0}, {1, 2}, {1, 0}, {0, -2}, {1, -2}, {-1, 1}});
    assertThat(Building.Black_Castle.corners(Direction._270))
        .doesNotContain(Building.Black_Castle.turn(Direction._270).toArray(Position[]::new))
        .containsAll(cornersOfCastle_270)
        .hasSameSizeAs(cornersOfCastle_270);
  }

  @Test
  void silhouette() {
    final List<Position> silhouetteOfTavern = generateListFromValues(new int[][] {{-1, -1}, {1, 1}, {0, -1}, {1, 0}, {1, -1}, {-1, 1}, {0, 1}, {-1, 0}});
    assertThat(Building.Black_Tavern.silhouette(Direction._0))
        .doesNotContain(Building.Black_Tavern.turn(Direction._0).toArray(Position[]::new))
        .containsAll(Building.Black_Tavern.silhouette(Direction._90))
        .containsAll(Building.Black_Tavern.silhouette(Direction._180))
        .containsAll(Building.Black_Tavern.silhouette(Direction._270))
        .containsAll(silhouetteOfTavern)
        .hasSameSizeAs(silhouetteOfTavern);

    final List<Position> silhouetteOfBridge = generateListFromValues(new int[][] {{-1, -2}, {-1, -1}, {-1, 0}, {-1, 1}, {-1, 2}, {0, -2}, {0, 2}, {1, -2}, {1, -1}, {1, 0}, {1, 1}, {1, 2}});
    assertThat(Building.White_Bridge.silhouette(Direction._0))
        .doesNotContain(Building.White_Bridge.turn(Direction._0).toArray(Position[]::new))
        .containsAll(Building.White_Bridge.silhouette(Direction._180))
        .containsAll(silhouetteOfBridge)
        .hasSameSizeAs(silhouetteOfBridge);

    final List<Position> silhouetteOfBridge_90 = generateListFromValues(new int[][] {{-1, 1}, {0, 1}, {1, 1}, {2, 1}, {-2, 0}, {2, 0}, {-2, -1}, {-1, -1}, {0, -1}, {1, -1}, {2, -1}, {-2, 1}});
    assertThat(Building.White_Bridge.silhouette(Direction._90))
        .doesNotContain(Building.White_Bridge.turn(Direction._90).toArray(Position[]::new))
        .containsAll(Building.White_Bridge.silhouette(Direction._270))
        .containsAll(silhouetteOfBridge_90)
        .hasSameSizeAs(silhouetteOfBridge_90);

    final List<Position> silhouetteOfCastle = generateListFromValues(new int[][] {{-1, -1}, {-2, -1}, {2, 0}, {1, -1}, {2, -1}, {-2, 2}, {-1, 2}, {-2, 1}, {1, 2}, {2, 2}, {2, 1}, {0, 2}, {0, 1}, {-2, 0}, {0, -1}});
    assertThat(Building.Black_Castle.silhouette(Direction._0))
        .doesNotContain(Building.Black_Castle.turn(Direction._0).toArray(Position[]::new))
        .containsAll(silhouetteOfCastle)
        .hasSameSizeAs(silhouetteOfCastle);

    final List<Position> silhouetteOfCastle_90 = generateListFromValues(new int[][] {{0, 2}, {1, 1}, {1, 2}, {-2, -2}, {-2, -1}, {-1, -2}, {-2, 1}, {-2, 2}, {-1, 2}, {-2, 0}, {-1, 0}, {0, -2}, {1, 0}, {1, -1}, {1, -2}});
    assertThat(Building.Black_Castle.silhouette(Direction._90))
        .doesNotContain(Building.Black_Castle.turn(Direction._90).toArray(Position[]::new))
        .containsAll(silhouetteOfCastle_90)
        .hasSameSizeAs(silhouetteOfCastle_90);

    final List<Position> silhouetteOfCastle_180 = generateListFromValues(new int[][] {{2, 1}, {1, 1}, {0, 1}, {2, 0}, {0, -1}, {0, -2}, {-2, -1}, {-2, -2}, {-1, -2}, {2, -1}, {1, -2}, {2, -2}, {-2, 1}, {-1, 1}, {-2, 0}});
    assertThat(Building.Black_Castle.silhouette(Direction._180))
        .doesNotContain(Building.Black_Castle.turn(Direction._180).toArray(Position[]::new))
        .containsAll(silhouetteOfCastle_180)
        .hasSameSizeAs(silhouetteOfCastle_180);

    final List<Position> silhouetteOfCastle_270 = generateListFromValues(new int[][] {{-1, 0}, {-1, 1}, {-1, 2}, {0, -2}, {-1, -1}, {-1, -2}, {2, 2}, {2, 1}, {1, 2}, {2, -1}, {2, -2}, {1, -2}, {2, 0}, {1, 0}, {0, 2}});
    assertThat(Building.Black_Castle.silhouette(Direction._270))
        .doesNotContain(Building.Black_Castle.turn(Direction._270).toArray(Position[]::new))
        .containsAll(silhouetteOfCastle_270)
        .hasSameSizeAs(silhouetteOfCastle_270);
  }

  @Test
  void score() {
    assertThat(Building.Black_Castle.score()).isEqualTo(5);
    assertThat(Building.White_Inn.score()).isEqualTo(3);
    assertThat(Building.Black_Academy.score()).isEqualTo(5);
  }

  @Test
  void possiblePlacements(){
    assertThat(Building.Black_Tavern.getAllPossiblePlacements())
        .hasSize(100)
        .contains(new Placement(0,0, Direction._0, Building.Black_Tavern), new Placement(9,9, Direction._0, Building.Black_Tavern))
        .hasSameSizeAs(Building.White_Tavern.getAllPossiblePlacements());

    assertThat(Building.Black_Academy.getAllPossiblePlacements())
        .contains(
            new Placement(3,3, Direction._0, Building.Black_Academy),
            new Placement(3,3, Direction._90, Building.Black_Academy),
            new Placement(3,3, Direction._180, Building.Black_Academy),
            new Placement(3,3, Direction._270, Building.Black_Academy)
        );

    Board board = new Board();
    board.placeBuilding(new Placement(3,3, Direction._0, Building.Black_Academy), true);
    board.placeBuilding(new Placement(0,0, Direction._0, Building.Black_Tavern), true);

    assertThat(Building.Black_Academy.getPossiblePlacements(board)).isEmpty();
    assertThat(Building.White_Academy.getPossiblePlacements(board)).isNotEmpty();
    assertThat(Building.Black_Tavern.getPossiblePlacements(board)).isNotEmpty();

    Game game = new Game(board);

    assertThat(Building.Black_Academy.getPossiblePlacements(game)).isEmpty();
    assertThat(Building.White_Academy.getPossiblePlacements(game)).isNotEmpty();
    assertThat(Building.Black_Tavern.getPossiblePlacements(game)).isNotEmpty();
  }
}