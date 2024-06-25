package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import static javax.swing.JOptionPane.showMessageDialog;

public class ChessBoard extends JPanel {
    private Board board;
    private Piece selectedPiece;
    private PieceColor nextPlayer;
    private List<Coordinate> highlightedMoves = new ArrayList<>();
    private Square[][] squares = new Square[8][8];
    private boolean isInCheck;
    private List<MoveHistory> moveHistory = new ArrayList<>();

    public ChessBoard(Board board) {
        this.board = board;
        this.nextPlayer = PieceColor.WHITE;
        this.setPreferredSize(new Dimension(800, 800));
        this.setLayout(new GridLayout(8, 8));
        this.isInCheck = false;

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
            Coordinate movePiece = new Coordinate(row,col);
            for (Coordinate move : highlightedMoves) {
                if (move.equals(movePiece)) {
                    finalizeMove(row,col, square);
                    if (selectedPiece.getColor() == PieceColor.WHITE) {
                        isInCheck = board.isInCheck(PieceColor.BLACK);
                    } else {
                        isInCheck = board.isInCheck(PieceColor.WHITE);
                    }
                    clearPiece();
                    updateBoard();
                    if (isInCheck) {
                        checkForCheckmate();
                    }
                    return;
                }
            }
            /*if (selectedPiece.isValidMove(row, col, board)) {
                if (tryMovePiece(row, col)) {
                    finalizeMove(row, col);
                    clearPiece();
                    updateBoard();
                    return;
                }
            }*/
        }

        selectedPiece = board.getPiece(row, col);
        if (selectedPiece != null && selectedPiece.getColor() == nextPlayer) {
            highlightedMoves = selectedPiece.possibleMoves(board, moveHistory);

            highlightedMoves.removeIf(move -> board.isInCheckAfterMove(selectedPiece.getX(), selectedPiece.getY(), move.getX(), move.getY()));
        }
        updateBoard();
    }
    private void checkForCheckmate() {
        Coordinate kingLocation = new Coordinate(board.getKingPosition(nextPlayer).getX(), board.getKingPosition(nextPlayer).getY());
        Piece king = board.getPiece(kingLocation.getX(), kingLocation.getY());
        if (king != null) {
            List<Coordinate> moves = king.possibleMoves(board, moveHistory);
            moves.removeIf(move -> board.isInCheckAfterMove(kingLocation.getX(), kingLocation.getY(), move.getX(), move.getY()));
            boolean possible = canOtherPiecesBlock(king.getColor());
            if (moves.isEmpty() && !possible) {
                System.out.println("Král nemá žádné možné tahy. Šach mat!");
                showMessageDialog(null, "šach mat");
            }
        }
    }

    private boolean canOtherPiecesBlock(PieceColor color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square s = squares[row][col];
                Piece p = s.getPiece();
                if (p != null && p.getColor() == color) {
                    List<Coordinate> coords = s.getPiece().possibleMoves(board, moveHistory);
                    coords.removeIf(move -> board.isInCheckAfterMove(p.getX(), p.getY(), move.getX(), move.getY()));
                    if (!coords.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void clearPiece() {
        selectedPiece = null;
        highlightedMoves.clear();
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

    private void finalizeMove(int row, int col, Square square) {
        int originalX = selectedPiece.getX();
        board.movePiece(selectedPiece.getX(),selectedPiece.getY(),row,col);

        boolean criteriaForEnPassant = false;
        if (selectedPiece instanceof Pawn) {
            ((Pawn) selectedPiece).setFirstMove();
            if (Math.abs(originalX - row) == 2) {
                criteriaForEnPassant = true;
            }
        }
        nextPlayer = (selectedPiece.getColor() == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
        MoveHistory move = new MoveHistory(selectedPiece.getX(), selectedPiece.getY(), row, col, selectedPiece, criteriaForEnPassant, selectedPiece.getColor());
        moveHistory.add(move);
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
