![pipeline](https://gitlab.oit.duke.edu/ld170/risc-project1/badges/master/pipeline.svg)
![coverage](https://gitlab.oit.duke.edu/ld170/risc-project1/badges/master/coverage.svg?job=test_stable)


ECE 651: RISC Evolution 1
======================================

Final commit ID: 805837b8

Hosting server can be done with './gradlew run-server' in highest level of repository. This will open server on the machine with the default post of 12345.

Opening a new emacs window and running client can be done with './gradlew run-client' in the highest level of the repository. This will then prompt the user to enter the connection address (localhost will work), prompt to enter the port (12345), and attempt to connect the server. You can repeat these steps to create additional clients (up to 5).

The server waits 2.5 minutes for players to connect before starting the game or timing out.

Between each turn, the server waits 3 minutes to receive a valid turn from each player. After 3 minutes, if a player does not have a valid turn entered, they will be kicked out of the game. If only one player remains, they will automatically win after playing their turn.

Validation of placements and moves are happening on the server side, meaning if a player enters multiple orders (attack and move) in a turn, but one order is considered invalid due to project constraints, then the player is reprompted to enter all moves for that turn. Some minimum validation is done on the client side regarding players entering things that are not numbers for units or selecting regions that do not exist.

When attacking or moving, at least one unit must be left behind in your region (i.e. you cannot abandon a region).

UML diagrams can be found in top level of repository or on OneNote.

## Coverage
[Detailed coverage](https://ld170.pages.oit.duke.edu/risc-project1/dashboard.html)


