package org.example;

import javax.swing.*;

public class Queen extends Piece {
    public Queen(int x, int y, PieceColor pieceColor) {
        super(pieceColor, x, y);
        this.name = "Queen";
        this.shortcut = 'Q';
        if (pieceColor == PieceColor.BLACK) {
            this.image = new ImageIcon(Pawn.class.getResource("/black-queen.png")).getImage();
        } else {
            this.image = new ImageIcon(Pawn.class.getResource("/white-queen.png")).getImage();
        }
    }
    @Override
    public boolean isValidMove(int newX, int newY, Board board) {
        Piece p = board.getPiece(newX, newY);

        if (!verticalMovement(newX, newY, board) && !diagonalMovement(newX, newY, board)){
            return false;
        }
        if (p != null) {
            if (p.getColor() == color) {
                return false;
            }
        }
        return true;
    }

    private boolean diagonalMovement(int newX, int newY, Board board) {
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

        return true;
    }

    private boolean verticalMovement(int newX, int newY, Board board) {
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
        return true;
    }
}
