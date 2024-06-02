package org.example;


import javax.swing.*;

public class Rook extends Piece {

    public Rook(int x, int y, PieceColor pieceColor) {
        super(pieceColor, x, y);
        this.name = "Rook";
        this.shortcut = 'R';
        if (pieceColor == PieceColor.BLACK) {
            this.image = new ImageIcon(Pawn.class.getResource("/black-rook.png")).getImage();
        } else {
            this.image = new ImageIcon(Pawn.class.getResource("/white-rook.png")).getImage();
        }
    }
    @Override
    public boolean isValidMove(int newX, int newY, Board board) {
        Piece p = board.getPiece(newX, newY);
        if (newX != x && newY != y) {
            return false;
        }

        int directionX = Integer.compare(newX, x);
        int directionY = Integer.compare(newY, y);

        int currentX = x + directionX;
        int currentY = y + directionY;

        while (currentX != newX || currentY != newY) {
            if (board.getPiece(currentX, currentY) != null) {
                return false;
            }
            currentX += directionX;
            currentY += directionY;
        }
        if (p != null) {
            if (p.getColor() == color) {
                return false;
            }
        }
        return true;
    }
}
