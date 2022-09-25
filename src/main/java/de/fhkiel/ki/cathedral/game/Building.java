package de.fhkiel.ki.cathedral.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The enum Building defines all possible builings in the game. These buildings are final and <b>can
 * not be changed on runtime</b>.
 * <br><br>
 * Buildings have an id, a name, a {@link Color}, the number of them in a normal game,
 * their score value and how it is {@link Turnable}  <br>
 * Their form is described with a list of {@link Position} and each possible {@link Direction} has
 * an own form. These can be accessed through {@link Building#turn(Direction)}<br>
 * Derived from the form are the corners of a building which are all positions directly touching
 * positions of the form in a {@link Direction}. The form positions are not contained in these
 * lists. These can be accessed through {@link Building#corners(Direction)}<br>
 * Derived from the form is the silhouette of a building which are all positions directly
 * surrounding positions of the form. The form positions are not contained in these lists.
 * These can be accessed through {@link Building#silhouette(Direction)}<br>
 * <br><br>
 * The forms of the individual building are shown in the following format in this documentation:<br>
 * <pre>
 *        0\u00B0   |    90\u00B0   |    180\u00B0  |    270\u00B0
 *    -1  0  1 | -1  0  1 | -1  0  1 | -1  0  1 x
 * -1          |          |          |
 *  0     X    |     X    |     X    |     X
 *  1          |          |          |
 *  y
 * </pre>
 * X shows the center of the form. O is a part of the form. <br>
 * Depending on the {@link Turnable} type, only certain forms are shown.
 *
 * @author      Eike Petersen {@literal <eike.petersen@fh-kiel.de>}
 * @version     1.0
 * @since       1.0
 */
public enum Building {

  /**
   * The Black tavern.
   * <br><br>
   * id is 1, name is 'Black Tavern', color is {@link Color#Black}, normal games have 2,
   * score value is 1 and it is {@link Turnable#No} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   
   *    -1  0  1 x
   * -1          
   *  0     X    
   *  1          
   *  y
   * </pre>
   */
  Black_Tavern(1, "Black Tavern", Color.Black, 2, Turnable.No,
      new int[][] {{0, 0}}),

  /**
   * The Black stable.
   * <br><br>
   * id is 2, name is 'Black Stable', color is {@link Color#Black}, normal games have 2,
   * score value is 2 and it is {@link Turnable#Half} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0
   *    -1  0  1 | -1  0  1 x
   * -1          |
   *  0     X  O |     X
   *  1          |     O
   *  y
   * </pre>
   */
  Black_Stable(2, "Black Stable", Color.Black, 2, Turnable.Half,
      new int[][] {{0, 0}, {1, 0}}),

  /**
   * The Black inn.
   * <br><br>
   * id is 3, name is 'Black Inn', color is {@link Color#Black}, normal games have 2,
   * score value is 3 and it is {@link Turnable#Full} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0   |    180\u00B0  |    270\u00B0
   *    -1  0  1 | -1  0  1 | -1  0  1 | -1  0  1 x
   * -1          |          |  O       |     O  O
   *  0     X  O |     X    |  O  X    |     X
   *  1        O |  O  O    |          |
   *  y
   * </pre>
   */
  Black_Inn(3, "Black Inn", Color.Black, 2, Turnable.Full,
      new int[][] {{0, 0}, {1, 0}, {1, 1}}),

  /**
   * The Black bridge.
   * <br><br>
   * id is 4, name is 'Black Bridge', color is {@link Color#Black}, normal games have 1,
   * score value is 3 and it is {@link Turnable#Half} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0
   *    -1  0  1 | -1  0  1 x
   * -1     O    |
   *  0     X    |  O  X  O
   *  1     O    |
   *  y
   * </pre>
   */
  Black_Bridge(4, "Black Bridge", Color.Black, Turnable.Half,
      new int[][] {{0, 0}, {0, -1}, {0, 1}}),

  /**
   * The Black manor.
   * <br><br>
   * id is 5, name is 'Black Manor', color is {@link Color#Black}, normal games have 1,
   * score value is 4 and it is {@link Turnable#Full} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0   |    180\u00B0  |    270\u00B0
   *    -1  0  1 | -1  0  1 | -1  0  1 | -1  0  1 x
   * -1          |     O    |     O    |     O
   *  0  O  X  O |  O  X    |  O  X  O |     X  O
   *  1     O    |     O    |          |     O
   *  y
   * </pre>
   */
  Black_Manor(5, "Black Manor", Color.Black, Turnable.Full,
      new int[][] {{-1, 0}, {0, 0}, {1, 0}, {0, 1}}),

  /**
   * The Black square.
   * <br><br>
   * id is 6, name is 'Black Square', color is {@link Color#Black}, normal games have 1,
   * score value is 4 and it is {@link Turnable#No} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0
   *    -1  0  1 x
   * -1
   *  0     X  O
   *  1     O  O
   *  y
   * </pre>
   */
  Black_Square(6, "Black Square", Color.Black, Turnable.No,
      new int[][] {{0, 0}, {0, 1}, {1, 0}, {1, 1}}),

  /**
   * The Black abbey.
   * <br><br>
   * id is 7, name is 'Black Abbey', color is {@link Color#Black}, normal games have 1,
   * score value is 4 and it is {@link Turnable#Half} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0
   *    -1  0  1 | -1  0  1 x
   * -1          |     O
   *  0  O  X    |  O  X
   *  1     O  O |  O
   *  y
   * </pre>
   */
  Black_Abbey(7, "Black Abbey", Color.Black, Turnable.Half,
      new int[][] {{-1, 0}, {0, 0}, {0, 1}, {1, 1}}),

  /**
   * The Black infirmary.
   * <br><br>
   * id is 8, name is 'Black Infirmary', color is {@link Color#Black}, normal games have 1,
   * score value is 5 and it is {@link Turnable#No} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0
   *    -1  0  1 x
   * -1     O
   *  0  O  X  O
   *  1     O
   *  y
   * </pre>
   */
  Black_Infirmary(8, "Black Infirmary", Color.Black, Turnable.No,
      new int[][] {{-1, 0}, {0, -1}, {0, 0}, {0, 1}, {1, 0}}),

  /**
   * The Black castle.
   * <br><br>
   * id is 9, name is 'Black Castle', color is {@link Color#Black}, normal games have 1,
   * score value is 5 and it is {@link Turnable#Full} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0   |    180\u00B0  |    270\u00B0
   *    -1  0  1 | -1  0  1 | -1  0  1 | -1  0  1 x
   * -1          |  O  O    |  O     O |     O  O
   *  0  O  X  O |     X    |  O  X  O |     X
   *  1  O     O |  O  O    |          |     O  O
   *  y
   * </pre>
   */
  Black_Castle(9, "Black Castle", Color.Black, Turnable.Full,
      new int[][] {{-1, 0}, {-1, 1}, {0, 0}, {1, 0}, {1, 1}}),

  /**
   * The Black tower.
   * <br><br>
   * id is 10, name is 'Black Tower', color is {@link Color#Black}, normal games have 1,
   * score value is 5 and it is {@link Turnable#Full} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0   |    180\u00B0  |    270\u00B0
   *    -1  0  1 | -1  0  1 | -1  0  1 | -1  0  1 x
   * -1  O  O    |        O |  O       |     O  O
   *  0     X  O |     X  O |  O  X    |  O  X
   *  1        O |  O  O    |     O  O |  O
   *  y
   * </pre>
   */
  Black_Tower(10, "Black Tower", Color.Black, Turnable.Full,
      new int[][] {{-1, -1}, {0, -1}, {0, 0}, {1, 0}, {1, 1}}),

  /**
   * The Black academy.
   * <br><br>
   * id is 11, name is 'Black Academy', color is {@link Color#Black}, normal games have 1,
   * score value is 5 and it is {@link Turnable#Full} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0   |    180\u00B0  |    270\u00B0
   *    -1  0  1 | -1  0  1 | -1  0  1 | -1  0  1 x
   * -1     O  O |     O    |     O    |  O
   *  0  O  X    |  O  X  O |     X  O |  O  X  O
   *  1     O    |        O |  O  O    |     O
   *  y
   * </pre>
   */
  Black_Academy(11, "Black Academy", Color.Black, Turnable.Full,
      new int[][] {{-1, 0}, {0, -1}, {0, 0}, {0, 1}, {1, -1}}),


  /**
   * The White tavern.
   * <br><br>
   * id is 12, name is 'White Tavern', color is {@link Color#White}, normal games have 2,
   * score value is 1 and it is {@link Turnable#No} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0
   *    -1  0  1 x
   * -1
   *  0     X
   *  1
   *  y
   * </pre>
   */
  White_Tavern(12, "White Tavern", Color.White, 2, Turnable.No,
      new int[][] {{0, 0}}),

  /**
   * The White stable.
   * <br><br>
   * id is 13, name is 'White Stable', color is {@link Color#White}, normal games have 2,
   * score value is 2 and it is {@link Turnable#Half} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0
   *    -1  0  1 | -1  0  1 x
   * -1          |
   *  0     X  O |     X
   *  1          |     O
   *  y
   * </pre>
   */
  White_Stable(13, "White Stable", Color.White, 2, Turnable.Half,
      new int[][] {{0, 0}, {1, 0}}),

  /**
   * The White inn.
   * <br><br>
   * id is 14, name is 'White Inn', color is {@link Color#White}, normal games have 2,
   * score value is 3 and it is {@link Turnable#Full} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0   |    180\u00B0  |    270\u00B0
   *    -1  0  1 | -1  0  1 | -1  0  1 | -1  0  1 x
   * -1          |          |  O       |     O  O
   *  0     X  O |     X    |  O  X    |     X
   *  1        O |  O  O    |          |
   *  y
   * </pre>
   */
  White_Inn(14, "White Inn", Color.White, 2, Turnable.Full,
      new int[][] {{0, 0}, {1, 0}, {1, 1}}),

  /**
   * The White bridge.
   * <br><br>
   * id is 15, name is 'White Bridge', color is {@link Color#White}, normal games have 1,
   * score value is 3 and it is {@link Turnable#Half} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0
   *    -1  0  1 | -1  0  1 x
   * -1     O    |
   *  0     X    |  O  X  O
   *  1     O    |
   *  y
   * </pre>
   */
  White_Bridge(15, "White Bridge", Color.White, Turnable.Half,
      new int[][] {{0, -1}, {0, 0}, {0, 1}}),

  /**
   * The White manor.
   * <br><br>
   * id is 16, name is 'White Manor', color is {@link Color#White}, normal games have 1,
   * score value is 4 and it is {@link Turnable#Full} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0   |    180\u00B0  |    270\u00B0
   *    -1  0  1 | -1  0  1 | -1  0  1 | -1  0  1 x
   * -1          |     O    |     O    |     O
   *  0  O  X  O |  O  X    |  O  X  O |     X  O
   *  1     O    |     O    |          |     O
   *  y
   * </pre>
   */
  White_Manor(16, "White Manor", Color.White, Turnable.Full,
      new int[][] {{-1, 0}, {0, 0}, {1, 0}, {0, 1}}),

  /**
   * The White square.
   * <br><br>
   * id is 17, name is 'White Square', color is {@link Color#White}, normal games have 1,
   * score value is 4 and it is {@link Turnable#No} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0
   *    -1  0  1 x
   * -1
   *  0     X  O
   *  1     O  O
   *  y
   * </pre>
   */
  White_Square(17, "White Square", Color.White, Turnable.No,
      new int[][] {{0, 0}, {0, 1}, {1, 0}, {1, 1}}),

  /**
   * The White abbey.
   * <br><br>
   * id is 18, name is 'White Abbey', color is {@link Color#White}, normal games have 1,
   * score value is 4 and it is {@link Turnable#Half} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0
   *    -1  0  1 | -1  0  1 x
   * -1          |  O
   *  0     X  O |  O  X
   *  1  O  O    |     O
   *  y
   * </pre>
   */
  White_Abbey(18, "White Abbey", Color.White, Turnable.Half,
      new int[][] {{-1, 1}, {0, 0}, {0, 1}, {1, 0}}),

  /**
   * The White infirmary.
   * <br><br>
   * id is 19, name is 'White Infirmary', color is {@link Color#White}, normal games have 1,
   * score value is 5 and it is {@link Turnable#No} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0
   *    -1  0  1 x
   * -1     O
   *  0  O  X  O
   *  1     O
   *  y
   * </pre>
   */
  White_Infirmary(19, "White Infirmary", Color.White, Turnable.No,
      new int[][] {{-1, 0}, {0, -1}, {0, 0}, {0, 1}, {1, 0}}),

  /**
   * The White castle.
   * <br><br>
   * id is 20, name is 'White Castle', color is {@link Color#White}, normal games have 1,
   * score value is 5 and it is {@link Turnable#Full} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0   |    180\u00B0  |    270\u00B0
   *    -1  0  1 | -1  0  1 | -1  0  1 | -1  0  1 x
   * -1          |  O  O    |  O     O |     O  O
   *  0  O  X  O |     X    |  O  X  O |     X
   *  1  O     O |  O  O    |          |     O  O
   *  y
   * </pre>
   */
  White_Castle(20, "White Castle", Color.White, Turnable.Full,
      new int[][] {{-1, 0}, {-1, 1}, {0, 0}, {1, 0}, {1, 1}}),

  /**
   * The White tower.
   * <br><br>
   * id is 21, name is 'White Tower', color is {@link Color#White}, normal games have 1,
   * score value is 5 and it is {@link Turnable#Full} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0   |    180\u00B0  |    270\u00B0
   *    -1  0  1 | -1  0  1 | -1  0  1 | -1  0  1 x
   * -1  O  O    |        O |  O       |     O  O
   *  0     X  O |     X  O |  O  X    |  O  X
   *  1        O |  O  O    |     O  O |  O
   *  y
   * </pre>
   */
  White_Tower(21, "White Tower", Color.White, Turnable.Full,
      new int[][] {{-1, -1}, {0, -1}, {0, 0}, {1, 0}, {1, 1}}),

  /**
   * The White academy.
   * <br><br>
   * id is 22, name is 'White Academy', color is {@link Color#White}, normal games have 1,
   * score value is 5 and it is {@link Turnable#Full} <br>
   * Forms are: <br>
   * <pre>
   *        0\u00B0   |    90\u00B0   |    180\u00B0  |    270\u00B0
   *    -1  0  1 | -1  0  1 | -1  0  1 | -1  0  1 x
   * -1  O  O    |        O |     O    |     O
   *  0     X  O |  O  X  O |  O  X    |  O  X  O
   *  1     O    |     O    |     O  O |  O
   *  y
   * </pre>
   */
  White_Academy(22, "White Academy", Color.White, Turnable.Full,
      new int[][] {{-1, -1}, {0, -1}, {0, 0}, {0, 1}, {1, 0}}),

  /**
   * The Blue cathedral.
   * <br><br>
   * id is 23, name is 'Cathedral', color is {@link Color#Blue}, normal games have 1,
   * score value is 0 (5 for blue :)) and it is {@link Turnable#Full} <br>
   * Forms are: <br>
   * <pre>
   *           0\u00B0      |       90\u00B0      |       180\u00B0     |       270\u00B0
   *    -2 -1  0  1  2 | -2 -1  0  1  2 | -2 -1  0  1  2 | -2 -1  0  1  2 x
   * -2                |                |        O       |
   * -1        O       |        O       |        O       |        O
   *  0     O  X  O    |  O  O  X  O    |     O  X  O    |     O  X  O  O
   *  1        O       |        O       |        O       |        O
   *  2        O       |                |                |
   *  y
   * </pre>
   */
  Blue_Cathedral(23, "Cathedral", Color.Blue, Turnable.Full,
      new int[][] {{-1, 0}, {0, -1}, {0, 0}, {0, 1}, {0, 2}, {1, 0}});

  private final int id;
  private final String name;
  private final Color color;
  private final int numberInGame;
  private final Turnable turnable;
  private final Set<Position> form;
  private final Set<Position> corners;
  private final Set<Position> silhouette;

  private Set<Placement> allPossiblePlacements;

  Building(int id, String name, Color color, int numberInGame, Turnable turnable,
           int[][] form) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.numberInGame = numberInGame;
    this.turnable = turnable;
    this.form = Set.copyOf(Arrays
        .stream(form)
        .map(ints -> new Position(ints[0], ints[1]))
        .collect(Collectors.toSet()));

    Set<Position> tempCorners = new HashSet<>();
    Set<Position> tempSilhouette = new HashSet<>();

    for (int[] part : form) {
      // generating the corners
      tempCorners.add(new Position(part[0] + 1, part[1]));
      tempCorners.add(new Position(part[0] - 1, part[1]));
      tempCorners.add(new Position(part[0], part[1] + 1));
      tempCorners.add(new Position(part[0], part[1] - 1));

      // generating the silhouette
      tempSilhouette.add(new Position(part[0] + 1, part[1]));
      tempSilhouette.add(new Position(part[0] - 1, part[1]));
      tempSilhouette.add(new Position(part[0], part[1] + 1));
      tempSilhouette.add(new Position(part[0], part[1] - 1));
      tempSilhouette.add(new Position(part[0] + 1, part[1] + 1));
      tempSilhouette.add(new Position(part[0] - 1, part[1] + 1));
      tempSilhouette.add(new Position(part[0] + 1, part[1] - 1));
      tempSilhouette.add(new Position(part[0] - 1, part[1] - 1));

    }

    this.form.forEach(tempCorners::remove);
    this.form.forEach(tempSilhouette::remove);

    this.corners = Set.copyOf(tempCorners);
    this.silhouette = Set.copyOf(tempSilhouette);
  }

  Building(int id, String name, Color color, Turnable turnable, int[][] form) {
    this(id, name, color, 1, turnable, form);
  }


  private List<Position> turn(Direction direction, Collection<Position> positions) {
    if (turnable != Turnable.No) {
      List<Position> turnedPositions = new ArrayList<>();
      positions.forEach(position -> {
        Position turnedPosition = position;
        for (int turns = 0; turns < direction.getId() % turnable.getNumberOfPossibleForms(); ++turns) {
          turnedPosition = new Position(-turnedPosition.y(), turnedPosition.x());
        }
        turnedPositions.add(turnedPosition);
      });
      return turnedPositions;
    }
    return positions.stream().collect(Collectors.toUnmodifiableList());
  }

  private final Map<Direction, List<Position>> turnedForm = new EnumMap<>(Direction.class);

  /**
   * Gets the form of the building as a list of {@link Position}s  turned in the {@link Direction}.
   * <br><br>
   * See documentation which directions are possible for which enum-value.
   * The building will not be changed by this action.
   *
   * @param direction the {@link Direction} the building form is turned
   * @return the list of {@link Position}s representing the turned form
   */
  public List<Position> turn(Direction direction) {
    return turnedForm.computeIfAbsent(direction, d -> turn(direction, form));
  }

  private final Map<Direction, List<Position>> turnedCorners = new EnumMap<>(Direction.class);

  /**
   * Gets the corners of the building as a list of {@link Position}s
   * turned in the {@link Direction}.
   * <br><br>
   * See documentation which directions are possible for which enum-value.
   * The building will not be changed by this action.
   *
   * @param direction the {@link Direction} the building form is turned
   * @return the list of {@link Position}s representing the corners of the turned form
   */
  public List<Position> corners(Direction direction) {
    return turnedCorners.computeIfAbsent(direction, d -> turn(direction, corners));
  }

  private final Map<Direction, List<Position>> turnedSilhouette = new EnumMap<>(Direction.class);

  /**
   * Gets the silhouette of the building as a list of {@link Position}s
   * turned in the {@link Direction}.
   * <br><br>
   * See documentation which directions are possible for which enum-value.
   * The building will not be changed by this action.
   *
   * @param direction the {@link Direction} the building form is turned
   * @return the list of {@link Position}s representing the silhouette of the turned form
   */
  public List<Position> silhouette(Direction direction) {
    return turnedSilhouette.computeIfAbsent(direction, d -> turn(direction, silhouette));
  }

  /**
   * The score of the building.
   * <br><br>
   * The score of a building is proportional to their size. Each occupied square is a point.
   *
   * @return the score of the building
   */
  public int score() {
    return form.size();
  }

  /**
   * Gets id of the building.
   *
   * @return the id of the building
   */
  public int getId() {
    return id;
  }

  /**
   * Gets name of the building.
   *
   * @return the name of the building
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the {@link Color} of the building.
   *
   * @return the {@link Color} of the building
   */
  public Color getColor() {
    return color;
  }

  /**
   * Gets the {@link Turnable} restriction of the building.
   *
   * @return the {@link Turnable} restriction of the building
   */
  public Turnable getTurnable() {
    return turnable;
  }

  /**
   * Gets the number of this building in a normal game.
   *
   * @return the number of this building in a normal game
   */
  public Integer getNumberInGame() {
    return numberInGame;
  }


  /**
   * Get all possible {@link Placement}s for this building in a cathedral game as a set.
   *
   * @return the set of possible {@link Placement}s
   */
  public Set<Placement> getAllPossiblePlacements(){
    if(this.allPossiblePlacements == null){
      Set<Placement> allPossiblePlacements = new HashSet<>();
      Board board = new Board(this);
      for (int y=0; y<=9; ++y) {
        for (int x=0; x<=9; ++x) {
          for(Direction rotation: turnable.getPossibleDirections()){
            Placement current = new Placement(x,y, rotation, this);
            if(board.copy().placeBuilding(current, true)){
              allPossiblePlacements.add(current);
            }
          }
        }
      }
      this.allPossiblePlacements = Collections.unmodifiableSet(allPossiblePlacements);
    }
    return this.allPossiblePlacements;
  }

  /**
   * Gets the possible {@link Placement}s of this building for the given {@link Board}.
   *
   * @param board the board
   * @return the set of possible {@link Placement}s
   */
  public Set<Placement> getPossiblePlacements(Board board){
    Set<Placement> possiblePlacements = new HashSet<>();
    for(Placement placement: getAllPossiblePlacements()){
      if(board.copy().placeBuilding(placement, true)){
        possiblePlacements.add(placement);
      }
    }
    return possiblePlacements;
  }

  /**
   * Gets the possible {@link Placement}s of this building for the given {@link Game}.
   *
   * @param game the game
   * @return the set of possible {@link Placement}s
   */
  public Set<Placement> getPossiblePlacements(Game game){
    return getPossiblePlacements(game.getBoard());
  }

  /**
   * Returns the building as a string in the form 'id (color) name'.
   *
   * @return the string representing the building
   */
  @Override
  public String toString() {
    return id + " " + name;
  }
}
