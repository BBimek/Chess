package org.example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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

    public void setFirstMove() {
        firstMove = false;
    }

    @Override
    public Piece copy() {
        return new Pawn(this.getX(), this.getY(), this.getColor());
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

    @Override
    public List<Coordinate> possibleMoves(Board board, List<MoveHistory> moveHistory) {
        List<Coordinate> possibleMoves = new ArrayList<>();
        int direction = (getColor() == PieceColor.WHITE) ? -1 : 1;

        // Move forward one square
        if (isValidCoordinate(x + direction, y) && board.getPiece(x + direction, y) == null) {
            possibleMoves.add(new Coordinate(x + direction, y));
        }

        // Move forward two squares from initial position
        if (firstMove) {
            if (isValidCoordinate(x + direction * 2, y) && board.getPiece(x + direction * 2, y) == null && board.getPiece(x + direction, y) == null) {
                possibleMoves.add(new Coordinate(x + direction * 2, y));
            }
        }

        // Capture diagonally right
        if (isValidCoordinate(x + direction, y + 1)) {
            Piece p = board.getPiece(x + direction, y + 1);
            if (p != null && p.getColor() != getColor()) {
                possibleMoves.add(new Coordinate(x + direction, y + 1));
            }
        }

        // Capture diagonally left
        if (isValidCoordinate(x + direction, y - 1)) {
            Piece p = board.getPiece(x + direction, y - 1);
            if (p != null && p.getColor() != getColor()) {
                possibleMoves.add(new Coordinate(x + direction, y - 1));
            }
        }
        if (!moveHistory.isEmpty()) {
            enPassant(possibleMoves, moveHistory);
        }

        return possibleMoves;
    }

    private void enPassant(List<Coordinate> possibleMoves, List<MoveHistory> moveHistory) {
        MoveHistory lastMove = moveHistory.getLast();
        if (lastMove.getCriteriaForEnPassantMet() && (Math.abs(y - lastMove.getEndY())) == 1 && this.color != lastMove.getColor() && x == lastMove.getEndX()) {
            int directionY = Integer.compare(y, lastMove.getEndY());
            possibleMoves.add(new Coordinate(lastMove.getEndX() + direction, y - (directionY)));
        }
    }

    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
}
