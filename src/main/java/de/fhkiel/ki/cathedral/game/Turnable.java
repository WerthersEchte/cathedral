package de.fhkiel.ki.cathedral.game;

import java.util.Arrays;
import java.util.List;

/**
 * The enum Turnable describes if a {@link Building} can be turned
 * and has a different form in these {@link Direction}s.
 * <br><br>
 * While a building can always be turned in any {@link Direction},
 * there are {@link Building}s where these turned forms are identical.<br>
 * To reduce the number of Possibilities in placing {@link Building}s,
 * Turnable defines if a {@link Building} has 4, 2 or 1 forms on the {@link Board}.
 *
 *
 * @author      Eike Petersen {@literal <eike.petersen@fh-kiel.de>}
 * @version     1.0
 * @since       1.0
 */
public enum Turnable {
  /**
   * The is no point in turning the Building in other {@link Direction}s,
   * as they will have always the same form.
   * <p>
   * Reduction from 400 max possible Turns to 100 max.<br>
   * Examples are {@link Building#White_Tavern}, {@link Building#Black_Infirmary}
   * </p>
   */
  No(Direction._0),

  /**
   * The Building has two forms. One in {@link Direction#_0} and one in {@link Direction#_90}.
   * The other Directions are the same as their mirror.
   * <p>
   * Reduction from 400 max possible Turns to 200 max.<br>
   * Examples are {@link Building#White_Bridge}, {@link Building#Black_Stable}
   * </p>
   */
  Half(Direction._0, Direction._90),

  /**
   * The Building has four distinct forms. One in each {@link Direction}.
   * <p>
   * Examples are {@link Building#Blue_Cathedral}
   * </p>
   */
  Full(Direction._0, Direction._90, Direction._180, Direction._270);

  private final int numberOfPossibleForms;
  private final List<Direction> possibleDirections;

  Turnable(Direction... possibleDirections) {
    this.numberOfPossibleForms = possibleDirections.length;
    this.possibleDirections = Arrays.stream(possibleDirections).toList();
  }

  /**
   * Gets the number of possible forms associated with the enum-value.
   * <br><br>
   * Can be used in calculations over possible turns.
   *
   * @return the number of possible forms
   */
  public int getNumberOfPossibleForms() {
    return numberOfPossibleForms;
  }

  /**
   * Gets a list of possible {@link Direction}s this enum-value and subsequent forms can be turned
   * to.
   *
   * @return a unmodifiable list of the possible {@link Direction}s.
   */
  public List<Direction> getPossibleDirections() {
    return possibleDirections;
  }

  /**
   * Gets the possible {@link Direction} that corresponds to the given {@link Direction}.
   * <br><br>
   * For {@link Turnable#Full} this just returns the given {@link Direction}.<br>
   * For {@link Turnable#Half} this returns {@link Direction#_0} for {@link Direction#_0} or
   * {@link Direction#_180} and {@link Direction#_90} for {@link Direction#_90} or
   * {@link Direction#_270}.<br>
   * For {@link Turnable#No} this returns {@link Direction#_0} for any given {@link Direction}.
   *
   * @param direction the {@link Direction} to convert
   * @return the {@link Direction} that corresponds to the given {@link Direction}
   */
  public Direction getRealDirection(Direction direction) {
    return possibleDirections.get(direction.getId() % getNumberOfPossibleForms());
  }
}
