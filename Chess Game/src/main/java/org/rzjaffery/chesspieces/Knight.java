package org.rzjaffery.chesspieces;

import org.rzjaffery.chessboard.Piece;
import org.rzjaffery.chessboard.PieceColor;
import org.rzjaffery.utils.Position;

public class Knight extends Piece {
    public Knight (PieceColor pieceColor, Position position) {
        super (pieceColor, position);
    }

    @Override
    public boolean isValidMove (Position newPosition, Piece[][] board) {

        if (newPosition.equals (this.position))
            return false; // cannot move to the same position

        int rowDiff = Math.abs (this.position.getRow () - newPosition.getRow ());
        int colDiff = Math.abs (this.position.getColumn () - newPosition.getColumn ());

        // check for 'L' shaped move pattern
        boolean isValidMove = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);

        if (!isValidMove)
            return false; // Not a valid Knight move

        Piece targetPiece = board[newPosition.getRow ()][newPosition.getColumn ()];

        if (targetPiece == null) {
            return true; // the square is empty therefore move is valid
        } else {
            // can capture opponent's piece if change of color
            return targetPiece.getPieceColor () != this.getPieceColor ();
        }
    }
}
