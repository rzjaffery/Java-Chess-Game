package org.rzjaffery.ui;

import org.rzjaffery.chessboard.Chessboard;
import org.rzjaffery.chessboard.Piece;
import org.rzjaffery.chessboard.PieceColor;
import org.rzjaffery.chesspieces.*;
import org.rzjaffery.gamelogic.ChessGame;
import org.rzjaffery.utils.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This is main display screen which will show the main chess board

public class ChessGameGUI extends JFrame {
    private JLabel scoreLabel;


    // used to display the square on the board
    private final ChessSquareComponent[][] squares = new ChessSquareComponent[8][8];

    // this manages the game logic
    private final ChessGame game = new ChessGame ();


    // map created to represent the Unicode of the chess pieces
    private final Map<Class<? extends Piece>, String> pieceUnicodeMap = new HashMap<>() {
        {
            put(Pawn.class, "\u265F");
            put(Rook.class, "\u265C");
            put(Knight.class, "\u265E");
            put(Bishop.class, "\u265D");
            put(Queen.class, "\u265B");
            put(King.class, "\u265A");
        }
    };

    // constructor
    public ChessGameGUI() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout ());
        initializeTopPanel ();
        initializeBoardPanel ();
        addGameResetOption();

        pack();
        setLocationRelativeTo (null);
        setVisible(true);
    }




    // Used for setting up the scoreboard
    private void initializeTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // or use new BorderLayout()
        scoreLabel = new JLabel("Score - White: 0 | Black: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(scoreLabel);

        add(topPanel, BorderLayout.NORTH); // Add this panel to the top of the main frame
    }

    // used for setting up the board
    private void initializeBoardPanel() {
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                game.setScoreUpdateListener(new ChessGame.ScoreUpdateListener() {
                    @Override
                    public void onScoreUpdated(int whiteScore, int blackScore) {
                        scoreLabel.setText("Score - White: " + whiteScore + " | Black: " + blackScore);
                    }
                });
                final int finalRow = row;
                final int finalCol = col;
                ChessSquareComponent square = new ChessSquareComponent(row, col);
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(finalRow, finalCol);
                    }
                });
                boardPanel.add(square);
                squares[row][col] = square;
            }
        }
        add(boardPanel, BorderLayout.CENTER); // Center of window
        refreshBoard();
    }

    private void refreshBoard() {
        Chessboard board = game.getBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    // If using Unicode symbols:
                    String pieceName = piece.getClass().getSimpleName().toLowerCase(); // e.g., "pawn"
                    String colorName = piece.getColor() == PieceColor.WHITE ? "white" : "black";
                    String imagePath = "/images/" + pieceName + "_" + colorName + ".png";

                    URL imageUrl = getClass().getResource(imagePath);
                    if (imageUrl != null) {
                        ImageIcon icon = new ImageIcon(imageUrl);
                        squares[row][col].setPieceImage(icon);
                    } else {
                        System.err.println("Image not found for: " + imagePath);
                        squares[row][col].clearPieceImage(); // Optional fallback
                    }

                } else {
                    squares[row][col].clearPieceImage();
                }
            }
        }
    }

    private void handleSquareClick(int row, int col) {
        boolean moveResult = game.handleSquareSelection(row, col);
        clearHighlights();
        if (moveResult) {
            refreshBoard();
            checkGameState();
            checkGameOver();
        } else if (game.isPieceSelected()) {
            highlightLegalMoves(new Position(row, col));
        }
        refreshBoard();
    }

    private void checkGameState () {
        PieceColor currentPlayer = game.getCurrentPlayerColor ();
        boolean inCheck = game.isInCheck (currentPlayer);

        if (inCheck) {
            JOptionPane.showMessageDialog (this, currentPlayer + " is in check!");
        }
    }

    private void highlightLegalMoves (Position position) {
        List<Position> legalMoves = game.getLegalMovesForPieceAt (position);
        for (Position move : legalMoves) {
            squares[move.getRow ()][move.getColumn ()].setBackground (Color.GREEN);
        }
    }

    private void clearHighlights () {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setBackground ((row + col) % 2 == 0 ? Color.LIGHT_GRAY : new Color (205, 133, 63));
            }
        }
    }
    

    // added a menu with reset option
    private void addGameResetOption () {
        JMenuBar menuBar = new JMenuBar ();
        JMenu gameMenu = new JMenu ("Game");
        JMenuItem resetItem = new JMenuItem ("Reset");

        resetItem.addActionListener (e -> resetGame ());
        gameMenu.add (resetItem);
        menuBar.add (gameMenu);
        setJMenuBar (menuBar);
    }

    // defined this method to reset the game
    private void resetGame () {
        game.resetGame ();
        refreshBoard ();
    }

    private void checkGameOver () {

        // checkmate condition to quit the game
        if (game.isCheckmate (game.getCurrentPlayerColor ())) {
            // dialog box displaying the information for the user
            int response = JOptionPane.showConfirmDialog (this, "Checkmate! Would you like to play again?", "Game Over", JOptionPane.YES_NO_OPTION);

            // the game reset if the user selects YES
            if (response == JOptionPane.YES_OPTION){
                resetGame ();
            }
            else {
                System.exit (0); // else system exits
            }
        }
    }

    public static void main (String[] args) {
        SwingUtilities.invokeLater (ChessGameGUI::new);
    }

}
