package de.fhkiel.ki.cathedral.game;

import java.util.List;
import java.util.Objects;

/**
 * The immutable class Placement is used to take a turn in a {@link Game}.
 * <br><br>
 * The Placement is composed by a {@link Position}, a {@link Direction} and a {@link Building}.
 *
 * @author Eike Petersen {@literal <eike.petersen@fh-kiel.de>}
 * @version 1.0
 * @since 1.0
 */
public class Placement {

  private final Position position;
  private final Direction direction;
  private final Building building;

  /**
   * Instantiates a new Placement.<br>
   * The {@link Direction} is not legal in respect to the building, it will be transformed into
   * a legal {@link Direction}.
   *
   * @param position  the position
   * @param direction the direction
   * @param building  the building
   */
  public Placement(Position position, Direction direction, Building building) {
    this.position = position;
    this.direction = building.getTurnable().getRealDirection(direction);
    this.building = building;
  }

  /**
   * Instantiates a new Placement with x and y coordinates.<br>
   * The coordinates will be saved in a {@link Position}.<br>
   * The {@link Direction} is not legal in respect to the building, it will be transformed into
   * a legal {@link Direction}.
   *
   * @param x         the x coordinate
   * @param y         the y coordinate
   * @param direction the direction
   * @param building  the building
   */
  public Placement(int x, int y, Direction direction, Building building) {
    this(new Position(x, y), direction, building);
  }

  /**
   * Returns the x coordinate of the stored position.
   *
   * @return the x coordinate
   */
  public int x() {
    return position.x();
  }

  /**
   * Returns the y coordinate of the stored position.
   *
   * @return the y coordinate
   */
  public int y() {
    return position.y();
  }

  /**
   * Returns the stored position.
   *
   * @return the stored position
   */
  public Position position() {
    return position;
  }

  /**
   * Returns the stored direction.<br>
   * This will always be a valid {@link Direction} in respect to the
   * stored {@link Building}s {@link Turnable}.
   *
   * @return the stored direction
   */
  public Direction direction() {
    return direction;
  }

  /**
   * Returns the stored building.
   *
   * @return the stored building
   */
  public Building building() {
    return building;
  }

  /**
   * Returns a list of {@link Position}s representing the stored {@link Building} turned in
   * the stored {@link Direction}.
   *
   * @return a list of {@link Position}s representing the turned {@link Building}
   */
  public List<Position> form() {
    return building.turn(direction);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Placement placement = (Placement) o;
    return position.equals(placement.position)
        && direction == placement.direction
        && building.equals(placement.building);
  }

  @Override
  public int hashCode() {
    return Objects.hash(position, direction, building);
  }

  /**
   * Creates a string representing the Placement in the form
   * 'Building: {@link Building} Position: {@link Position} Direction {@link Direction}'.
   *
   * @return the string representing the Placement
   */
  @Override
  public String toString() {
    return "Building: " + building + " Position: " + position + " Direction " + direction;
  }
}
