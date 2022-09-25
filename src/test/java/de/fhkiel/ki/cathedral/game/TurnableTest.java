package de.fhkiel.ki.cathedral.game;

import static org.assertj.core.api.Assertions.assertThat;

import de.fhkiel.ki.cathedral.game.Direction;
import de.fhkiel.ki.cathedral.game.Turnable;
import org.junit.jupiter.api.Test;

class TurnableTest {

  @Test
  void getRealDirection() {
    assertThat(Turnable.No.getRealDirection(Direction._0)).isEqualTo(Direction._0);
    assertThat(Turnable.No.getRealDirection(Direction._90)).isEqualTo(Direction._0);
    assertThat(Turnable.No.getRealDirection(Direction._180)).isEqualTo(Direction._0);
    assertThat(Turnable.No.getRealDirection(Direction._270)).isEqualTo(Direction._0);

    assertThat(Turnable.Half.getRealDirection(Direction._0)).isEqualTo(Direction._0);
    assertThat(Turnable.Half.getRealDirection(Direction._90)).isEqualTo(Direction._90);
    assertThat(Turnable.Half.getRealDirection(Direction._180)).isEqualTo(Direction._0);
    assertThat(Turnable.Half.getRealDirection(Direction._270)).isEqualTo(Direction._90);

    assertThat(Turnable.Full.getRealDirection(Direction._0)).isEqualTo(Direction._0);
    assertThat(Turnable.Full.getRealDirection(Direction._90)).isEqualTo(Direction._90);
    assertThat(Turnable.Full.getRealDirection(Direction._180)).isEqualTo(Direction._180);
    assertThat(Turnable.Full.getRealDirection(Direction._270)).isEqualTo(Direction._270);
  }
}