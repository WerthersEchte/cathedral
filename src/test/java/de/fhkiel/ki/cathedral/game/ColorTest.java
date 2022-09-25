package de.fhkiel.ki.cathedral.game;

import static org.assertj.core.api.Assertions.assertThat;

import de.fhkiel.ki.cathedral.game.Color;
import org.junit.jupiter.api.Test;

class ColorTest {

  @Test
  void subColor() {
    assertThat(Color.Black.subColor()).isEqualTo(Color.Black_Owned);
    assertThat(Color.White.subColor()).isEqualTo(Color.White_Owned);

    assertThat(Color.Black_Owned.subColor()).isEqualTo(Color.None);
    assertThat(Color.White_Owned.subColor()).isEqualTo(Color.None);
    assertThat(Color.Blue.subColor()).isEqualTo(Color.None);
    assertThat(Color.None.subColor()).isEqualTo(Color.None);
  }

  @Test
  void getSubColor() {
    assertThat(Color.getSubColor(Color.Black)).isEqualTo(Color.Black_Owned);
    assertThat(Color.getSubColor(Color.White)).isEqualTo(Color.White_Owned);

    assertThat(Color.getSubColor(Color.Black_Owned)).isEqualTo(Color.None);
    assertThat(Color.getSubColor(Color.White_Owned)).isEqualTo(Color.None);
    assertThat(Color.getSubColor(Color.Blue)).isEqualTo(Color.None);
    assertThat(Color.getSubColor(Color.None)).isEqualTo(Color.None);
  }

  @Test
  void opponent() {
    assertThat(Color.Black.opponent()).isEqualTo(Color.White);
    assertThat(Color.White.opponent()).isEqualTo(Color.Black);

    assertThat(Color.Black_Owned.opponent()).isEqualTo(Color.None);
    assertThat(Color.White_Owned.opponent()).isEqualTo(Color.None);
    assertThat(Color.Blue.opponent()).isEqualTo(Color.None);
    assertThat(Color.None.opponent()).isEqualTo(Color.None);
  }

  @Test
  void getOpponent() {
    assertThat(Color.getOpponent(Color.Black)).isEqualTo(Color.White);
    assertThat(Color.getOpponent(Color.White)).isEqualTo(Color.Black);

    assertThat(Color.getOpponent(Color.Black_Owned)).isEqualTo(Color.None);
    assertThat(Color.getOpponent(Color.White_Owned)).isEqualTo(Color.None);
    assertThat(Color.getOpponent(Color.Blue)).isEqualTo(Color.None);
    assertThat(Color.getOpponent(Color.None)).isEqualTo(Color.None);
  }
}