package org.rzjaffery.chesspieces;

import org.rzjaffery.chessboard.Piece;
import org.rzjaffery.chessboard.PieceColor;
import org.rzjaffery.utils.Position;

public class Pawn extends Piece {
    public Pawn (PieceColor pieceColor, Position position) {
        super (pieceColor, position);
    }

    @Override
    public boolean isValidMove (Position newPosition, Piece[][] board) {
        int forwardDirection = pieceColor == PieceColor.WHITE ? -1 : 1;
        int rowDiff = (newPosition.getRow () - position.getRow ()) * forwardDirection;
        int colDiff = newPosition.getColumn () - position.getColumn ();

        // Forward move
        if (colDiff == 0 && rowDiff == 1 && board[newPosition.getRow ()][newPosition.getColumn ()] == null)
            return true; // Move forward one square

        // Initial 2 square move
        boolean isStartingPosition = (pieceColor == PieceColor.WHITE && position.getRow () == 6) || (pieceColor == PieceColor.BLACK && position.getRow () == 1);
        if (colDiff == 0 && rowDiff == 2 && isStartingPosition && board[newPosition.getRow ()][newPosition.getColumn ()] == null) {

            // check for the blocking pieces
            int middleRow = position.getRow () + forwardDirection;
            if (board[middleRow][position.getColumn ()] == null) {
                return true; // move forward 2 square
            }
        }

        // Diagonal capture move
        if (Math.abs (colDiff) == 1 && rowDiff == 1 && board[newPosition.getRow ()][newPosition.getColumn ()] != null && board[newPosition.getRow ()][newPosition.getColumn ()].getPieceColor () == this.pieceColor) {
            return true;
        }
        return false;
    }
}
