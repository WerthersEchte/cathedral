package de.fhkiel.ki.cathedral.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Class used to emulate a cathedral-board.
 *
 * Contains all successful {@link Placement}s, number of available {@link Building}s and an
 * array repesetiong the regions on the board.
 *
 * @author Eike Petersen {@literal <eike.petersen@fh-kiel.de>}
 * @version 1.0
 * @since 1.0
 */
public class Board {
  private final Map<Building, Integer> freeBuildings = new EnumMap<>(Building.class);
  private final List<Placement> placedBuildings = new ArrayList<>();

  private final Color[][] field = new Color[10][10];

  private int numberOfPlacements = 0;

  /**
   * Instantiates a new Board with all defauld {@link Building}s.
   */
  public Board() {
  this(Building.values());
  }

  /**
   * Instantiates a new Board with the given {@link Building}s.
   *
   * @param buildings the buildings to use on this board
   */
  public Board(Building... buildings) {
    for (Building building : buildings) {
      freeBuildings.put(building, building.getNumberInGame());
    }

    for (int y = 0; y < 10; ++y) {
      for (int x = 0; x < 10; ++x) {
        field[y][x] = Color.None;
      }
    }
  }

  /**
   * Creates a deep-copy of this board.
   *
   * @return the copy of this board
   */
  public Board copy() {
    Board newBoard = new Board( new Building[]{} );

    newBoard.numberOfPlacements = numberOfPlacements;

    newBoard.freeBuildings.putAll(freeBuildings);
    newBoard.placedBuildings.addAll(placedBuildings);

    for (int y = 0; y < 10; ++y) {
      System.arraycopy(field[y], 0, newBoard.field[y], 0, 10);
    }
    return newBoard;
  }

  /**
   * Places a {@link Building} on this board. Returns if successful or not.
   *
   * @param placement the placement with the building to place
   * @return if the building could be placed
   */
  public boolean placeBuilding(Placement placement) {
    return placeBuilding(placement, false);
  }

  /**
   * Places a {@link Building} on this board. Returns if successful or not.
   *
   * Can be set to compute the regions to speed up computation.
   * THIS CAN LEAD TO INCORRECT BOARDSTATES!
   *
   * @param placement the placement with the building to place
   * @param fast      should the regions NOT be computed
   * @return if the building could be placed
   */
  public boolean placeBuilding(Placement placement, boolean fast) {

    if (freeBuildings.getOrDefault(placement.building(), 0) <= 0) {
      return false;
    }

    if (isNotPlaceable(placement)) {
      return false;
    }

    placeColor(placement.form(), placement.building().getColor(), placement.x(),
        placement.y());
    freeBuildings.put(placement.building(),
        freeBuildings.getOrDefault(placement.building(), 1) -
            1); //do not want any negative building number
    placedBuildings.add(placement);

    if (!fast && numberOfPlacements >= 3) {
      int numberOfConnections = 0;
      for(Position corner : placement.building().corners(placement.direction())){
        Position point = placement.position().plus(corner);
        if (!point.isViable() || field[point.y()][point.x()] == placement.building().getColor() || numberOfPlacements == 3){
          numberOfConnections += 1;
          if(numberOfConnections > 1){
            buildRegions();
            break;
          }
        }
      }
    }

    numberOfPlacements += 1;

    return true;
  }

  private Placement lastPlacement = null;
  private Map<Color, Integer> currentScore = null;

  /**
   * Returns a map of the score.
   *
   * There are always scores for the {@link Color#Black} and the {@link Color#White} contained. <br>
   * They range from 47 to 0 and show the aggregate of all unplaced buildings of the chosen color.
   *
   * @return the map with the score for {@link Color#Black} and {@link Color#White}
   */
  public Map<Color, Integer> score() {
    if (currentScore == null ||
        !placedBuildings.isEmpty() && lastPlacement != placedBuildings.get(placedBuildings.size() - 1)) {
      if (!placedBuildings.isEmpty()) {
        lastPlacement = placedBuildings.get(placedBuildings.size() - 1);
      }

      Map<Color, Integer> score = new EnumMap<>(Color.class);
      score.put(Color.Black, 0);
      score.put(Color.White, 0);

      freeBuildings.keySet()
          .stream()
          .filter(building -> score.containsKey(building.getColor()))
          .forEach(building -> score.put(building.getColor(),
              score.get(building.getColor())+building.score() * freeBuildings.get(building)));
      currentScore = score;
    }

    return currentScore;
  }

  /**
   * Gets number of free pieces of the {@link Building}-type.
   *
   * The default would be a number between 0 as minimum to 2 as maximum.
   *
   * @param building the {@link Building}-type
   * @return the number of free pieces of the type
   */
  public int getNumberOfFreeBuildings(Building building) {
    return freeBuildings.getOrDefault(building, 0);
  }

  /**
   * Gets a set of all known {@link Building}s.
   * Should normally be the same {@link Building}s as {@link Building#values()}
   *
   * @return the set of all {@link Building}s
   */
  public Set<Building> getBuildings() {
    return freeBuildings.keySet();
  }

  /**
   * Gets a list of unplaced {@link Building}s for the player {@link Color}.
   *
   * @param player the {@link Color} of the player
   * @return the list of unplaced {@link Building}s
   */
  public List<Building> getPlacableBuildings(Color player) {
    return getBuildings().stream()
        .filter(building -> building.getColor() == player)
        .filter(building -> getNumberOfFreeBuildings(building) > 0)
        .toList();
  }

  /**
   * Gets all currently unplaced {@link Building}s in a list.
   * There are no numbers for {@link Building}s. The buildings can be all valid building {@link Color}s.
   *
   * @return a list of all currently unplaced {@link Building}s
   */
  public List<Building> getAllUnplacedBuildings() {
    return getBuildings().stream()
        .filter(building -> getNumberOfFreeBuildings(building) > 0)
        .toList();
  }


  /**
   * Gets a {@link Map} of all {@link Building}s pointing at a number of how many of these are
   * <b>not</b> placed.
   *
   * E.g. if the cathedral is placed the map would contain the entry cathedral -> 0
   *
   * @return the free buildings
   */
  public Map<Building, Integer> getFreeBuildings() {
    return new EnumMap<>(freeBuildings);
  }

  /**
   * Gets a list of all {@link Placement}s on the board.
   *
   * @return the list of all {@link Placement}s
   */
  public List<Placement> getPlacedBuildings() {
    return new ArrayList<>(placedBuildings);
  }

  /**
   * Get the board as a two-dimensional array of colors.
   * Dimensions are 0-9 in x and y and are build to the format field[y][x] .<br>
   * {@link Color}s range over the full spectrum of possibilities. <br>
   * There are no indicators where {@link Building}s are placed. To get this information, use
   * {@link Board#getPlacedBuildings()}.
   *
   * @return the array representing the board
   */
  public Color[][] getField() {
    return field;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Board board = (Board) o;
    return numberOfPlacements == board.numberOfPlacements && freeBuildings.equals(board.freeBuildings) &&
        placedBuildings.equals(board.placedBuildings) && Arrays.deepEquals(field, board.field);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(freeBuildings, placedBuildings, numberOfPlacements);
    result = 31 * result + Arrays.deepHashCode(field);
    return result;
  }

  @Override
  public String toString() {
    StringJoiner boardAsString = new StringJoiner(", ", Board.class.getSimpleName() + "[", "]")
        .add("\nfreeBuildings=" + freeBuildings)
        .add("\nplacedBuildings=" + placedBuildings)
        .add("\nboard=\n");
    for (int y = 0; y < 10; ++y) {
      boardAsString.add(Arrays.toString(field[y]) + "\n");
    }
    return boardAsString.toString();
  }

  private void buildRegions() {
    Arrays.stream(new Color[] {Color.Black, Color.White}).forEach(color -> {
      int[][] fieldWithoutColor = new int[10][10];
      for (int y = 0; y < 10; ++y) {
        for (int x = 0; x < 10; ++x) {
          if(field[y][x] != color){
            fieldWithoutColor[y][x] = 1;
          }
        }
      }
      int runner = 0;
      while (runner < 100) {
        if(fieldWithoutColor[runner/10][runner%10] == 1) {
          Deque<Position> freeFieldsToLookAt = new LinkedList<>();
          freeFieldsToLookAt.push(new Position(runner%10, runner/10));
          fieldWithoutColor[runner/10][runner%10] = 0;

          List<Position> region = new ArrayList<>();

          while (!freeFieldsToLookAt.isEmpty()) {
            Position currentField = freeFieldsToLookAt.pop();

            region.add(currentField);

            int xMin = -1;
            int xMax = 2;
            for (int y = -1; y < 2; ++y) {
              final int yToLookAt = currentField.y() + y;
              if (yToLookAt < 0 || yToLookAt > 9) {
                continue;
              } else {
                for (int x = xMin; x < xMax; ++x) {
                  if (x != 0 || y != 0) {
                    final int xToLookAt = currentField.x() + x;
                    if (xToLookAt < 0) {
                      ++xMin;
                    } else if (xToLookAt > 9) {
                      --xMax;
                    } else if (fieldWithoutColor[yToLookAt][xToLookAt] == 1) {
                      freeFieldsToLookAt.push(new Position(xToLookAt, yToLookAt));
                      fieldWithoutColor[yToLookAt][xToLookAt] = 0;
                    }
                  }
                }
              }
            }
          }

          List<Placement> enemyBuildingsInRegion = getAllEnemyBuildingsInRegion(region, color);
          if (enemyBuildingsInRegion.size() < 2) {
            enemyBuildingsInRegion.forEach(this::removePlacement);
            placeColor(region, Color.getSubColor(color), 0, 0);
          }
        }
        runner += 1;
      }
    });
  }

  private List<Placement> getAllEnemyBuildingsInRegion(List<Position> region, Color color) {
    return placedBuildings.stream()
        .filter(placement -> region.contains(placement.position()))
        .filter(placement -> placement.building().getColor() != color)
        .collect(Collectors.toList());
  }

  private void removePlacement(Placement placement) {
    placedBuildings.remove(placement);
    freeBuildings.put(placement.building(), freeBuildings.getOrDefault(placement.building(), 0) + 1);
  }

  private void placeColor(List<Position> form, Color color, int x, int y) {
    form.forEach(position -> field[position.y() + y][position.x() + x] = color);
  }

  private boolean isNotPlaceable(Placement placement) {
    return placement.form().stream().anyMatch(position -> {
      Position realPosition = position.plus(placement.position());

      return
          !realPosition.isViable() ||
              !colorIsCompatible(field[realPosition.y()][realPosition.x()], placement.building().getColor());
    });
  }

  private boolean colorIsCompatible(Color onPosition, Color toPlace) {
    return onPosition == Color.None ||
        onPosition == Color.Black_Owned && toPlace == Color.Black ||
        onPosition == Color.White_Owned && toPlace == Color.White;
  }
}
