package org.rzjaffery.chesspieces;

import org.rzjaffery.chessboard.Piece;
import org.rzjaffery.chessboard.PieceColor;
import org.rzjaffery.utils.Position;
public class Bishop extends Piece {
    public Bishop(PieceColor color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board) {
        int rowDiff = Math.abs(position.getRow() - newPosition.getRow());
        int colDiff = Math.abs(position.getColumn() - newPosition.getColumn());

        if (position.equals(newPosition)) {
            return false;
        }

        if (rowDiff != colDiff) {
            return false;
        }

        int rowStep = Integer.compare(newPosition.getRow(), position.getRow());
        int colStep = Integer.compare(newPosition.getColumn(), position.getColumn());

        int currentRow = position.getRow() + rowStep;
        int currentCol = position.getColumn() + colStep;

        while (currentRow != newPosition.getRow() && currentCol != newPosition.getColumn()) {
            if (board[currentRow][currentCol] != null) {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        System.out.println(this.getClass().getSimpleName() + " attempting move to " + newPosition);


        Piece destinationPiece = board[newPosition.getRow()][newPosition.getColumn()];
        if (destinationPiece == null) {
            return true;
        } else if (destinationPiece.getColor() != this.getColor()) {
            return true;
        }

        return false;
    }
}