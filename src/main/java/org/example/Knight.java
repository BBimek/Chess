package org.example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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
    public Piece copy() {
        return new Knight(this.getX(), this.getY(), this.getColor());
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

    @Override
    public List<Coordinate> possibleMoves(Board board, List<MoveHistory> moveHistory) {
        List<Coordinate> possibleMoves = new ArrayList<>();
        int[][] directions = {
                {-2, -1}, //up left
                {-2, 1},  //up right
                {-1, 2},  //right up
                {1, 2},   //right down
                {2, 1},   //down right
                {2, -1},  //down left
                {-1, -2}, //left down
                {1, -2}  //left up
        };

        getMoves(board, possibleMoves, directions);
        return possibleMoves;
    }
    public void getMoves(Board board, List<Coordinate> possibleMoves, int[][] directions) {
        for (int[] direction : directions) {
            int i = x + direction[0];
            int j = y + direction[1];

            // Check borders
            if (i >= 0 && i < 8 && j >= 0 && j < 8) {
                Piece p = board.getPiece(i, j);
                if (p == null || p.getColor() != color) {
                    possibleMoves.add(new Coordinate(i, j));
                }
            }
        }
    }
}
