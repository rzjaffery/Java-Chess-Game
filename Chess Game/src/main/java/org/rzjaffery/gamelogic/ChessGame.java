package org.rzjaffery.gamelogic;

import org.rzjaffery.chessboard.Chessboard;
import org.rzjaffery.chessboard.Piece;
import org.rzjaffery.chessboard.PieceColor;
import org.rzjaffery.chesspieces.King;
import org.rzjaffery.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class ChessGame {
    private Chessboard board;
    private boolean whiteTurn = true;
    private Position selectedPosition;

    private int whiteScore = 0;
    private int blackScore = 0;
    private ScoreUpdateListener scoreListener; // We'll use this to notify the UI


    public ChessGame () {
        this.board = new Chessboard ();
    }

    // displays the board to user
    public Chessboard getBoard () {
        return this.board;
    }

    // reset all the game properties
    public void resetGame () {
        this.board = new Chessboard (); // Re-initialize the board
        this.whiteTurn = true; // Reset turn to white
    }

    public PieceColor getCurrentPlayerColor () {
        return whiteTurn ? PieceColor.WHITE : PieceColor.BLACK;
    }

    public boolean isPieceSelected () {
        return selectedPosition != null;
    }
    public interface ScoreUpdateListener {
        void onScoreUpdated(int whiteScore, int blackScore);
    }
    public void setScoreUpdateListener(ScoreUpdateListener listener) {
        this.scoreListener = listener;
    }



    // used for selecting the square which the user clicks
    public boolean handleSquareSelection (int row, int col) {
        if (selectedPosition == null) {

            // Attempt to select a piece
            Piece selectedPiece = board.getPiece (row, col);
            if (selectedPiece != null && selectedPiece.getColor () == (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
                selectedPosition = new Position (row, col);
                return false; // Indicate a piece was selected but not moved

            }
        } else {

            // Attempt to move the selected piece
            boolean moveMade = makeMove (selectedPosition, new Position (row, col));
            selectedPosition = null; // Reset the selection
            return moveMade; // Return true if the move made was successful
        }
        return false; // Return false if no move was made or piece was not selected
    }

    public boolean makeMove(Position start, Position end) {
        Piece movingPiece = board.getPiece(start.getRow(), start.getColumn());
        Piece targetPiece = board.getPiece(end.getRow(), end.getColumn());

        if (movingPiece == null || movingPiece.getColor() != (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
            return false;
        }

        if (movingPiece.isValidMove(end, board.getBoard())) {
            // Check for capture
            if (targetPiece != null && targetPiece.getColor() != movingPiece.getColor()) {
                if (targetPiece.getColor() == PieceColor.WHITE) {
                    blackScore++;
                } else {
                    whiteScore++;
                }

                // Notify listener (UI) about the updated score
                if (scoreListener != null) {
                    scoreListener.onScoreUpdated(whiteScore, blackScore);
                }
            }

            board.movePiece(start, end);
            whiteTurn = !whiteTurn;
            return true;
        }
        return false;
    }


    // made to verify the move is a check move
    public boolean isInCheck (PieceColor kingColor) {
        Position kingPosition = findKingPosition (kingColor);
        for (int row = 0; row < board.getBoard ().length; row++) {
            for (int col = 0; col < board.getBoard ()[row].length; col++) {
                Piece piece = board.getPiece (row, col);
                if (piece != null && piece.getColor () != kingColor) {
                    if (piece.isValidMove (kingPosition, board.getBoard ())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // made to look for the position of the king
    private Position findKingPosition (PieceColor kingColor) {
        for (int row = 0; row < board.getBoard ().length; row++) {
            for (int col = 0; col < board.getBoard ()[row].length; col++) {
                Piece piece = board.getPiece (row, col);
                if (piece instanceof King && piece.getColor () == kingColor) {
                    return new Position (row, col);
                }
            }
        }
        throw new RuntimeException ("King not found, which can never happen");
    }

    // made to verify the checkmate move
    public boolean isCheckmate (PieceColor kingColor) {
        if (!isInCheck (kingColor)) {
            return false; // not in check therefore cannot be checkmate
        }
        Position kingPosition = findKingPosition (kingColor);
        King king = (King) board.getPiece (kingPosition.getRow (), kingPosition.getColumn ());

        // attempt to make the king out of check
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {
                if (rowOffset == 0 && colOffset == 0) {
                    continue; // skip the current position of the king
                }

                // check if moving the king to the new position is a legal move and does not
                Position newPosition = new Position (kingPosition.getRow () + rowOffset, kingPosition.getColumn () + colOffset);

                // result in check
                if ((isPositionOnBoard (newPosition) && king.isValidMove (newPosition, board.getBoard ()))
                        && !wouldBeInCheckAfterMove (kingColor, kingPosition, newPosition)) {
                    return false; // found a way to make the king out of check
                }
            }
        }
        return true; // no legal moves available to escape from the check therefore CHECKMATE
    }

    // made to check if the move is on the board or out of board
    private boolean isPositionOnBoard (Position position) {
        return (position.getRow () >= 0 && position.getRow () < board.getBoard ().length)
                && (position.getColumn () >= 0 && position.getColumn () < board.getBoard ()[0].length);
    }


    // made to check if player's being checked before executing on the main board
    private boolean wouldBeInCheckAfterMove (PieceColor kingColor, Position from, Position to) {
        // Simulate the move temporarily
        Piece temp = board.getPiece (to.getRow (), to.getColumn ());
        board.setPiece (to.getRow (), to.getColumn (), board.getPiece (from.getRow (), from.getColumn ()));
        board.setPiece (from.getRow (), from.getColumn (), null);

        boolean inCheck = isInCheck (kingColor);

        // Undo the move
        board.setPiece (from.getRow (), from.getColumn (), board.getPiece (to.getRow (), to.getColumn ()));
        board.setPiece (to.getRow (), to.getColumn (), temp);

        return inCheck;
    }


    public List<Position> getLegalMovesForPieceAt (Position position) {
        Piece selectedPiece = board.getPiece (position.getRow (), position.getColumn ());
        if (selectedPiece == null) {
            return new ArrayList<> (); // there is no move selected for the piece
        }

        // Making a list and storing all the legal moves in chess
        List<Position> legalMoves = new ArrayList<> ();

        // Switch statement for better clarification of the moves
        switch (selectedPiece.getClass ().getSimpleName ()) {

            case "Pawn":
                addPawnMoves (position, selectedPiece.getColor (), legalMoves);
                break;
            case "Rook":
                addLineMoves (position, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}, legalMoves);
                break;
            case "Knight":
                addSingleMoves (position, new int[][]{{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}}, legalMoves);
                break;
            case "Bishop":
                addLineMoves (position, new int[][]{{1, 1}, {-1, -1}, {1, -1}, {-1, 1}}, legalMoves);
                break;
            case "Queen":
                addLineMoves (position, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}}, legalMoves);
                break;
            case "King":
                addSingleMoves (position, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}}, legalMoves);
                break;
        }
        return legalMoves;
    }

    // this method is crucial for the movement in straight or diagonal line
    private void addLineMoves (Position position, int[][] directions, List<Position> legalMoves) {
        for (int[] d : directions) {
            Position newPos = new Position (position.getRow () + d[0], position.getColumn () + d[1]);
            while (isPositionOnBoard (newPos)) {
                if (board.getPiece (newPos.getRow (), newPos.getColumn ()) == null) {
                    legalMoves.add (new Position (newPos.getRow (), newPos.getColumn ()));
                    newPos = new Position (newPos.getRow () + d[0], newPos.getColumn () + d[1]);
                } else {
                    if (board.getPiece (newPos.getRow (), newPos.getColumn ()).getColor ()
                            != board.getPiece (position.getRow (), position.getColumn ()).getColor ()) {
                        legalMoves.add (newPos);
                    }
                    break;
                }
            }
        }
    }

    // This handles movement for pieces such as knight and king as they move on certain positions
    private void addSingleMoves (Position position, int[][] moves, List<Position> legalMoves) {
        for (int[] move : moves) {
            Position newPos = new Position (position.getRow () + move[0], position.getColumn () + move[1]);
            if (isPositionOnBoard (newPos) && (board.getPiece (newPos.getRow (), newPos.getColumn ()) == null ||
                    board.getPiece (newPos.getRow (), newPos.getColumn ()).getColor () != board
                            .getPiece (position.getRow (), position.getColumn ()).getColor ())) {
                legalMoves.add (newPos);
            }
        }
    }

    private void addPawnMoves (Position position, PieceColor color, List<Position> legalMoves) {
        int direction = color == PieceColor.WHITE ? -1 : 1;

        // Standard single move
        Position newPos = new Position (position.getRow () + direction, position.getColumn ());
        if (isPositionOnBoard (newPos) && board.getPiece (newPos.getRow (), newPos.getColumn ()) == null) {
            legalMoves.add (newPos);
        }

        // Double move from starting position
        if ((color == PieceColor.WHITE && position.getRow () == 6)
                || (color == PieceColor.BLACK && position.getRow () == 1)) {
            newPos = new Position (position.getRow () + 2 * direction, position.getColumn ());
            Position intermediatePos = new Position (position.getRow () + direction, position.getColumn ());
            if (isPositionOnBoard (newPos) && board.getPiece (newPos.getRow (), newPos.getColumn ()) == null
                    && board.getPiece (intermediatePos.getRow (), intermediatePos.getColumn ()) == null) {
                legalMoves.add (newPos);
            }
        }

        // This is for diagonal captures
        int[] captureCols = {position.getColumn () - 1, position.getColumn () + 1};
        for (int col : captureCols) {
            newPos = new Position (position.getRow () + direction, col);
            if (isPositionOnBoard (newPos) && board.getPiece (newPos.getRow (), newPos.getColumn ()) != null &&
                    board.getPiece (newPos.getRow (), newPos.getColumn ()).getColor () != color) {
                legalMoves.add (newPos);
            }
        }
    }
}


