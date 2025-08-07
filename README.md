# Java Chess Game

A fully functional Chess game built using **Java Swing**. The game supports player-vs-player mode, move validation for all chess pieces, and a basic score display for White and Black players.

## Features

- Player vs Player mode
- Legal move validation for all chess pieces:
  - Pawn
  - Rook
  - Knight
  - Bishop
  - Queen
  - King
- Turn-based play (White starts first)
- Piece capturing and scoreboard
- Graphical User Interface using Java Swing
- Score displayed at the top (captures count as score)

## 📷 Screenshots
<img width="641" height="743" alt="image" src="https://github.com/user-attachments/assets/00ddffd8-3000-43c5-85a1-8b51eab590aa" />


## 🛠 Technologies Used

- Java 8+
- Java Swing for GUI
- Object-Oriented Programming (OOP)

## Project Structure

```bash
.
├── src/
│   ├── ChessGame.java         # Main class to launch the game
│   ├── Board.java             # Chessboard setup and logic
│   ├── Piece.java             # Abstract class for chess pieces
│   ├── Pawn.java, Rook.java, etc. # Specific piece logic
│   ├── MoveValidator.java     # Logic to check legal moves
│   └── ScoreManager.java      # Score updating logic
├── README.md
├── screenshot.png

```
## How to Run
1 - Clone the repository \
 - git clone **https://github.com/rzjaffery/Java-Chess-Game.git** \
 - cd java-chess-game
2 - Compile and run the game
```
javac ChessGame.java
java ChessGame
```
## Known Issues / To-Do
- No AI or multiplayer over network
