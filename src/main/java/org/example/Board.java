package org.example;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private Piece[][] board;
    private Map<Coordinate,Piece> piecesOnBoard;
    private boolean isInCheck;
    private List<String> annotations = new ArrayList<>();
    private int moveCount;

    public Board() {
        board = new Piece[8][8];
        piecesOnBoard = new HashMap<>();
        this.moveCount = 0;
        setupPieces();
    }

    private void setupPieces() {
        for (int i = 0; i < 8; i++) {
            piecesOnBoard.put(new Coordinate(1, i), new Pawn(1, i, PieceColor.BLACK));
        }
        for (int i = 0; i < 8; i++) {
            piecesOnBoard.put(new Coordinate(6, i), new Pawn(6, i, PieceColor.WHITE));
        }
        piecesOnBoard.put(new Coordinate(0, 0), new Rook(0, 0, PieceColor.BLACK));
        piecesOnBoard.put(new Coordinate(0, 7), new Rook(0, 7, PieceColor.BLACK));
        piecesOnBoard.put(new Coordinate(0, 1), new Knight(0, 1, PieceColor.BLACK));
        piecesOnBoard.put(new Coordinate(0, 6), new Knight(0, 6, PieceColor.BLACK));
        piecesOnBoard.put(new Coordinate(0, 2), new Bishop(0, 2, PieceColor.BLACK));
        piecesOnBoard.put(new Coordinate(0, 5), new Bishop(0, 5, PieceColor.BLACK));
        piecesOnBoard.put(new Coordinate(0, 3), new Queen(0, 3, PieceColor.BLACK));
        piecesOnBoard.put(new Coordinate(0, 4), new King(0, 4, PieceColor.BLACK));

        piecesOnBoard.put(new Coordinate(7, 0), new Rook(7, 0, PieceColor.WHITE));
        piecesOnBoard.put(new Coordinate(7, 7), new Rook(7, 7, PieceColor.WHITE));
        piecesOnBoard.put(new Coordinate(7, 1), new Knight(7, 1, PieceColor.WHITE));
        piecesOnBoard.put(new Coordinate(7, 6), new Knight(7, 6, PieceColor.WHITE));
        piecesOnBoard.put(new Coordinate(7, 2), new Bishop(7, 2, PieceColor.WHITE));
        piecesOnBoard.put(new Coordinate(7, 5), new Bishop(7, 5, PieceColor.WHITE));
        piecesOnBoard.put(new Coordinate(7, 3), new Queen(7, 3, PieceColor.WHITE));
        piecesOnBoard.put(new Coordinate(7, 4), new King(7, 4, PieceColor.WHITE));
    }

    public Piece getPiece(int x, int y) {
        return piecesOnBoard.get(new Coordinate(x, y));
    }

    public void setPiece(int x, int y, Piece piece) {
        piecesOnBoard.put(new Coordinate(x, y), piece);
    }

    public void removePiece(int x, int y) {
        piecesOnBoard.remove(new Coordinate(x, y));
    }
    public void movePiece(int startX, int startY, int endX, int endY) {
        Coordinate start = new Coordinate(startX, startY);
        Coordinate end = new Coordinate(endX, endY);
        boolean taken = false;

        Piece myPiece = piecesOnBoard.get(start);
        Piece oponentPiece = piecesOnBoard.get(end);

        //If capture is about to happen
        if (oponentPiece != null && oponentPiece.getColor() != myPiece.getColor()) {
            piecesOnBoard.remove(end);
            taken = true;
        }
        //set new location for moved piece
        piecesOnBoard.put(end,myPiece);
        piecesOnBoard.remove(start);
        myPiece.setX(endX);
        myPiece.setY(endY);
        createAnnotation(myPiece, taken, startX, startY, endX, endY);
        moveCount++;
    }
    public boolean isInCheck(PieceColor color) {
        Coordinate kingPosition = getKingPosition(color);
        for (Map.Entry<Coordinate,Piece> entry : piecesOnBoard.entrySet()) {
            Piece piece = entry.getValue();
            if (piece.getColor()!=color && piece.isValidMove(kingPosition.getX(), kingPosition.getY(), this)) {
                return true;
            }
        }
        return false;
    }
    private Coordinate getKingPosition(PieceColor color) {
        for (Map.Entry<Coordinate,Piece> entry : piecesOnBoard.entrySet()) {
            Piece piece = entry.getValue();
            if (piece instanceof King && piece.getColor() == color) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void createAnnotation(Piece myPiece, boolean taken, int startX, int startY, int endX, int endY) {
        char col = getCharForNumber(endY + 1); // Convert column to letter
        int row = 8 - endX; // Convert row to chess row numbering
        char startCol = getCharForNumber(startY + 1);
        int startRow = 8 - startX;

        String move = "";

        // Add piece type if not a pawn
        if (!(myPiece instanceof Pawn)) {
            move += myPiece.getShortcut();
        }

        // Add 'x' for capture
        if (taken) {
            if (myPiece instanceof Pawn) {
                move += startCol; // Add starting column for pawn captures
            }
            move += "x";
        }

        // Add destination square
        move += col + String.valueOf(row);

        // Add special annotations (e.g., check, checkmate)
        if (isInCheck) {
            move += "+"; // Add check symbol
        }

        // Add the move to the annotations list
        annotations.add(move);
        moveCount++;

        // Print all annotations
        System.out.println(move);
    }

    private char getCharForNumber(int i) {
        return (char) ('a' + i - 1);
    }
}
