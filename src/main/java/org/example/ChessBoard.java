package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ChessBoard extends JPanel {
    private Board board;
    private Piece selectedPiece;
    private PieceColor nextPlayer;
    private List<Coordinate> highlightedMoves = new ArrayList<>();
    private Square[][] squares = new Square[8][8];

    public ChessBoard(Board board) {
        this.board = board;
        this.nextPlayer = PieceColor.WHITE;
        this.setPreferredSize(new Dimension(800, 800));
        this.setLayout(new GridLayout(8, 8));

        initializeBoard();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseClick(e);
            }
        });
    }

    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = new Square(row, col);
                square.setPiece(board.getPiece(row, col));
                squares[row][col] = square;
                this.add(square);
            }
        }
    }

    private void handleMouseClick(MouseEvent e) {
        Square square = (Square) getComponentAt(e.getPoint());
        int row = square.getRow();
        int col = square.getCol();

        if (selectedPiece != null && nextPlayer == selectedPiece.getColor()) {
            if (selectedPiece.isValidMove(row, col, board)) {
                if (tryMovePiece(row, col)) {
                    finalizeMove(row, col);
                    clearPiece();
                    updateBoard();
                    return;
                }
            }
        }

        selectedPiece = board.getPiece(row, col);
        if (selectedPiece != null && selectedPiece.getColor() == nextPlayer) {
            highlightedMoves = selectedPiece.possibleMoves(board);
        }
        updateBoard();
    }

    private void clearPiece() {
        selectedPiece = null;
        highlightedMoves.clear();
        updateBoard();
    }

    private boolean tryMovePiece(int newRow, int newCol) {
        int originalRow = selectedPiece.getX();
        int originalCol = selectedPiece.getY();
        Piece originalDestinationPiece = board.getPiece(newRow, newCol);

        board.movePiece(originalRow, originalCol, newRow, newCol);

        boolean isInCheck = board.isInCheck(selectedPiece.getColor());

        if (isInCheck) {
            board.movePiece(newRow, newCol, originalRow, originalCol);
            if (originalDestinationPiece != null) {
                board.setPiece(newRow, newCol, originalDestinationPiece);
            }
        }
        return !isInCheck;
    }

    private void finalizeMove(int row, int col) {
        selectedPiece.setX(row);
        selectedPiece.setY(col);
        nextPlayer = (selectedPiece.getColor() == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
    }

    private void updateBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setPiece(board.getPiece(row, col));
                squares[row][col].setHighlight(false);
            }
        }

        for (Coordinate move : highlightedMoves) {
            squares[move.getX()][move.getY()].setHighlight(true);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].repaint();
            }
        }
    }
}
