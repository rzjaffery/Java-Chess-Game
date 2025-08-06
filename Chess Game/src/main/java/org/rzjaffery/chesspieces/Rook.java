package org.rzjaffery.chesspieces;

import org.rzjaffery.chessboard.Piece;
import org.rzjaffery.chessboard.PieceColor;
import org.rzjaffery.utils.Position;

public class Rook extends Piece {
    public Rook (PieceColor pieceColor, Position position) {
        super (pieceColor, position);
    }

    @Override
    public boolean isValidMove (Position newPosition, Piece[][] board) {
        // Rooks can move both vertically, and horizontally
        // They cannot jump over the pieces

        if (position.getRow () == newPosition.getRow ()) {
            int columnStart = Math.min (position.getColumn (), newPosition.getColumn ()) + 1;
            int columnEnd = Math.max (position.getColumn (), newPosition.getColumn ());

            for (int column = columnStart; column < columnEnd; column++) {
                if (board[position.getRow ()][column] != null)
                    return false; // there is a piece in the way
            }

        } else if (position.getColumn () == newPosition.getColumn ()) {
            int rowStart = Math.min (position.getRow (), newPosition.getRow ()) + 1;
            int rowEnd = Math.max (position.getRow (), newPosition.getRow ());

            for (int row = rowStart; row < rowEnd; row++) {
                if (board[row][position.getColumn ()] != null)
                    return false;
            }
        } else {
            return false;
        }

        Piece destinationPiece = board[newPosition.getRow ()][newPosition.getColumn ()];
        if (destinationPiece == null)
            return true;
        else if (destinationPiece.getPieceColor () != this.pieceColor) {
            return true;
        }

        return false;
    }
}
