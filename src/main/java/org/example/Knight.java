package org.example;

import javax.swing.*;

public class Knight extends Piece {
    public Knight(int x, int y, PieceColor pieceColor) {
        super(pieceColor, x, y);
        this.name = "Knight";
        this.shortcut = 'N';
        if (pieceColor == PieceColor.BLACK) {
            this.image = new ImageIcon(Pawn.class.getResource("/black-knight.png")).getImage();
        } else {
            this.image = new ImageIcon(Pawn.class.getResource("/white-knight.png")).getImage();
        }
    }

    @Override
    public boolean isValidMove(int newX, int newY, Board board) {
        Piece p = board.getPiece(newX, newY);

        int directionX = Math.abs(newX - x);
        int directionY = Math.abs(newY - y);

        if (!((directionX == 2 && directionY == 1) || (directionX == 1 && directionY == 2))) {
            return false;
        }
        if (p != null) {
            if (p.getColor() == color) {
                return false;
            }
        }
        return true;
    }
}
