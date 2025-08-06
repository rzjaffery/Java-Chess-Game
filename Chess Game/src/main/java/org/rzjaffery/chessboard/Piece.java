package org.rzjaffery.chessboard;


import org.rzjaffery.utils.Position;

public abstract class Piece {
    protected Position position;
    protected PieceColor pieceColor;

    public Piece(PieceColor pieceColor, Position position){
        this.pieceColor = pieceColor;
        this.position=position;
    }

    public PieceColor getPieceColor () {
        return pieceColor;
    }

    public void setPieceColor (PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public Position getPosition () {
        return position;
    }

    public void setPosition (Position position) {
        this.position = position;
    }
    public abstract boolean isValidMove(Position newPosition, Piece[][] board);
}
