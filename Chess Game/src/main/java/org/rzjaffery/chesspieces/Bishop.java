package org.rzjaffery.chesspieces;

import org.rzjaffery.chessboard.Piece;
import org.rzjaffery.chessboard.PieceColor;
import org.rzjaffery.utils.Position;

public class Bishop extends Piece {
    public Bishop (PieceColor pieceColor, Position position) {
        super (pieceColor, position);
    }

    @Override
    public boolean isValidMove (Position newPosition, Piece[][] board) {
        int rowDiff = Math.abs (position.getRow () - newPosition.getRow ());
        int colDiff = Math.abs (position.getColumn () - newPosition.getColumn ());

        if (rowDiff != colDiff) {
            return false; // move is not diagonal
        }

        int rowStep = newPosition.getRow () > position.getRow () ? 1 : -1;
        int colStep = newPosition.getColumn () > position.getColumn () ? 1 : -1;
        int step = rowDiff - 1; // Number of squares to check for obstruction

        // check for the obstruction along the path
        for (int i = 0; i < step; i++) {
            if (board[position.getRow () + i * rowStep][position.getColumn () + i * colStep] != null) {
                return false; // there is a piece in the way
            }
        }

        Piece destinationPiece = board[newPosition.getRow ()][newPosition.getColumn ()];
        if (destinationPiece == null) {
            return true; // the destination is empty but the move is valid
        } else if (destinationPiece.getPieceColor () != this.getPieceColor ()) {
            return true; // the destination has the piece, capture is valid
        }
        return false; // the destination has the piece of same color, move is valid
    }
}
