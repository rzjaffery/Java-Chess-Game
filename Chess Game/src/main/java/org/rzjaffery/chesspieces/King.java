package org.rzjaffery.chesspieces;

import org.rzjaffery.chessboard.Piece;
import org.rzjaffery.chessboard.PieceColor;
import org.rzjaffery.utils.Position;

public class King extends Piece {
    public King (PieceColor pieceColor, Position position) {
        super (pieceColor, position);
    }

    @Override
    public boolean isValidMove (Position newPosition, Piece[][] board) {
        int rowDiff = Math.abs (newPosition.getRow () - position.getRow ());
        int colDiff = Math.abs (newPosition.getColumn () - position.getColumn ());

        // King can move only one square in any direction
        boolean isOneSquareMove = rowDiff <=1 && colDiff<=1 && !(rowDiff==0 && colDiff==0);
        if (!isOneSquareMove){
            return false; // move is not withing one square
        }

        Piece destinationPiece = board[newPosition.getRow ()][newPosition.getColumn ()];
        return destinationPiece == null || destinationPiece.getPieceColor () != this.getPieceColor ();
    }
}
