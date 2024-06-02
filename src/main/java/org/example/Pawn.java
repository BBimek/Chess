package org.example;

import javax.swing.*;

public class Pawn extends Piece {
    private int direction;
    private boolean firstMove;

    public Pawn(int x, int y, PieceColor pieceColor) {
        super(pieceColor, x, y);
        this.name = "Pawn";
        this.shortcut = ' ';
        if (pieceColor == PieceColor.BLACK) {
            this.image = new ImageIcon(Pawn.class.getResource("/black-pawn.png")).getImage();
            this.direction = 1;
            this.firstMove = true;
        } else {
            this.image = new ImageIcon(Pawn.class.getResource("/white-pawn.png")).getImage();
            this.direction = -1;
            this.firstMove = true;
        }
    }

    @Override
    public boolean isValidMove(int newX, int newY, Board board) {
        Piece p = board.getPiece(newX, newY);

        if (firstMove) {
            if (newX == x + direction * 2 && newY == y
                    && board.getPiece(x + direction, y) == null
                    && board.getPiece(newX, newY) == null) {
                firstMove = false;
                return true;
            }
        }

        if (newY == y && newX == x + direction && p == null) {
            firstMove = false;
            return true;
        }
        if (newX == x + direction && Math.abs(newY - y) == 1 && p != null && color != p.getColor()) {
            firstMove = false;
            return true;
        }
        return false;
    }
}
