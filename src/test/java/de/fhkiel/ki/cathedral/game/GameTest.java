package de.fhkiel.ki.cathedral.game;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.fhkiel.ki.cathedral.game.Board;
import de.fhkiel.ki.cathedral.game.Building;
import de.fhkiel.ki.cathedral.game.Color;
import de.fhkiel.ki.cathedral.game.Direction;
import de.fhkiel.ki.cathedral.game.Game;
import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Turn;
import java.util.HashMap;
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

    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(0);
    assertThat(gameUnderTest.lastTurn().hasAction()).isFalse();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(null);
    assertThat(gameUnderTest.lastTurn().getBoard()).isEqualTo(boardMock);

    gameUnderTest = new Game();
    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(0);
    assertThat(gameUnderTest.lastTurn().hasAction()).isFalse();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(null);
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

    gameUnderTest.forfeitTurn();
    gameUnderTest.forfeitTurn();
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

    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(0);
    assertThat(gameUnderTest.lastTurn().hasAction()).isFalse();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(null);
    assertThat(gameUnderTest.lastTurn().getBoard()).isEqualTo(boardMock);

    Placement testPlacement = new Placement(5,5, Direction._0, Building.Blue_Cathedral);
    gameUnderTest.takeTurn(testPlacement);

    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(1);
    assertThat(gameUnderTest.lastTurn().hasAction()).isTrue();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(testPlacement);
    assertThat(gameUnderTest.lastTurn().getBoard()).isEqualTo(boardMock);

    gameUnderTest.forfeitTurn();

    assertThat(gameUnderTest.lastTurn().getTurnNumber()).isEqualTo(2);
    assertThat(gameUnderTest.lastTurn().hasAction()).isFalse();
    assertThat(gameUnderTest.lastTurn().getAction()).isEqualTo(null);
    assertThat(gameUnderTest.lastTurn().getBoard()).isEqualTo(boardMock);

  }

  @Test
  void undoLastTurn() {
    Game gameUnderTest = new Game();
    gameUnderTest.forfeitTurn();
    gameUnderTest.forfeitTurn();

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

    gameUnderTest.forfeitTurn();
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

    gameUnderTest.forfeitTurn();
    assertThat(gameUnderTest.getCurrentPlayer()).isEqualTo(Color.Black);

    gameUnderTest.forfeitTurn();
    assertThat(gameUnderTest.getCurrentPlayer()).isEqualTo(Color.White);

    gameUnderTest.forfeitTurn();
    assertThat(gameUnderTest.getCurrentPlayer()).isEqualTo(Color.Black);

    gameUnderTest.forfeitTurn();
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

    gameUnderTest.forfeitTurn();
    gameUnderTest.getPlacableBuildings();
    verify(boardMock, times(1)).getPlacableBuildings(Color.Black);

    gameUnderTest.forfeitTurn();
    gameUnderTest.getPlacableBuildings();
    verify(boardMock, times(1)).getPlacableBuildings(Color.White);

    gameUnderTest.forfeitTurn();
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

    gameUnderTest.forfeitTurn();
    gameUnderTest.getPlacableBuildings();
    verify(boardMock, times(2)).getAllUnplacedBuildings();

    gameUnderTest.forfeitTurn();
    gameUnderTest.getPlacableBuildings();
    verify(boardMock, times(3)).getAllUnplacedBuildings();

    gameUnderTest.forfeitTurn();
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
    gameUnderTest.forfeitTurn();
    assertThat(gameUnderTest.getBoard()).isNotSameAs(board);
  }

  @Test
  void score() {
    Map<Color, Integer> scoreMap = new HashMap<>();
    when(boardMock.score()).thenReturn(scoreMap);

    Game gameUnderTest = new Game(boardMock);
    assertThat(gameUnderTest.score()).isSameAs(scoreMap);
  }
}