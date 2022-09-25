package de.fhkiel.ki.cathedral.game;

/**
 * The enum Color defines the colors a {@link Building} or an area can be colored.
 * <br><br>
 * Each Color has an Id which can be used in calculations.<br>
 * Certain Colors have Opponents or Sub-colors
 *
 *
 * @author      Eike Petersen {@literal <eike.petersen@fh-kiel.de>}
 * @version     1.0
 * @since       1.0
 */
public enum Color {

  /**
   * The No or None color.
   * <br><br>
   * Id is 0. <br>
   * Default neutral color of areas
   */
  None(0),

  /**
   * The Blue color.
   * <br><br>
   * Id is 1.
   */
  Blue(1),

  /**
   * The Black color.
   * <br><br>
   * Id is 2. <br>
   * Opponent is {@link Color#White} <br>
   * Sub-color is {@link Color#Black_Owned}
   */
  Black(2),

  /**
   * The color of black owned areas.
   * <br><br>
   * Id is 3.
   */
  Black_Owned(3),
  /**
   * The Whithe color.
   * <br><br>
   * Id is 4. <br>
   * Opponent is {@link Color#Black} <br>
   * Sub-color is {@link Color#White_Owned}
   */
  White(4),
  /**
   * The color of white owned areas.
   * <br><br>
   * Id is 5.
   */
  White_Owned(5);

  private final int id;

  Color(int id) {
    this.id = id;
  }

  /**
   * Gets the id of the enum-value.
   *
   * @return the color id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the sub-color coloring areas of the enum-value.
   *
   * @return the sub-color or {@link Color#None} if the color has no sub-color
   */
  public Color subColor() {
    return getSubColor(this);
  }

  /**
   * Gets the sub-color coloring areas of the color.
   *
   * @param color the color for which to get the sub-color
   * @return the sub-color or {@link Color#None} if the color has no sub-color
   */
  public static Color getSubColor(Color color) {
    switch (color) {
      case Black:
        return Black_Owned;
      case White:
        return White_Owned;
      default:
        return None;
    }
  }

  /**
   * Gets opposing color of the enum-value.
   *
   * @return the opposing color or {@link Color#None} if the color has no opposing color
   */
  public Color opponent() {
    return getOpponent(this);
  }

  /**
   * Gets opposing color of the enum-value.
   *
   * @param color the color to get the opposing color for
   * @return the opposing color or {@link Color#None} if the color has no opposing color
   */
  public static Color getOpponent(Color color) {
    switch (color) {
      case Black:
        return White;
      case White:
        return Black;
      default:
        return None;
    }
  }

  /**
   * Returns the id as a string.
   *
   * @return the id
   */
  @Override
  public String toString() {
    return id + "";
  }
}
