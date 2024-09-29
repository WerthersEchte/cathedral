Feature: Save and Load
  A user wants to save a gamestate in a File and load this gamestate at any later date.

  Scenario: Save an empty or new Game
    Given an empty or new Game
    When it is saved as "NewGame.cathedral"
    Then there exists a file with the name "NewGame.cathedral"
    And it is empty

  Scenario: Load an empty/new Game
    Given an empty or new Game
    And it has been saved as "NewGame.cathedral"
    When the file with the name "NewGame.cathedral" gets loaded
    Then there is a game
    And it contains no turns
    And it is equal to the given game

  Scenario: Save a Game with 10 turns
    Given a Game with 10 turns
    When it is saved as "10Turns.cathedral"
    Then there exists a file with the name "10Turns.cathedral"
    And it contains the turns in shorthand

  Scenario: Load a Game with 10 turns
    Given a Game with 10 turns
    And it has been saved as "10Turns.cathedral"
    When the file with the name "10Turns.cathedral" gets loaded
    Then there is a game
    And it contains 10 turns
    And it is equal to the given game

  Scenario: Save a finished Game
    Given a finished Game
    When it is saved as "Finished.cathedral"
    Then there exists a file with the name "Finished.cathedral"
    And it contains the turns in shorthand

  Scenario: Load a finished Game
    Given a finished Game
    And it has been saved as "Finished.cathedral"
    When the file with the name "Finished.cathedral" gets loaded
    Then there is a game
    And it contains the turns
    And it is finished
    And it is equal to the given game
