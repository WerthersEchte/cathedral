package de.fhkiel.ki.cathedral.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import de.fhkiel.ki.cathedral.game.Board;
import de.fhkiel.ki.cathedral.game.Building;
import de.fhkiel.ki.cathedral.game.Color;
import de.fhkiel.ki.cathedral.game.Direction;
import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Position;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class BoardTest {

  @Test
  void creation(){
    Board boardUnderTest = new Board();
    for(int y = 0; y <= 9; ++y){
      for(int x = 0; x <= 9; ++x){
        assertThat(boardUnderTest.getField()[y][x]).isEqualTo(Color.None);
      }
    }

    assertThat(boardUnderTest.getBuildings()).containsExactlyInAnyOrder(Building.values());
    assertThat(boardUnderTest.getAllUnplacedBuildings()).containsExactlyInAnyOrder(Building.values());
    assertThat(boardUnderTest.getPlacedBuildings()).isEmpty();

    boardUnderTest = new Board(Building.Black_Castle, Building.White_Academy, Building.Black_Abbey);
    for(int y = 0; y <= 9; ++y){
      for(int x = 0; x <= 9; ++x){
        assertThat(boardUnderTest.getField()[y][x]).isEqualTo(Color.None);
      }
    }

    assertThat(boardUnderTest.getBuildings()).containsExactlyInAnyOrder(Building.Black_Castle, Building.White_Academy, Building.Black_Abbey);
    assertThat(boardUnderTest.getAllUnplacedBuildings()).containsExactlyInAnyOrder(Building.Black_Castle, Building.White_Academy, Building.Black_Abbey);
    assertThat(boardUnderTest.getPlacedBuildings()).isEmpty();
  }


  @Test
  void copy() {
    Board boardUnderTest = new Board();
    assertThat(boardUnderTest).isEqualTo(boardUnderTest.copy());

    boardUnderTest.placeBuilding(new Placement(3,3, Direction._0, Building.Black_Academy), true);
    Board boardCopyUnderTest = boardUnderTest.copy();
    assertThat(boardUnderTest).isEqualTo(boardCopyUnderTest);

    boardUnderTest.placeBuilding(new Placement(0,0, Direction._0, Building.Black_Tavern), true);
    assertThat(boardUnderTest).isNotEqualTo(boardCopyUnderTest);
  }

  @Test
  void placeBuildingCorrectOnField() {
    Board boardUnderTest = new Board();
    for(int y = 0; y <= 9; ++y){
      for(int x = 0; x <= 9; ++x){
        assertThat(boardUnderTest.getField()[y][x]).isEqualTo(Color.None);
      }
    }

    boardUnderTest.placeBuilding(new Placement(4,4, Direction._0, Building.Blue_Cathedral), true);
    for(Position part: Building.Blue_Cathedral.turn(Direction._0)){
      assertThat(boardUnderTest.getField()[4 + part.y()][4 + part.x()]).isEqualTo(Color.Blue);
    }
    assertThat(boardUnderTest.getField()[0][0]).isEqualTo(Color.None);
    assertThat(boardUnderTest.getField()[9][9]).isEqualTo(Color.None);

    boardUnderTest.placeBuilding(new Placement(4,7, Direction._180, Building.Black_Castle), true);
    for(Position part: Building.Blue_Cathedral.turn(Direction._0)){
      assertThat(boardUnderTest.getField()[4 + part.y()][4 + part.x()]).isEqualTo(Color.Blue);
    }
    for(Position part: Building.Black_Castle.turn(Direction._180)){
      assertThat(boardUnderTest.getField()[7 + part.y()][4 + part.x()]).isEqualTo(Color.Black);
    }
    assertThat(boardUnderTest.getField()[0][0]).isEqualTo(Color.None);
    assertThat(boardUnderTest.getField()[9][9]).isEqualTo(Color.None);

    boardUnderTest.placeBuilding(new Placement(6,4, Direction._90, Building.White_Castle), true);
    for(Position part: Building.Blue_Cathedral.turn(Direction._0)){
      assertThat(boardUnderTest.getField()[4 + part.y()][4 + part.x()]).isEqualTo(Color.Blue);
    }
    for(Position part: Building.Black_Castle.turn(Direction._180)){
      assertThat(boardUnderTest.getField()[7 + part.y()][4 + part.x()]).isEqualTo(Color.Black);
    }
    for(Position part: Building.White_Castle.turn(Direction._90)){
      assertThat(boardUnderTest.getField()[4 + part.y()][6 + part.x()]).isEqualTo(Color.White);
    }
    assertThat(boardUnderTest.getField()[0][0]).isEqualTo(Color.None);
    assertThat(boardUnderTest.getField()[9][9]).isEqualTo(Color.None);

    boardUnderTest.placeBuilding(new Placement(7,7, Direction._0, Building.Black_Square), true);
    for(Position part: Building.Blue_Cathedral.turn(Direction._0)){
      assertThat(boardUnderTest.getField()[4 + part.y()][4 + part.x()]).isEqualTo(Color.Blue);
    }
    for(Position part: Building.Black_Castle.turn(Direction._180)){
      assertThat(boardUnderTest.getField()[7 + part.y()][4 + part.x()]).isEqualTo(Color.Black);
    }
    for(Position part: Building.White_Castle.turn(Direction._90)){
      assertThat(boardUnderTest.getField()[4 + part.y()][6 + part.x()]).isEqualTo(Color.White);
    }
    for(Position part: Building.Black_Square.turn(Direction._0)){
      assertThat(boardUnderTest.getField()[7 + part.y()][7 + part.x()]).isEqualTo(Color.Black);
    }
    assertThat(boardUnderTest.getField()[0][0]).isEqualTo(Color.None);
    assertThat(boardUnderTest.getField()[9][9]).isEqualTo(Color.None);
  }

  @Test
  void placeBuildingBlocksOther(){
    Board boardUnderTest = new Board();
    assertThat(boardUnderTest.placeBuilding(new Placement(4,4, Direction._0, Building.Blue_Cathedral), true)).isTrue();
    for(Position part: Building.Blue_Cathedral.turn(Direction._0)){
      assertThat(boardUnderTest.getField()[4 + part.y()][4 + part.x()]).isEqualTo(Color.Blue);
    }

    assertThat(boardUnderTest.placeBuilding(new Placement(4,6, Direction._180, Building.Black_Castle), true)).isFalse();
    for(Position part: Building.Blue_Cathedral.turn(Direction._0)){
      assertThat(boardUnderTest.getField()[4 + part.y()][4 + part.x()]).isEqualTo(Color.Blue);
    }
    for(Position part: Building.Black_Castle.turn(Direction._180)){
      assertThat(boardUnderTest.getField()[6 + part.y()][4 + part.x()]).isNotEqualTo(Color.Black);
    }

    assertThat(boardUnderTest.placeBuilding(new Placement(3,3, Direction._0, Building.White_Abbey), true)).isFalse();
    for(Position part: Building.Blue_Cathedral.turn(Direction._0)){
      assertThat(boardUnderTest.getField()[4 + part.y()][4 + part.x()]).isEqualTo(Color.Blue);
    }
    for(Position part: Building.Black_Castle.turn(Direction._0)){
      assertThat(boardUnderTest.getField()[3 + part.y()][3 + part.x()]).isNotEqualTo(Color.White);
    }
  }

  @Test
  void placeBuildingAvailability(){
    Board boardUnderTest = new Board();
    assertThat(boardUnderTest.placeBuilding(new Placement(4,4, Direction._0, Building.Blue_Cathedral), true)).isTrue();
    assertThat(boardUnderTest.placeBuilding(new Placement(7,7, Direction._0, Building.Blue_Cathedral), true)).isFalse();

    assertThat(boardUnderTest.placeBuilding(new Placement(0,7, Direction._0, Building.Black_Bridge), true)).isTrue();
    assertThat(boardUnderTest.placeBuilding(new Placement(1,7, Direction._0, Building.Black_Bridge), true)).isFalse();

    assertThat(boardUnderTest.placeBuilding(new Placement(0,0, Direction._0, Building.White_Tavern), true)).isTrue();
    assertThat(boardUnderTest.placeBuilding(new Placement(1,0, Direction._0, Building.White_Tavern), true)).isTrue();
    assertThat(boardUnderTest.placeBuilding(new Placement(2,0, Direction._0, Building.White_Tavern), true)).isFalse();
  }


  @Test
  void placeBuildingRegionFirstAfterThirdTurn(){
    Board boardUnderTest = new Board();
    for(int y = 0; y <= 9; ++y){
      for(int x = 0; x <= 9; ++x){
        assertThat(boardUnderTest.getField()[y][x]).isEqualTo(Color.None);
      }
    }

    boardUnderTest.placeBuilding(new Placement(1,1, Direction._90, Building.Black_Tower));
    for(int y = 0; y <= 9; ++y){
      for(int x = 0; x <= 9; ++x){
        assertThat(boardUnderTest.getField()[y][x]).isNotEqualTo(Color.Black_Owned);
      }
    }

    boardUnderTest.placeBuilding(new Placement(4,2, Direction._0, Building.Black_Bridge));
    for(int y = 0; y <= 9; ++y){
      for(int x = 0; x <= 9; ++x){
        assertThat(boardUnderTest.getField()[y][x]).isNotEqualTo(Color.Black_Owned);
      }
    }

    boardUnderTest.placeBuilding(new Placement(8,9, Direction._270, Building.White_Inn));
    for(int y = 0; y <= 9; ++y){
      for(int x = 0; x <= 9; ++x){
        assertThat(boardUnderTest.getField()[y][x]).isNotEqualTo(Color.Black_Owned);
        assertThat(boardUnderTest.getField()[y][x]).isNotEqualTo(Color.White_Owned);
      }
    }

    boardUnderTest.placeBuilding(new Placement(6,6, Direction._0, Building.White_Square));

    assertThat(boardUnderTest.getField()[0][0]).isEqualTo(Color.Black_Owned);
    assertThat(boardUnderTest.getField()[1][0]).isEqualTo(Color.Black_Owned);
    assertThat(boardUnderTest.getField()[0][1]).isEqualTo(Color.Black_Owned);

    assertThat(boardUnderTest.getField()[9][9]).isEqualTo(Color.White_Owned);

    assertThat(boardUnderTest.getField()[9][0]).isEqualTo(Color.None);
    assertThat(boardUnderTest.getField()[0][9]).isEqualTo(Color.None);

  }

  @Test
  void placeBuildingRegionPickUpBuilding(){
    Board boardUnderTest = new Board();
    boardUnderTest.placeBuilding(new Placement(9,9, Direction._0, Building.White_Tavern), true);
    boardUnderTest.placeBuilding(new Placement(8,9, Direction._0, Building.White_Tavern), true);

    boardUnderTest.placeBuilding(new Placement(2,1, Direction._90, Building.Black_Tower), true);
    boardUnderTest.placeBuilding(new Placement(0,1, Direction._0, Building.White_Bridge), true);
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.White_Bridge)).isZero();

    boardUnderTest.placeBuilding(new Placement(1,3, Direction._90, Building.Black_Inn));
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.White_Bridge)).isEqualTo(1);

    assertThat(boardUnderTest.getField()[0][0]).isEqualTo(Color.Black_Owned);
    assertThat(boardUnderTest.getField()[0][1]).isEqualTo(Color.Black_Owned);
    assertThat(boardUnderTest.getField()[0][2]).isEqualTo(Color.Black_Owned);
    assertThat(boardUnderTest.getField()[1][0]).isEqualTo(Color.Black_Owned);
    assertThat(boardUnderTest.getField()[1][1]).isEqualTo(Color.Black_Owned);
    assertThat(boardUnderTest.getField()[2][0]).isEqualTo(Color.Black_Owned);
    assertThat(boardUnderTest.getField()[3][0]).isEqualTo(Color.Black_Owned);

    assertThat(boardUnderTest.getField()[9][0]).isEqualTo(Color.None);
    assertThat(boardUnderTest.getField()[0][9]).isEqualTo(Color.None);

  }

  @Test
  void placeBuildingRegionPickUpAll(){
    Board boardUnderTest = new Board();
    boardUnderTest.placeBuilding(new Placement(9,9, Direction._0, Building.White_Tavern), true);

    boardUnderTest.placeBuilding(new Placement(2,1, Direction._90, Building.Black_Tower), true);
    boardUnderTest.placeBuilding(new Placement(0,1, Direction._0, Building.White_Bridge), true);
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.White_Bridge)).isZero();

    boardUnderTest.placeBuilding(new Placement(1,3, Direction._90, Building.Black_Inn));
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.White_Bridge)).isEqualTo(1);
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.White_Tavern)).isEqualTo(2);

    for(int y = 0; y <= 9; ++y){
      for(int x = 0; x <= 9; ++x){
        assertThat(boardUnderTest.getField()[y][x]).isNotEqualTo(Color.None);
        assertThat(boardUnderTest.getField()[y][x]).isNotEqualTo(Color.White);
      }
    }

  }

  @Test
  void placeBuildingOnRegion(){
    Board boardUnderTest = new Board();
    boardUnderTest.placeBuilding(new Placement(9,9, Direction._0, Building.White_Tavern), true);

    boardUnderTest.placeBuilding(new Placement(2,1, Direction._90, Building.Black_Tower), true);
    boardUnderTest.placeBuilding(new Placement(0,1, Direction._0, Building.White_Bridge), true);
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.White_Bridge)).isZero();

    boardUnderTest.placeBuilding(new Placement(1,3, Direction._90, Building.Black_Inn));

    assertThat(boardUnderTest.placeBuilding(new Placement(9,9, Direction._0, Building.White_Tavern))).isFalse();
    assertThat(boardUnderTest.placeBuilding(new Placement(9,9, Direction._0, Building.Black_Tavern))).isTrue();
  }

  @Test
  void placeBuildingOnBorder(){
    Board boardUnderTest = new Board();

    Position first = new Position(0, 5);
    assertThat(boardUnderTest.placeBuilding(new Placement(first, Direction._0, Building.Black_Tower))).isFalse();
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.Black_Tower)).isEqualTo(1);
    for(Position toCheck : Building.Black_Tower.turn(Direction._0).stream().map(position -> position.plus(first)).toList()){
      if(toCheck.isViable()){
        assertThat(boardUnderTest.getField()[toCheck.y()][toCheck.x()]).isEqualTo(Color.None);
      }
    }

    Position second = new Position(5, 9);
    assertThat(boardUnderTest.placeBuilding(new Placement(second, Direction._0, Building.White_Academy))).isFalse();
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.White_Academy)).isEqualTo(1);
    for(Position toCheck : Building.White_Academy.turn(Direction._0).stream().map(position -> position.plus(second)).toList()){
      if(toCheck.isViable()){
        assertThat(boardUnderTest.getField()[toCheck.y()][toCheck.x()]).isEqualTo(Color.None);
      }
    }
  }

  @Test
  void placeBuildingOutsideField(){
    Board boardUnderTest = new Board();

    Position first = new Position(-7, 5);
    assertThat(boardUnderTest.placeBuilding(new Placement(first, Direction._0, Building.Black_Tower))).isFalse();
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.Black_Tower)).isEqualTo(1);

    Position second = new Position(5, 19);
    assertThat(boardUnderTest.placeBuilding(new Placement(second, Direction._0, Building.White_Academy))).isFalse();
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.White_Academy)).isEqualTo(1);
  }

  @Test
  void placeBuildingOutsideBoard(){
    Board boardUnderTest = new Board();

    assertThat(boardUnderTest.placeBuilding(new Placement(0,5, Direction._0, Building.Black_Tower))).isFalse();
    assertThat(boardUnderTest.placeBuilding(new Placement(5,9, Direction._0, Building.White_Academy))).isFalse();
  }

  @Test
  void score() {
    Board boardUnderTest = new Board();

    assertThat(boardUnderTest.score())
        .containsEntry(Color.Black, 47)
        .containsEntry(Color.White, 47);

    boardUnderTest.placeBuilding(new Placement(3,3, Direction._0, Building.Blue_Cathedral), true);
    assertThat(boardUnderTest.score())
        .containsEntry(Color.Black, 47)
        .containsEntry(Color.White, 47);

    boardUnderTest.placeBuilding(new Placement(5,5, Direction._0, Building.Black_Academy), true);
    assertThat(boardUnderTest.score())
        .containsEntry(Color.Black, 42)
        .containsEntry(Color.White, 47);

    boardUnderTest.placeBuilding(new Placement(7,7, Direction._0, Building.White_Abbey), true);
    assertThat(boardUnderTest.score())
        .containsEntry(Color.Black, 42)
        .containsEntry(Color.White, 43);

    boardUnderTest.placeBuilding(new Placement(0,0, Direction._0, Building.Black_Tavern), true);
    assertThat(boardUnderTest.score())
        .containsEntry(Color.Black, 41)
        .containsEntry(Color.White, 43);

  }

  @Test
  void numberOfFreeBuildings() {
    Board boardUnderTest = new Board();

    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.Blue_Cathedral))
        .isEqualTo(1);
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.Black_Academy))
        .isEqualTo(1);
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.White_Tavern))
        .isEqualTo(2);


    boardUnderTest.placeBuilding(new Placement(3,3, Direction._0, Building.Blue_Cathedral), true);
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.Blue_Cathedral))
        .isZero();
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.Black_Academy))
        .isEqualTo(1);
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.White_Tavern))
        .isEqualTo(2);

    boardUnderTest.placeBuilding(new Placement(5,5, Direction._0, Building.Black_Academy), true);
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.Blue_Cathedral))
        .isZero();
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.Black_Academy))
        .isZero();
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.White_Tavern))
        .isEqualTo(2);

    boardUnderTest.placeBuilding(new Placement(0,0, Direction._0, Building.White_Tavern), true);
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.Blue_Cathedral))
        .isZero();
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.Black_Academy))
        .isZero();
    assertThat(boardUnderTest.getNumberOfFreeBuildings(Building.White_Tavern))
        .isEqualTo(1);
  }

  @Test
  void getBuildings() {
    Board boardUnderTest = new Board();
    assertThat(boardUnderTest.getBuildings()).containsExactlyInAnyOrder(Building.values());
    boardUnderTest.placeBuilding(new Placement(3,3, Direction._0, Building.Blue_Cathedral), true);
    assertThat(boardUnderTest.getBuildings()).containsExactlyInAnyOrder(Building.values());

    boardUnderTest = new Board(Building.Black_Castle, Building.White_Academy, Building.Black_Abbey);
    assertThat(boardUnderTest.getBuildings()).containsExactlyInAnyOrder(Building.Black_Castle, Building.White_Academy, Building.Black_Abbey);
  }

  @Test
  void placableBuildings() {
    Board boardUnderTest = new Board();

    assertThat(boardUnderTest.getPlacableBuildings(Color.Blue))
        .containsExactlyInAnyOrder(Arrays.stream(Building.values()).filter(building -> building.getColor() == Color.Blue).toList().toArray(new Building[0]));
    assertThat(boardUnderTest.getPlacableBuildings(Color.Black))
        .containsExactlyInAnyOrder(Arrays.stream(Building.values()).filter(building -> building.getColor() == Color.Black).toList().toArray(new Building[0]));
    assertThat(boardUnderTest.getPlacableBuildings(Color.White))
        .containsExactlyInAnyOrder(Arrays.stream(Building.values()).filter(building -> building.getColor() == Color.White).toList().toArray(new Building[0]));


    boardUnderTest.placeBuilding(new Placement(3,3, Direction._0, Building.Blue_Cathedral), true);
    assertThat(boardUnderTest.getPlacableBuildings(Color.Blue))
        .doesNotContain(Building.Blue_Cathedral);
    assertThat(boardUnderTest.getPlacableBuildings(Color.Black))
        .containsExactlyInAnyOrder(Arrays.stream(Building.values()).filter(building -> building.getColor() == Color.Black).toList().toArray(new Building[0]));
    assertThat(boardUnderTest.getPlacableBuildings(Color.White))
        .containsExactlyInAnyOrder(Arrays.stream(Building.values()).filter(building -> building.getColor() == Color.White).toList().toArray(new Building[0]));

    boardUnderTest.placeBuilding(new Placement(5,5, Direction._0, Building.Black_Academy), true);
    assertThat(boardUnderTest.getPlacableBuildings(Color.Blue))
        .doesNotContain(Building.Blue_Cathedral);
    assertThat(boardUnderTest.getPlacableBuildings(Color.Black))
        .doesNotContain(Building.Black_Academy);
    assertThat(boardUnderTest.getPlacableBuildings(Color.White))
        .containsExactlyInAnyOrder(Arrays.stream(Building.values()).filter(building -> building.getColor() == Color.White).toList().toArray(new Building[0]));

    boardUnderTest.placeBuilding(new Placement(0,0, Direction._0, Building.White_Tavern), true);
    assertThat(boardUnderTest.getPlacableBuildings(Color.Blue))
        .doesNotContain(Building.Blue_Cathedral);
    assertThat(boardUnderTest.getPlacableBuildings(Color.Black))
        .doesNotContain(Building.Black_Academy);
    assertThat(boardUnderTest.getPlacableBuildings(Color.White))
        .containsExactlyInAnyOrder(Arrays.stream(Building.values()).filter(building -> building.getColor() == Color.White).toList().toArray(new Building[0]));

    boardUnderTest.placeBuilding(new Placement(0,1, Direction._0, Building.White_Tavern), true);
    assertThat(boardUnderTest.getPlacableBuildings(Color.Blue))
        .doesNotContain(Building.Blue_Cathedral);
    assertThat(boardUnderTest.getPlacableBuildings(Color.Black))
        .doesNotContain(Building.Black_Academy);
    assertThat(boardUnderTest.getPlacableBuildings(Color.White))
        .doesNotContain(Building.White_Tavern);
  }

  @Test
  void allUnplacedBuildings() {

    Board boardUnderTest = new Board();

    assertThat(boardUnderTest.getAllUnplacedBuildings())
        .containsExactlyInAnyOrder(Building.values());


    boardUnderTest.placeBuilding(new Placement(3,3, Direction._0, Building.Blue_Cathedral), true);
    assertThat(boardUnderTest.getAllUnplacedBuildings())
        .doesNotContain(Building.Blue_Cathedral);

    boardUnderTest.placeBuilding(new Placement(5,5, Direction._0, Building.Black_Academy), true);
    assertThat(boardUnderTest.getAllUnplacedBuildings())
        .doesNotContain(Building.Blue_Cathedral, Building.Black_Academy);

    boardUnderTest.placeBuilding(new Placement(0,0, Direction._0, Building.White_Tavern), true);
    assertThat(boardUnderTest.getAllUnplacedBuildings())
        .doesNotContain(Building.Blue_Cathedral, Building.Black_Academy)
        .contains(Building.White_Tavern);

    boardUnderTest.placeBuilding(new Placement(0,1, Direction._0, Building.White_Tavern), true);
    assertThat(boardUnderTest.getAllUnplacedBuildings())
        .doesNotContain(Building.Blue_Cathedral, Building.Black_Academy, Building.White_Tavern);
  }
}