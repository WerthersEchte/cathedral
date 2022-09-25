package de.fhkiel.ki.cathedral.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.fhkiel.ki.cathedral.game.Board;
import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Turn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TurnTest {

  @Mock
  Board boardMock;
  @Mock
  Placement placementMock;

  @Test
  void constructorNormal() {
    final int firstTurnTestNumber = 3;

    Turn firstTurnUnderTest = new Turn(firstTurnTestNumber, boardMock, placementMock);

    assertThat(firstTurnUnderTest.getTurnNumber()).isEqualTo(firstTurnTestNumber);
    assertThat(firstTurnUnderTest.getBoard()).isEqualTo(boardMock);
    assertThat(firstTurnUnderTest.hasAction()).isTrue();
    assertThat(firstTurnUnderTest.getAction()).isEqualTo(placementMock);

    final int secondTurnTestNumber = 56;

    Turn secondTurnUnderTest = new Turn(secondTurnTestNumber, boardMock, placementMock);

    assertThat(secondTurnUnderTest.getTurnNumber()).isEqualTo(secondTurnTestNumber);
    assertThat(secondTurnUnderTest.getBoard()).isEqualTo(boardMock);
    assertThat(secondTurnUnderTest.hasAction()).isTrue();
    assertThat(secondTurnUnderTest.getAction()).isEqualTo(placementMock);
  }

  @Test
  void constructorNoAction() {
    final int firstTurnTestNumber = 7;

    Turn firstTurnUnderTest = new Turn(firstTurnTestNumber, boardMock);

    assertThat(firstTurnUnderTest.getTurnNumber()).isEqualTo(firstTurnTestNumber);
    assertThat(firstTurnUnderTest.getBoard()).isEqualTo(boardMock);
    assertThat(firstTurnUnderTest.hasAction()).isFalse();
    assertThat(firstTurnUnderTest.getAction()).isNull();

    final int secondTurnTestNumber = 1;

    Turn secondTurnUnderTest = new Turn(secondTurnTestNumber, boardMock);

    assertThat(secondTurnUnderTest.getTurnNumber()).isEqualTo(secondTurnTestNumber);
    assertThat(secondTurnUnderTest.getBoard()).isEqualTo(boardMock);
    assertThat(secondTurnUnderTest.hasAction()).isFalse();
    assertThat(secondTurnUnderTest.getAction()).isNull();
  }

  @Test
  void copy() {
    when(boardMock.copy()).thenReturn(boardMock);

    final int firstTurnTestNumber = 5;

    Turn firstTurnUnderTest = new Turn(firstTurnTestNumber, boardMock, placementMock);
    Turn firstCopyTurnUnderTest = firstTurnUnderTest.copy();

    assertThat(firstCopyTurnUnderTest)
        .isEqualTo(firstTurnUnderTest)
        .isNotSameAs(firstTurnUnderTest);
    assertThat(firstCopyTurnUnderTest.getTurnNumber()).isEqualTo(firstTurnUnderTest.getTurnNumber());
    assertThat(firstCopyTurnUnderTest.getBoard()).isEqualTo(firstTurnUnderTest.getBoard());
    assertThat(firstCopyTurnUnderTest.hasAction()).isEqualTo(firstTurnUnderTest.hasAction());
    assertThat(firstCopyTurnUnderTest.getAction()).isEqualTo(firstTurnUnderTest.getAction());
    verify(boardMock, times(1)).copy();

    clearInvocations(boardMock);

    final int secondTurnTestNumber = 29;

    Turn secondTurnUnderTest = new Turn(secondTurnTestNumber, boardMock);
    Turn secondCopyTurnUnderTest = secondTurnUnderTest.copy();

    assertThat(secondCopyTurnUnderTest)
        .isEqualTo(secondTurnUnderTest)
        .isNotSameAs(secondTurnUnderTest);
    assertThat(secondCopyTurnUnderTest.getTurnNumber()).isEqualTo(secondTurnUnderTest.getTurnNumber());
    assertThat(secondCopyTurnUnderTest.getBoard()).isEqualTo(secondTurnUnderTest.getBoard());
    assertThat(secondCopyTurnUnderTest.hasAction()).isEqualTo(secondTurnUnderTest.hasAction());
    assertThat(secondCopyTurnUnderTest.getAction()).isEqualTo(secondTurnUnderTest.getAction());
    verify(boardMock, times(1)).copy();
  }

  @Test
  void hasAction() {

    Turn firstTurnUnderTest = new Turn(3, boardMock, placementMock);
    assertThat(firstTurnUnderTest.hasAction()).isTrue();

    Turn secondTurnUnderTest = new Turn(7, boardMock, null);
    assertThat(secondTurnUnderTest.hasAction()).isFalse();

    Turn thirdTurnUnderTest = new Turn(4, boardMock);
    assertThat(thirdTurnUnderTest.hasAction()).isFalse();
  }
}