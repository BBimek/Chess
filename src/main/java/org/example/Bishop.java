package org.example;

import javax.swing.*;

public class Bishop extends Piece {
    public Bishop(int x, int y, PieceColor pieceColor) {
        super(pieceColor, x, y);
        this.name = "Bishop";
        this.shortcut = 'B';
        if (pieceColor == PieceColor.BLACK) {
            this.image = new ImageIcon(Pawn.class.getResource("/black-bishop.png")).getImage();
        } else {
            this.image = new ImageIcon(Pawn.class.getResource("/white-bishop.png")).getImage();
        }
    }

    @Override
    public boolean isValidMove(int newX, int newY, Board board) {
        Piece p = board.getPiece(newX, newY);

        int movesX = newX - x;
        int movesY = newY - y;

        if (Math.abs(movesX) != Math.abs(movesY)) {
            return false;
        }
        int directionX = movesX > 0 ? 1 : -1;
        int directionY = movesY > 0 ? 1 : -1;
        int currentX = x + directionX;
        int currentY = y + directionY;

        while (currentX != newX && currentY != newY) {
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
