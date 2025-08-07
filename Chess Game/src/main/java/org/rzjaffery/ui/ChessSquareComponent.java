package org.rzjaffery.ui;

import javax.swing.*;
import java.awt.*;

public class ChessSquareComponent extends JButton {
    private final int row;
    private final int col;

    public ChessSquareComponent (int row, int col) {
        this.row = row;
        this.col = col;
        initButton ();
    }

    private void initButton () {
        // set same button size for uniformity
        setPreferredSize (new Dimension (64, 64));

        // set background color for checkerboard effect
        if ((row + col) % 2 == 0) {
            setBackground (Color.LIGHT_GRAY);
        } else {
            setBackground (new Color (205, 133, 63));
        }
        // ensures the chess pieces are right in center
        setHorizontalAlignment (SwingConstants.CENTER);
        setVerticalAlignment (SwingConstants.CENTER);
        // font setting can be adjusted for visual enhancement
        setFont (new Font ("Serif", Font.BOLD, 36));
    }

    public void setPieceModel (String symbol, Color color) {
        this.setText (symbol);
        this.setForeground (color);
    }

    public void clearPieceSymbol () {
        this.setText ("");
    }

    public void setPieceImage(ImageIcon icon) {
        this.setIcon(icon);
    }
    public void clearPieceImage() {
        this.setIcon(null);
    }

}
