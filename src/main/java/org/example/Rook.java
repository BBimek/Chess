package org.example;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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
    public Piece copy() {
        return new Rook(this.getX(), this.getY(), this.getColor());
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

    @Override
    public List<Coordinate> possibleMoves(Board board, List<MoveHistory> moveHistory) {
        List<Coordinate> possibleMoves = new ArrayList<>();
        int[][] allDirections = {
                {-1, 0}, // Up
                {1, 0},  // Down
                {0, -1}, // Left
                {0, 1},  // Right
        };
        getMoves(board, possibleMoves, allDirections);
        return possibleMoves;
    }

    public void getMoves(Board board, List<Coordinate> possibleMoves, int[][] directions) {
        for (int[] direction : directions) {
            int i = x;
            int j = y;
            while (true) {
                i += direction[0];
                j += direction[1];
                // Check borders
                if (i < 0 || i >= 8 || j < 0 || j >= 8) {
                    break;
                }
                Piece p = board.getPiece(i, j);
                if (p == null) {
                    possibleMoves.add(new Coordinate(i, j));
                } else {
                    if (p.getColor() != color) {
                        possibleMoves.add(new Coordinate(i, j));
                    }
                    break;
                }
            }
        }
    }
}
