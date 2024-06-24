package org.example;

import javax.swing.*;
import java.awt.*;

public class Square extends JPanel {
    private int row;
    private int col;
    private Piece piece;
    private boolean highlight;

    public Square(int row, int col) {
        this.row = row;
        this.col = col;
        this.piece = null;
        this.highlight = false;
        this.setPreferredSize(new Dimension(100, 100));
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        repaint();
    }

    public Piece getPiece() {
        return piece;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if ((row + col) % 2 == 0) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(new Color(105, 146, 62));
        }
        g.fillRect(0, 0, getWidth(), getHeight());

        if (piece != null && piece.getImage() != null) {
            g.drawImage(piece.getImage(), 0, 0, getWidth(), getHeight(), this);
        }

        if (highlight) {
            g.setColor(new Color(0, 255, 0, 128));
            g.fillOval(getWidth() / 2 - 15, getHeight() / 2 - 15, 30, 30);
        }
    }
}
