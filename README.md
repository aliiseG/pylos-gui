# About the project
Pylos is a board game for two players, where each player has 15 spheres. During the game each player makes their move on the given board, creating a pyramid - winner is the one who's sphere is on the top. 

There are three types of possible moves - placing a single sphere on any free spot on the board, removing one or two spheres from the board if the player has managed to place a square of their own spheres, move a sphere that was already placed on the board on top of a square (colors don't matter).
By removing or moving existing spheres the player has a bigger chance of winning.
<p align="center"><img width="900" height="595" alt="pylos board game image" src="https://github.com/user-attachments/assets/8ebdf465-a022-497a-8da9-bc126065fdb1"/><br><a href="https://boardgamegeek.com/image/59864/pylos">image source</a></p>


Currently the game can be played by two players over the same network. The goal of the project was to better understand OOP principles in Java. 
# Built with
* Java
* JavaFX (GUI)
* Java Sockets (newtork connection TCP/IP)
* CSS

# Installation
1. Clone repository https://github.com/aliiseG/pylos-gui.git
2. Run GameServer
3. Run two instances of GameClient (two windows or two people connected to the same network)

# To-do list
Currently the game can be played by two players using written input. List of things that have been implemented and that are planned to be:
- <s>player specific output (sphere color, count, turn messages, error outputs)</s>
- <s>buttons have functions (exit, resert, place new, remove)</s>
- <s>players can place new spheres</s>
- <s>players can remove existing spheres if conditions are met</s>
- <s>show possible moves on top of a stack of spheres</s>
- make moves based on mouse click on the board (not by writing input) (!)
- update winning output / add option to start new game or exit
- testing

# Screenshots from the game
<img  width="1493" height="592" alt="image" src="https://github.com/user-attachments/assets/3ff28a9d-b96c-4355-a4ac-e4f37ad45e46"/>
