package org.rzjaffery.gamelogic;

import org.rzjaffery.chessboard.Chessboard;
import org.rzjaffery.chessboard.Piece;
import org.rzjaffery.chessboard.PieceColor;
import org.rzjaffery.utils.Position;

public class ChessGame {
    private Chessboard board;
    private boolean whiteTurn = true;

    public ChessGame () {
        this.board = new Chessboard ();
    }


    public boolean makeMove (Position start, Position end) {
        Piece movingPiece = board.getPiece (start.getRow (), start.getColumn ());
        if (movingPiece == null || movingPiece.getPieceColor () != (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
            return false;
        }
        if (movingPiece.isValidMove (end, board.getBoard ())) {
            board.movePiece (start, end);
            whiteTurn = !whiteTurn;
            return true;
        }
        return false;
    }
}
