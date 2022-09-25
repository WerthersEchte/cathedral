package de.fhkiel.ki.cathedral.game;

import java.util.Objects;

/**
 * The class Position is a storage class for positions in a {@link Game}.
 * <br><br>
 * A Position is an <b>immutable</b> class containing the position in x and y coordinates and if the
 * resulting position is on the playfield (0&#60;=x&#60;=9;0&#60;=y&#60;=9).
 *
 * @author Eike Petersen {@literal <eike.petersen@fh-kiel.de>}
 * @version 1.0
 * @since 1.0
 */
public class Position {
  private static final int MIN_X = 0;
  private static final int MAX_X = 9;
  private static final int MIN_Y = 0;
  private static final int MAX_Y = 9;

  private final int x;
  private final int y;

  private final boolean viable;

  /**
   * Instantiates a new Position.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public Position(int x, int y) {
    this.x = x;
    this.y = y;

    viable = x >= MIN_X && x <= MAX_X && y >= MIN_Y && y <= MAX_Y;
  }

  /**
   * Returns the x coordinate of the position.
   *
   * @return the x value
   */
  public int x() {
    return x;
  }

  /**
   * Returns the y coordinate of the position.
   *
   * @return the y value
   */
  public int y() {
    return y;
  }

  /**
   * Checks if the position is on the playfield.
   *
   * @return true if it is one the playfield, false if not
   */
  public boolean isViable() {
    return viable;
  }

  /**
   * Convenience method to add another Position and this together creating a new Position.<br>
   * <b>This does not change this Position</b>
   *
   * @param position the Position to add
   * @return a <b>new</b> Position containing the result
   */
  public Position plus(Position position) {
    return new Position(x + position.x, y + position.y);
  }

  /**
   * Convenience method to add x and y values and this together creating a new Position.<br>
   * <b>This does not change this Position</b>
   *
   * @param x the x to add
   * @param y the y to add
   * @return a <b>new</b> Position containing the result
   */
  public Position plus(int x, int y) {
    return new Position(this.x + x, this.y + y);
  }

  /**
   * Convenience method to subtract another Position from this creating a new Position.<br>
   * <b>This does not change this Position</b>
   *
   * @param position the Position to subtract
   * @return a <b>new</b> Position containing the result
   */
  public Position minus(Position position) {
    return new Position(x - position.x, y - position.y);
  }

  /**
   * Convenience method to subtract x and y values from this creating a new Position.<br>
   * <b>This does not change this Position</b>
   *
   * @param x the x value to subtract
   * @param y the y value to subtract
   * @return a <b>new</b> Position containing the result
   */
  public Position minus(int x, int y) {
    return new Position(this.x - x, this.y - y);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Position position = (Position) o;
    return x == position.x && y == position.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  /**
   * Creates a string representing the Position in the form '(x/y)'.
   *
   * @return the string representing the Position
   */
  @Override
  public String toString() {
    return "(" + x + "/" + y + ")";
  }
}
