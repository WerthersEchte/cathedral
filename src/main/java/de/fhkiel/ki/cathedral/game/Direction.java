package de.fhkiel.ki.cathedral.game;

/**
 * The enum Direction describes the four cardinal directions a {@link Building} can be turned.
 * <br><br>
 * Each Direction has an Id which can be used in calculations.
 *
 *
 * @author      Eike Petersen {@literal <eike.petersen@fh-kiel.de>}
 * @version     1.0
 * @since       1.0
 */
public enum Direction {
  /**
   * The 0\u00B0 or North direction. Id is 0.
   */
  _0(0),
  /**
   * The 90\u00B0 or East direction. Id is 1.
   */
  _90(1),
  /**
   * The 180\u00B0 or South direction. Id is 2.
   */
  _180(2),
  /**
   * The 270\u00B0 or West direction. Id is 3.
   */
  _270(3);

  private final int id;

  Direction(int id) {
    this.id = id;
  }

  /**
   * Gets the id associated with the enum-value.
   *
   * @return the associated id
   */
  public int getId() {
    return id;
  }
}
