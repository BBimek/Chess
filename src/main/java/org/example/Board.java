package org.example;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Square[][] board;
    private boolean isInCheck;
    private List<String> annotations = new ArrayList<>();
    private int moveCount;

    public Board() {
        board = new Square[8][8];
        this.moveCount = 0;
        setupSquares();
        setupPieces();
    }
    public Board(boolean setupPieces) {
        board = new Square[8][8];
        this.moveCount = 0;
        setupSquares();
        if (setupPieces) {
            setupPieces();
        }
    }

    private void setupSquares() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = new Square(row, col);
            }
        }
    }

    private void setupPieces() {
        for (int i = 0; i < 8; i++) {
            board[1][i].setPiece(new Pawn(1, i, PieceColor.BLACK));
            board[6][i].setPiece(new Pawn(6, i, PieceColor.WHITE));
        }

        board[0][0].setPiece(new Rook(0, 0, PieceColor.BLACK));
        board[0][7].setPiece(new Rook(0, 7, PieceColor.BLACK));
        board[0][1].setPiece(new Knight(0, 1, PieceColor.BLACK));
        board[0][6].setPiece(new Knight(0, 6, PieceColor.BLACK));
        board[0][2].setPiece(new Bishop(0, 2, PieceColor.BLACK));
        board[0][5].setPiece(new Bishop(0, 5, PieceColor.BLACK));
        board[0][3].setPiece(new Queen(0, 3, PieceColor.BLACK));
        board[0][4].setPiece(new King(0, 4, PieceColor.BLACK));

        board[7][0].setPiece(new Rook(7, 0, PieceColor.WHITE));
        board[7][7].setPiece(new Rook(7, 7, PieceColor.WHITE));
        board[7][1].setPiece(new Knight(7, 1, PieceColor.WHITE));
        board[7][6].setPiece(new Knight(7, 6, PieceColor.WHITE));
        board[7][2].setPiece(new Bishop(7, 2, PieceColor.WHITE));
        board[7][5].setPiece(new Bishop(7, 5, PieceColor.WHITE));
        board[7][3].setPiece(new Queen(7, 3, PieceColor.WHITE));
        board[7][4].setPiece(new King(7, 4, PieceColor.WHITE));
    }

    public Piece getPiece(int x, int y) {
        return board[x][y].getPiece();
    }

    public void setPiece(int x, int y, Piece piece) {
        board[x][y].setPiece(piece);
    }

    public void movePiece(int startX, int startY, int endX, int endY) {
        Square startSquare = board[startX][startY];
        Square endSquare = board[endX][endY];
        boolean taken = false;

        Piece myPiece = startSquare.getPiece();
        Piece opponentPiece = endSquare.getPiece();

        // If capture is about to happen
        if (opponentPiece != null && opponentPiece.getColor() != myPiece.getColor()) {
            endSquare.setPiece(null);
            taken = true;
        }


        if (myPiece instanceof Pawn) {
            if (endY != startY) {
                if (opponentPiece == null) {
                    System.out.println("enpassant");
                    int direction =  Integer.compare(startX,endX);
                    board[endX + direction][endY].setPiece(null);
                }
            }
        }

        // Set new location for moved piece
        endSquare.setPiece(myPiece);
        startSquare.setPiece(null);
        myPiece.setX(endX);
        myPiece.setY(endY);
        //createAnnotation(myPiece, taken, startX, startY, endX, endY);
        moveCount++;
    }

    public boolean isInCheck(PieceColor color) {
        Coordinate kingPosition = getKingPosition(color);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col].getPiece();
                if (piece != null && piece.getColor() != color && piece.isValidMove(kingPosition.getX(), kingPosition.getY(), this)) {
                    isInCheck = true;
                    return true;
                }
            }
        }
        isInCheck = false;
        return false;
    }

    public Coordinate getKingPosition(PieceColor color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col].getPiece();
                if (piece instanceof King && piece.getColor() == color) {
                    return new Coordinate(row, col);
                }
            }
        }
        return null;
    }

    public boolean isInCheckAfterMove(int startX, int startY, int endX, int endY) {
        Board copyBoard = this.copy();
        copyBoard.movePiece(startX, startY, endX, endY);
        PieceColor color = copyBoard.getPiece(endX, endY).getColor();
        return copyBoard.isInCheck(color);
    }

    public Board copy() {
        Board copy = new Board(false);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = this.getPiece(row, col);
                if (piece != null) {
                    copy.setPiece(row, col, piece.copy());
                }
            }
        }
        return copy;
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
