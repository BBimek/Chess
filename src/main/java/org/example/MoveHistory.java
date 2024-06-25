package org.example;

import java.time.LocalDateTime;

public class MoveHistory {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private Piece piece;
    private boolean criteriaForEnPassantMet;
    private boolean pieceCaptured;
    private Piece promotionPiece;
    private LocalDateTime moveTime;
    private String comments;
    private PieceColor color;

    public MoveHistory(int startX, int startY, int endX, int endY, Piece piece, boolean criteriaForEnPassantMet, PieceColor color) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.piece = piece;
        this.criteriaForEnPassantMet = criteriaForEnPassantMet;
        this.moveTime = LocalDateTime.now();
        this.color = color;
    }
    public boolean getCriteriaForEnPassantMet() {
        return criteriaForEnPassantMet;
    }
    public int getEndY() {
        return endY;
    }

    public int getEndX() {
        return endX;
    }

    public PieceColor getColor() {
        return color;
    }
}
