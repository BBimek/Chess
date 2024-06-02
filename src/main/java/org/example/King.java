package org.example;

import javax.swing.*;

public class King extends Piece {
    public King(int x, int y, PieceColor pieceColor) {
        super(pieceColor, x, y);
        this.name = "King";
        this.shortcut = 'K';
        if (pieceColor == PieceColor.BLACK) {
            this.image = new ImageIcon(Pawn.class.getResource("/black-king.png")).getImage();
        } else {
            this.image = new ImageIcon(Pawn.class.getResource("/white-king.png")).getImage();
        }
    }
    @Override
    public boolean isValidMove(int newX, int newY, Board board) {
        return false;
    }
}
