Feature: Save and Load
  A user wants to save a gamestate in a File and load this gamestate at any later date.

  Scenario: Save an empty or new Game
    Given an empty or new Game
    When it is saved as "NewGame.cathedral"
    Then there should be a file with the name "NewGame.cathedral"
    And it should be empty

  Scenario: Load an empty/new Game
    Given an empty or new Game
    And it has been saved as "NewGame.cathedral"
    When the file with the name "NewGame.cathedral" is loaded
    Then there should be a game
    And it should contain no turns
    And it should be equal to the given game

  Scenario: Save a Game with 10 turns
    Given a Game with 10 turns
    When it is saved as "10Turns.cathedral"
    Then there should be a file with the name "10Turns.cathedral"
    And it should contain the turns in shorthand

  Scenario: Load a Game with 10 turns
    Given a Game with 10 turns
    And it has been saved as "10Turns.cathedral"
    When the file with the name "10Turns.cathedral" is loaded
    Then there should be a game
    And it should contain 10 turns
    And it should be equal to the given game

  Scenario: Save a finished Game
    Given a finished Game
    When it is saved as "Finished.cathedral"
    Then there should be a file with the name "Finished.cathedral"
    And it should contain the turns in shorthand

  Scenario: Load a finished Game
    Given a finished Game
    And it has been saved as "Finished.cathedral"
    When the file with the name "Finished.cathedral" is loaded
    Then there should be a game
    And it should contain the turns
    And it should be finished
    And it should be equal to the given game


