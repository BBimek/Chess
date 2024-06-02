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


    public ChessBoard(Board board) {
        this.board = board;
        this.nextPlayer = PieceColor.WHITE;
        this.setPreferredSize(new Dimension(800, 800));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseClick(e);
            }
        });
    }

    private void handleMouseClick(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int col = x / 100; // Calculate the column based on the x-coordinate
        int row = y / 100; // Calculate the row based on the y-coordinate

        if (selectedPiece != null && nextPlayer == selectedPiece.getColor()) {
            if (selectedPiece.isValidMove(row, col, board)) {
                // Store original positions
                int originalX = selectedPiece.getX();
                int originalY = selectedPiece.getY();
                Piece originalDestinationPiece = board.getPiece(row, col);

                // Perform the hypothetical move
                board.movePiece(originalX, originalY, row, col);

                // Check if the move exposes the king to check
                if (board.isInCheck(selectedPiece.getColor())) {
                    // Revert the move
                    board.movePiece(row, col, originalX, originalY);
                    if (originalDestinationPiece != null) {
                        board.setPiece(row, col, originalDestinationPiece);
                    }
                    System.out.println("Illegal move: King would be in check.");
                } else {
                    // Finalize the move
                    selectedPiece.setX(row);
                    selectedPiece.setY(col);

                    // Switch the player turn
                    nextPlayer = (selectedPiece.getColor() == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

                    // Check if the next player is in check
                    if (board.isInCheck(nextPlayer)) {
                        System.out.println("Šach");
                    }

                    // Clear the selected piece
                    selectedPiece = null;
                    highlightedMoves.clear();
                    repaint();

                }
            } else {
                selectedPiece = null;
            }
        } else {
            selectedPiece = board.getPiece(row, col);
            if (selectedPiece != null && selectedPiece.getColor() == nextPlayer) {
                highlightedMoves = selectedPiece.possibleMoves(board);
            }

        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(new Color(105,146,62));
                }
                g.fillRect(col * 100, row * 100, 100, 100);
            }
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getImage() != null) {
                    g.drawImage(piece.getImage(), col * 100, row * 100, this);
                }
            }
        }
        g.setColor(new Color(0, 255, 0, 128)); // Semi-transparent green
        for (Coordinate move : highlightedMoves) {
            int x = move.getY() * 100 + 50 - 15; // Centered on the square
            int y = move.getX() * 100 + 50 - 15;
            g.fillOval(x, y, 30, 30); // Draw the circle
        }
    }
}
