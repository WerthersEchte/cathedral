package de.fhkiel.ki.cathedral.game;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameTest {

  @Mock
  Board boardMock;

  @BeforeEach
  void resetMocks(){
    reset(boardMock);
  }

  @Test
  void construction() {
    Game gameUnderTest = new Game(boardMock);

    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isZero();
    assertThat(gameUnderTest.lastTurn().hasAction()).isFalse();
    assertThat(gameUnderTest.lastTurn().getAction()).isNull();
    assertThat(gameUnderTest.lastTurn().getBoard()).isEqualTo(boardMock);

    gameUnderTest = new Game();
    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isZero();
    assertThat(gameUnderTest.lastTurn().hasAction()).isFalse();
    assertThat(gameUnderTest.lastTurn().getAction()).isNull();
    assertThat(gameUnderTest.lastTurn().getBoard()).isEqualTo(new Board());
  }

  @Test
  void copy() {
    when(boardMock.copy()).thenReturn(boardMock);
    when(boardMock.placeBuilding(any(Placement.class), anyBoolean())).thenReturn(true);
    Game gameUnderTest = new Game(boardMock);
    gameUnderTest.ignoreRules(true);

    Game gameCopyUnderTest = gameUnderTest.copy();
    assertThat(gameCopyUnderTest).isEqualTo(gameUnderTest);

    gameUnderTest.passTurn();
    gameUnderTest.passTurn();
    gameUnderTest.takeTurn(new Placement(5,5, Direction._0, Building.White_Academy));
    gameUnderTest.takeTurn(new Placement(5,5, Direction._0, Building.Blue_Cathedral));
    gameUnderTest.takeTurn(new Placement(5,5, Direction._0, Building.White_Academy));
    gameUnderTest.takeTurn(new Placement(5,5, Direction._0, Building.White_Academy));
    gameUnderTest.takeTurn(new Placement(5,5, Direction._0, Building.White_Academy));
    gameUnderTest.takeTurn(new Placement(5,5, Direction._0, Building.White_Academy));
    gameUnderTest.takeTurn(new Placement(5,5, Direction._0, Building.Black_Inn));
    gameUnderTest.takeTurn(new Placement(5,5, Direction._0, Building.White_Academy));
    gameUnderTest.takeTurn(new Placement(5,5, Direction._0, Building.White_Academy));
    gameUnderTest.takeTurn(new Placement(5,5, Direction._0, Building.White_Academy));
    gameUnderTest.takeTurn(new Placement(5,5, Direction._0, Building.White_Academy));

    gameCopyUnderTest = gameUnderTest.copy();
    assertThat(gameCopyUnderTest).isEqualTo(gameUnderTest);
  }

  @Test
  void takeTurn() {
    when(boardMock.copy()).thenReturn(boardMock);
    when(boardMock.placeBuilding(any(Placement.class), anyBoolean())).thenReturn(true);
    Game gameUnderTest = new Game(boardMock);

    Turn lastTurn = gameUnderTest.lastTurn();
    // only cathedral at first
    Placement testPlacement = new Placement(5,5, Direction._0, Building.White_Academy);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isFalse();
    assertThat(gameUnderTest.lastTurn()).isEqualTo(lastTurn);

    testPlacement = new Placement(5,5, Direction._0, Building.Black_Abbey);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isFalse();
    assertThat(gameUnderTest.lastTurn()).isEqualTo(lastTurn);

    testPlacement = new Placement(5,5, Direction._0, Building.Blue_Cathedral);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isTrue();
    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(lastTurn.getTurnNumber()+1);
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);

    lastTurn = gameUnderTest.lastTurn();
    // only black second
    testPlacement = new Placement(5,5, Direction._0, Building.White_Academy);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isFalse();
    assertThat(gameUnderTest.lastTurn()).isEqualTo(lastTurn);

    testPlacement = new Placement(5,5, Direction._0, Building.Blue_Cathedral);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isFalse();
    assertThat(gameUnderTest.lastTurn()).isEqualTo(lastTurn);

    testPlacement = new Placement(5,5, Direction._0, Building.Black_Abbey);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isTrue();
    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(lastTurn.getTurnNumber()+1);
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);

    lastTurn = gameUnderTest.lastTurn();
    // only white third
    testPlacement = new Placement(5,5, Direction._0, Building.Black_Abbey);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isFalse();
    assertThat(gameUnderTest.lastTurn()).isEqualTo(lastTurn);

    testPlacement = new Placement(5,5, Direction._0, Building.Blue_Cathedral);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isFalse();
    assertThat(gameUnderTest.lastTurn()).isEqualTo(lastTurn);

    testPlacement = new Placement(5,5, Direction._0, Building.White_Academy);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isTrue();
    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(lastTurn.getTurnNumber()+1);
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);

    lastTurn = gameUnderTest.lastTurn();
    // only black 3ed
    testPlacement = new Placement(5,5, Direction._0, Building.White_Academy);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isFalse();
    assertThat(gameUnderTest.lastTurn()).isEqualTo(lastTurn);

    testPlacement = new Placement(5,5, Direction._0, Building.Blue_Cathedral);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isFalse();
    assertThat(gameUnderTest.lastTurn()).isEqualTo(lastTurn);

    testPlacement = new Placement(5,5, Direction._0, Building.Black_Abbey);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isTrue();
    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(lastTurn.getTurnNumber()+1);
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);

    lastTurn = gameUnderTest.lastTurn();
    // only white 4th
    testPlacement = new Placement(5,5, Direction._0, Building.Black_Abbey);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isFalse();
    assertThat(gameUnderTest.lastTurn()).isEqualTo(lastTurn);

    testPlacement = new Placement(5,5, Direction._0, Building.Blue_Cathedral);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isFalse();
    assertThat(gameUnderTest.lastTurn()).isEqualTo(lastTurn);

    testPlacement = new Placement(5,5, Direction._0, Building.White_Academy);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isTrue();
    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(lastTurn.getTurnNumber()+1);
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);
  }

  @Test
  void takeTurnWithoutRules() {
    when(boardMock.copy()).thenReturn(boardMock);
    when(boardMock.placeBuilding(any(Placement.class), anyBoolean())).thenReturn(true);
    Game gameUnderTest = new Game(boardMock);
    gameUnderTest.ignoreRules(true);

    Placement testPlacement = new Placement(5,5, Direction._0, Building.Blue_Cathedral);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isTrue();
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);

    testPlacement = new Placement(5,5, Direction._0, Building.Blue_Cathedral);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isTrue();
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);

    testPlacement = new Placement(5,5, Direction._0, Building.Black_Abbey);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isTrue();
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);

    testPlacement = new Placement(5,5, Direction._0, Building.Black_Abbey);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isTrue();
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);

    testPlacement = new Placement(5,5, Direction._0, Building.Black_Abbey);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isTrue();
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);

    testPlacement = new Placement(5,5, Direction._0, Building.White_Academy);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isTrue();
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);

    testPlacement = new Placement(5,5, Direction._0, Building.White_Academy);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isTrue();
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);

    testPlacement = new Placement(5,5, Direction._0, Building.White_Academy);
    assertThat(gameUnderTest.takeTurn(testPlacement)).isTrue();
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);
  }

  @Test
  void lastTurn() {
    when(boardMock.copy()).thenReturn(boardMock);
    when(boardMock.placeBuilding(any(Placement.class), anyBoolean())).thenReturn(true);
    Game gameUnderTest = new Game(boardMock);

    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isZero();
    assertThat(gameUnderTest.lastTurn().hasAction()).isFalse();
    assertThat(gameUnderTest.lastTurn().getAction()).isNull();
    assertThat(gameUnderTest.lastTurn().getBoard()).isEqualTo(boardMock);

    Placement testPlacement = new Placement(5,5, Direction._0, Building.Blue_Cathedral);
    gameUnderTest.takeTurn(testPlacement);

    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(1);
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);
    assertThat(gameUnderTest.lastTurn().getBoard()).isEqualTo(boardMock);

    gameUnderTest.passTurn();

    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(2);
    assertThat(gameUnderTest.lastTurn().hasAction()).isFalse();
    assertThat(gameUnderTest.lastTurn().getAction()).isNull();
    assertThat(gameUnderTest.lastTurn().getBoard()).isEqualTo(boardMock);

  }

  @Test
  void undoLastTurn() {
    Game gameUnderTest = new Game();
    gameUnderTest.passTurn();
    gameUnderTest.passTurn();

    Turn lastTurn = gameUnderTest.lastTurn();

    gameUnderTest.undoLastTurn();
    assertThat(gameUnderTest.lastTurn()).isNotEqualTo(lastTurn);
    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(lastTurn.getTurnNumber()-1);

    Turn turnBeforeLastTurn = gameUnderTest.lastTurn();

    gameUnderTest.undoLastTurn();
    assertThat(gameUnderTest.lastTurn()).isNotEqualTo(turnBeforeLastTurn);
    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(turnBeforeLastTurn.getTurnNumber()-1);

    Turn startTurn = gameUnderTest.lastTurn();

    gameUnderTest.undoLastTurn();
    assertThat(gameUnderTest.lastTurn()).isEqualTo(startTurn);
    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(startTurn.getTurnNumber());
    gameUnderTest.undoLastTurn();
    assertThat(gameUnderTest.lastTurn()).isEqualTo(startTurn);
    gameUnderTest.undoLastTurn();
    assertThat(gameUnderTest.lastTurn()).isEqualTo(startTurn);
  }

  @Test
  void forfeitTurn() {
    Game gameUnderTest = new Game();
    Color currentPlayer = gameUnderTest.getCurrentPlayer();
    Turn turn = gameUnderTest.lastTurn();

    gameUnderTest.passTurn();
    assertThat(gameUnderTest.lastTurn()).isNotEqualTo(turn);
    assertThat(gameUnderTest.getCurrentPlayer()).isNotEqualTo(currentPlayer);

    assertThat(gameUnderTest.lastTurn().getBoard()).isEqualTo(turn.getBoard());
    assertThat(gameUnderTest.lastTurn().hasAction()).isFalse();
  }

  @Test
  void getCurrentPlayer() {
    when(boardMock.copy()).thenReturn(boardMock);
    Game gameUnderTest = new Game(boardMock);

    assertThat(gameUnderTest.getCurrentPlayer()).isEqualTo(Color.Blue);

    gameUnderTest.passTurn();
    assertThat(gameUnderTest.getCurrentPlayer()).isEqualTo(Color.Black);

    gameUnderTest.passTurn();
    assertThat(gameUnderTest.getCurrentPlayer()).isEqualTo(Color.White);

    gameUnderTest.passTurn();
    assertThat(gameUnderTest.getCurrentPlayer()).isEqualTo(Color.Black);

    gameUnderTest.passTurn();
    assertThat(gameUnderTest.getCurrentPlayer()).isEqualTo(Color.White);

    gameUnderTest.undoLastTurn();
    assertThat(gameUnderTest.getCurrentPlayer()).isEqualTo(Color.Black);

    gameUnderTest.undoLastTurn();
    assertThat(gameUnderTest.getCurrentPlayer()).isEqualTo(Color.White);

    gameUnderTest.undoLastTurn();
    assertThat(gameUnderTest.getCurrentPlayer()).isEqualTo(Color.Black);

    gameUnderTest.undoLastTurn();
    assertThat(gameUnderTest.getCurrentPlayer()).isEqualTo(Color.Blue);

    gameUnderTest.undoLastTurn();
    assertThat(gameUnderTest.getCurrentPlayer()).isEqualTo(Color.Blue);

    gameUnderTest.undoLastTurn();
    assertThat(gameUnderTest.getCurrentPlayer()).isEqualTo(Color.Blue);
  }

  @Test
  void getPlacableBuildingsPlayer() {
    Game gameUnderTest = new Game(boardMock);

    gameUnderTest.getPlacableBuildings(Color.White);
    verify(boardMock, times(1)).getPlacableBuildings(Color.White);

    gameUnderTest.getPlacableBuildings(Color.Black);
    verify(boardMock, times(1)).getPlacableBuildings(Color.Black);
  }

  @Test
  void getPlacableBuildings() {
    when(boardMock.copy()).thenReturn(boardMock);
    Game gameUnderTest = new Game(boardMock);

    gameUnderTest.getPlacableBuildings();
    verify(boardMock, times(1)).getPlacableBuildings(Color.Blue);

    gameUnderTest.passTurn();
    gameUnderTest.getPlacableBuildings();
    verify(boardMock, times(1)).getPlacableBuildings(Color.Black);

    gameUnderTest.passTurn();
    gameUnderTest.getPlacableBuildings();
    verify(boardMock, times(1)).getPlacableBuildings(Color.White);

    gameUnderTest.passTurn();
    gameUnderTest.getPlacableBuildings();
    verify(boardMock, times(2)).getPlacableBuildings(Color.Black);
  }

  @Test
  void getPlacableBuildingsIgnoreRules() {
    when(boardMock.copy()).thenReturn(boardMock);
    Game gameUnderTest = new Game(boardMock);
    gameUnderTest.ignoreRules(true);

    gameUnderTest.getPlacableBuildings();
    verify(boardMock, times(1)).getAllUnplacedBuildings();

    gameUnderTest.passTurn();
    gameUnderTest.getPlacableBuildings();
    verify(boardMock, times(2)).getAllUnplacedBuildings();

    gameUnderTest.passTurn();
    gameUnderTest.getPlacableBuildings();
    verify(boardMock, times(3)).getAllUnplacedBuildings();

    gameUnderTest.passTurn();
    gameUnderTest.getPlacableBuildings();
    verify(boardMock, times(4)).getAllUnplacedBuildings();
  }

  @Test
  void unplacedBuildings() {
    Game gameUnderTest = new Game(boardMock);

    gameUnderTest.getAllUnplacedBuildings();
    verify(boardMock, times(1)).getAllUnplacedBuildings();
  }

  @Test
  void getBoard() {
    Board board = new Board();

    Game gameUnderTest = new Game(board);
    assertThat(gameUnderTest.getBoard()).isSameAs(board);
    gameUnderTest.passTurn();
    assertThat(gameUnderTest.getBoard()).isNotSameAs(board);
  }

  @Test
  void score() {
    Map<Color, Integer> scoreMap = new EnumMap<>(Color.class);
    when(boardMock.score()).thenReturn(scoreMap);

    Game gameUnderTest = new Game(boardMock);
    assertThat(gameUnderTest.score()).isSameAs(scoreMap);
  }
}