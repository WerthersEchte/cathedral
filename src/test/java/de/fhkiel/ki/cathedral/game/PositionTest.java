package de.fhkiel.ki.cathedral.game;

import static org.assertj.core.api.Assertions.assertThat;

import de.fhkiel.ki.cathedral.game.Position;
import org.junit.jupiter.api.Test;

class PositionTest {

  @Test
  void construct() {
    final int x1 = 7, y1 = 4;

    Position positionUnderTest1 = new Position(x1 , y1);

    assertThat(positionUnderTest1.x()).isEqualTo(x1);
    assertThat(positionUnderTest1.y()).isEqualTo(y1);
    assertThat(positionUnderTest1.isViable()).isTrue();

    final int x2 = 1, y2 = 12;

    Position positionUnderTest2 = new Position(x2 , y2);

    assertThat(positionUnderTest2.x()).isEqualTo(x2);
    assertThat(positionUnderTest2.y()).isEqualTo(y2);
    assertThat(positionUnderTest2.isViable()).isFalse();
  }

  @Test
  void equals(){
    final int x1 = 1, y1 = 12;
    Position positionUnderTest = new Position(x1, y1);

    assertThat(positionUnderTest)
        .isEqualTo(positionUnderTest)
        .isEqualTo(new Position(x1, y1));

    final int x2 = 12, y2 = 1;
    assertThat(positionUnderTest)
        .isNotEqualTo(null)
        .isNotEqualTo(new Position(x2, y1))
        .isNotEqualTo(new Position(x2, y1))
        .isNotEqualTo(new Position(x1, y2))
        .isNotEqualTo(new Position(x2, y2));
  }

  void hashValue(){
    final int x1 = 1, y1 = 12;
    Position positionUnderTest = new Position(x1, y1);

    assertThat(positionUnderTest)
        .hasSameHashCodeAs(new Position(x1, y1));

    final int x2 = 12, y2 = 1;
    assertThat(positionUnderTest)
        .doesNotHaveSameHashCodeAs(new Position(x2, y1))
        .doesNotHaveSameHashCodeAs(new Position(x1, y2))
        .doesNotHaveSameHashCodeAs(new Position(x2, y2));
  }

  @Test
  void viable(){
    final int x1 = 7, y1 = 4;

    Position positionUnderTest1 = new Position(x1 , y1);

    assertThat(positionUnderTest1.isViable()).isTrue();

    // limits
    final int xLimit1 = 0, yLimit1 = 0;

    Position positionUnderTestLimit1 = new Position(xLimit1 , yLimit1);

    assertThat(positionUnderTestLimit1.isViable()).isTrue();

    final int xLimit2 = 9, yLimit2 = 9;

    Position positionUnderTestLimit2 = new Position(xLimit2 , yLimit2);

    assertThat(positionUnderTestLimit2.isViable()).isTrue();

    final int xLimit3 = 0, yLimit3 = 5;

    Position positionUnderTestLimit3 = new Position(xLimit3 , yLimit3);

    assertThat(positionUnderTestLimit3.isViable()).isTrue();

    final int xLimit4 = 9, yLimit4 = 5;

    Position positionUnderTestLimit4 = new Position(xLimit4 , yLimit4);

    assertThat(positionUnderTestLimit4.isViable()).isTrue();

    final int xLimit5 = 5, yLimit5 = 0;

    Position positionUnderTestLimit5 = new Position(xLimit5 , yLimit5);

    assertThat(positionUnderTestLimit5.isViable()).isTrue();

    final int xLimit6 = 5, yLimit6 = 9;

    Position positionUnderTestLimit6 = new Position(xLimit6 , yLimit6);

    assertThat(positionUnderTestLimit6.isViable()).isTrue();
  }

  @Test
  void notViable(){
    final int x1 = -10, y1 = 30;

    Position positionUnderTest1 = new Position(x1 , y1);

    assertThat(positionUnderTest1.isViable()).isFalse();

    final int x2 = -1, y2 = 5;

    Position positionUnderTest2 = new Position(x2 , y2);

    assertThat(positionUnderTest2.isViable()).isFalse();

    final int x3 = 10, y3 = 5;

    Position positionUnderTest3 = new Position(x3 , y3);

    assertThat(positionUnderTest3.isViable()).isFalse();

    final int x4 = 5, y4 = -1;

    Position positionUnderTest4 = new Position(x4 , y4);

    assertThat(positionUnderTest4.isViable()).isFalse();

    final int x5 = 5, y5 = 10;

    Position positionUnderTest5 = new Position(x5 , y5);

    assertThat(positionUnderTest5.isViable()).isFalse();

  }

  @Test
  void additionPosition(){

    final int x1 = 3, y1 = 6;
    final Position position1 = new Position(x1 , y1);
    final int x2 = 4, y2 = 1;
    final Position position2 = new Position(x2 , y2);

    Position positionUnderTest1 = position1.plus(position2);

    assertThat(position1.x()).isEqualTo(x1);
    assertThat(position1.y()).isEqualTo(y1);
    assertThat(position1.isViable()).isTrue();

    assertThat(position2.x()).isEqualTo(x2);
    assertThat(position2.y()).isEqualTo(y2);
    assertThat(position2.isViable()).isTrue();

    assertThat(positionUnderTest1.x()).isEqualTo(x1 + x2);
    assertThat(positionUnderTest1.y()).isEqualTo(y1 + y2);
    assertThat(positionUnderTest1.isViable()).isTrue();

    final int x3 = 9, y3 = 1;
    final Position position3 = new Position(x3 , y3);

    Position positionUnderTest2 = position1.plus(position3);

    assertThat(position1.x()).isEqualTo(x1);
    assertThat(position1.y()).isEqualTo(y1);
    assertThat(position1.isViable()).isTrue();

    assertThat(position3.x()).isEqualTo(x3);
    assertThat(position3.y()).isEqualTo(y3);
    assertThat(position3.isViable()).isTrue();

    assertThat(positionUnderTest2.x()).isEqualTo(x1 + x3);
    assertThat(positionUnderTest2.y()).isEqualTo(y1 + y3);
    assertThat(positionUnderTest2.isViable()).isFalse();

    final int x4 = -2, y4 = 1;
    final Position position4 = new Position(x4 , y4);

    Position positionUnderTest3 = position1.plus(position4);

    assertThat(position1.x()).isEqualTo(x1);
    assertThat(position1.y()).isEqualTo(y1);
    assertThat(position1.isViable()).isTrue();

    assertThat(position4.x()).isEqualTo(x4);
    assertThat(position4.y()).isEqualTo(y4);
    assertThat(position4.isViable()).isFalse();

    assertThat(positionUnderTest3.x()).isEqualTo(x1 + x4);
    assertThat(positionUnderTest3.y()).isEqualTo(y1 + y4);
    assertThat(positionUnderTest3.isViable()).isTrue();
  }

  @Test
  void subtractionPosition(){
    final int x1 = 4, y1 = 8;
    final Position position1 = new Position(x1 , y1);
    final int x2 = 2, y2 = 4;
    final Position position2 = new Position(x2 , y2);

    Position positionUnderTest1 = position1.minus(position2);

    assertThat(position1.x()).isEqualTo(x1);
    assertThat(position1.y()).isEqualTo(y1);
    assertThat(position1.isViable()).isTrue();

    assertThat(position2.x()).isEqualTo(x2);
    assertThat(position2.y()).isEqualTo(y2);
    assertThat(position2.isViable()).isTrue();

    assertThat(positionUnderTest1.x()).isEqualTo(x1 - x2);
    assertThat(positionUnderTest1.y()).isEqualTo(y1 - y2);
    assertThat(positionUnderTest1.isViable()).isTrue();

    final int x3 = 9, y3 = 1;
    final Position position3 = new Position(x3 , y3);

    Position positionUnderTest2 = position1.minus(position3);

    assertThat(position1.x()).isEqualTo(x1);
    assertThat(position1.y()).isEqualTo(y1);
    assertThat(position1.isViable()).isTrue();

    assertThat(position3.x()).isEqualTo(x3);
    assertThat(position3.y()).isEqualTo(y3);
    assertThat(position3.isViable()).isTrue();

    assertThat(positionUnderTest2.x()).isEqualTo(x1 - x3);
    assertThat(positionUnderTest2.y()).isEqualTo(y1 - y3);
    assertThat(positionUnderTest2.isViable()).isFalse();

    final int x4 = -4, y4 = 5;
    final Position position4 = new Position(x4 , y4);

    Position positionUnderTest3 = position1.minus(position4);

    assertThat(position1.x()).isEqualTo(x1);
    assertThat(position1.y()).isEqualTo(y1);
    assertThat(position1.isViable()).isTrue();

    assertThat(position4.x()).isEqualTo(x4);
    assertThat(position4.y()).isEqualTo(y4);
    assertThat(position4.isViable()).isFalse();

    assertThat(positionUnderTest3.x()).isEqualTo(x1 - x4);
    assertThat(positionUnderTest3.y()).isEqualTo(y1 - y4);
    assertThat(positionUnderTest3.isViable()).isTrue();
  }

  @Test
  void additionValue(){

    final int x1 = 3, y1 = 6;
    final Position position1 = new Position(x1 , y1);
    final int x2 = 4, y2 = 1;

    Position positionUnderTest1 = position1.plus(x2, y2);

    assertThat(position1.x()).isEqualTo(x1);
    assertThat(position1.y()).isEqualTo(y1);
    assertThat(position1.isViable()).isTrue();

    assertThat(positionUnderTest1.x()).isEqualTo(x1 + x2);
    assertThat(positionUnderTest1.y()).isEqualTo(y1 + y2);
    assertThat(positionUnderTest1.isViable()).isTrue();

    final int x3 = 9, y3 = 1;

    Position positionUnderTest2 = position1.plus(x3, y3);

    assertThat(position1.x()).isEqualTo(x1);
    assertThat(position1.y()).isEqualTo(y1);
    assertThat(position1.isViable()).isTrue();

    assertThat(positionUnderTest2.x()).isEqualTo(x1 + x3);
    assertThat(positionUnderTest2.y()).isEqualTo(y1 + y3);
    assertThat(positionUnderTest2.isViable()).isFalse();

    final int x4 = -2, y4 = 1;

    Position positionUnderTest3 = position1.plus(x4, y4);

    assertThat(position1.x()).isEqualTo(x1);
    assertThat(position1.y()).isEqualTo(y1);
    assertThat(position1.isViable()).isTrue();

    assertThat(positionUnderTest3.x()).isEqualTo(x1 + x4);
    assertThat(positionUnderTest3.y()).isEqualTo(y1 + y4);
    assertThat(positionUnderTest3.isViable()).isTrue();
  }

  @Test
  void subtractionValue(){
    final int x1 = 4, y1 = 8;
    final Position position1 = new Position(x1 , y1);
    final int x2 = 2, y2 = 4;

    Position positionUnderTest1 = position1.minus(x2, y2);

    assertThat(position1.x()).isEqualTo(x1);
    assertThat(position1.y()).isEqualTo(y1);
    assertThat(position1.isViable()).isTrue();

    assertThat(positionUnderTest1.x()).isEqualTo(x1 - x2);
    assertThat(positionUnderTest1.y()).isEqualTo(y1 - y2);
    assertThat(positionUnderTest1.isViable()).isTrue();

    final int x3 = 9, y3 = 1;

    Position positionUnderTest2 = position1.minus(x3, y3);

    assertThat(position1.x()).isEqualTo(x1);
    assertThat(position1.y()).isEqualTo(y1);
    assertThat(position1.isViable()).isTrue();

    assertThat(positionUnderTest2.x()).isEqualTo(x1 - x3);
    assertThat(positionUnderTest2.y()).isEqualTo(y1 - y3);
    assertThat(positionUnderTest2.isViable()).isFalse();

    final int x4 = -4, y4 = 5;

    Position positionUnderTest3 = position1.minus(x4, y4);

    assertThat(position1.x()).isEqualTo(x1);
    assertThat(position1.y()).isEqualTo(y1);
    assertThat(position1.isViable()).isTrue();

    assertThat(positionUnderTest3.x()).isEqualTo(x1 - x4);
    assertThat(positionUnderTest3.y()).isEqualTo(y1 - y4);
    assertThat(positionUnderTest3.isViable()).isTrue();
  }

}