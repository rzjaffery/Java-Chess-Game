package org.rzjaffery.chesspieces;

import org.rzjaffery.chessboard.Piece;
import org.rzjaffery.chessboard.PieceColor;
import org.rzjaffery.utils.Position;

public class Queen extends Piece {
    public Queen (PieceColor pieceColor, Position position) {
        super (pieceColor, position);
    }

    @Override
    public boolean isValidMove (Position newPosition, Piece[][] board) {
        // check for the piece in the same position
        if (newPosition.equals (this.position)) {
            return false;
        }
        int rowDiff = Math.abs (newPosition.getRow () - this.position.getRow ());
        int colDiff = Math.abs (newPosition.getColumn () - this.position.getColumn ());

        //check for the straight line movement
        boolean straightLine = this.position.getRow () == newPosition.getRow () || this.position.getColumn () == newPosition.getColumn ();

        // check for the diagonal
        boolean diagonalLine = rowDiff == colDiff;

        if (!straightLine && !diagonalLine) {
            return false; // neither straight nor diagonal
        }

        // calculate the direction of movement
        int rowDirection = Integer.compare (newPosition.getRow (), this.position.getRow ());
        int colDirection = Integer.compare (newPosition.getColumn (), this.position.getColumn ());

        // check for any pieces in the path
        int currentRow = this.position.getRow () + rowDirection;
        int currentCol = this.position.getColumn () + colDirection;

        while (currentRow != newPosition.getRow () || currentCol != newPosition.getColumn ()) {
            if (board[currentRow][currentCol] != null) {
                return false; // path is blocked
            }
            currentRow += rowDirection;
            currentCol += colDirection;
        }
        Piece destinationPiece = board[newPosition.getRow ()][newPosition.getColumn ()];
        return destinationPiece == null || destinationPiece.getPieceColor () == this.getPieceColor ();

    }
}
