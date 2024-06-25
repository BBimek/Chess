package org.example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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
    public Piece copy() {
        return new King(this.getX(), this.getY(), this.getColor());
    }

    @Override
    public boolean isValidMove(int newX, int newY, Board board) {
        // Check if the move is within one square in any direction
        if (Math.abs(newX - x) <= 1 && Math.abs(newY - y) <= 1) {
            Piece p = board.getPiece(newX, newY);
            // Check if the target square is either empty or contains an opponent's piece
            if (p == null || p.getColor() != this.color) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Coordinate> possibleMoves(Board board, List<MoveHistory> moveHistory) {
        List<Coordinate> possibleMoves = new ArrayList<>();
        int[][] allDirections = {
                {-1, 0},  // Up
                {1, 0},   // Down
                {0, -1},  // Left
                {0, 1},   // Right
                {-1, -1}, // Top left
                {-1, 1},  // Top right
                {1, -1},  // Bottom left
                {1, 1}    // Bottom right
        };
        getMoves(board, possibleMoves, allDirections);
        return possibleMoves;
    }

    public void getMoves(Board board, List<Coordinate> possibleMoves, int[][] directions) {
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            // Check borders
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                Piece p = board.getPiece(newX, newY);
                if (p == null || p.getColor() != color) {
                    possibleMoves.add(new Coordinate(newX, newY));
                }
            }
        }
    }
}
