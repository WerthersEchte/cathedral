# A cathedral implementation in Java
[![License: Unlicense](https://img.shields.io/badge/License-Unlicense-blue.svg)](http://unlicense.org/)
![Build](https://github.com/WerthersEchte/cathedral/actions/workflows/build.yml/badge.svg)
[![Test coverage](.github/badges/jacoco.svg)](https://github.com/WerthersEchte/cathedral/actions/workflows/build.yml)
[![Code Quality](https://github.com/WerthersEchte/cathedral/actions/workflows/codequality.yml/badge.svg)](https://github.com/WerthersEchte/cathedral/actions/workflows/codequality.yml)

## About
This is a Java implementation of the board game cathedral (https://en.wikipedia.org/wiki/Cathedral_(board_game)). It indended use is for developing basic ki and ki adjacent programs. It contains only the needed gamelogic.

## More Information
How to use this and everything else can be found under [https://werthersechte.github.io/cathedral/](https://werthersechte.github.io/cathedral/)

## How to
### get it
Clone the repository
or
Download the and use the released binaries from [https://github.com/WerthersEchte/cathedral/releases](https://github.com/WerthersEchte/cathedral/releases)

### use it
Example creating a new game and placing the Cathedral

    // create a game
    Game game = new Game();

    // Select a building
    // Get possible buildings with game.getPlacableBuildings()
    Building building = Building.Blue_Cathedral;
    // Select a position for the building
    Position position = new Position(3, 5);
    // Select a direction for the building to face
    // Get possible directions the building can turn with building.getTurnable().getPossibleDirections()
    Direction direction = Direction._90;

    // Create a placement from the building, position and rotation
    Placement placement = new Placement(position, direction, building);

    // take the turn with the placement
    game.takeTurn(placement);

    // repeat with other turns...

    // when the game is finished
    // game.isFinished() returns true if no more buildings can be placed
    if(game.isFinished()){
      System.out.println("Game is finished");
    }
