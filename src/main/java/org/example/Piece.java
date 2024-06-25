package org.example;

import java.awt.*;
import java.util.List;

public abstract class Piece {
    protected Image image;
    protected PieceColor color;
    protected String name;
    protected char shortcut;
    protected int x;
    protected int y;
    public Piece(PieceColor color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public Image getImage() {
        return image;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public char getShortcut(){
        return shortcut;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public PieceColor getColor() {
        return color;
    }
    public abstract boolean isValidMove(int newX, int newY, Board board);
    public abstract List<Coordinate> possibleMoves(Board board, List<MoveHistory> moveHistory);
    public abstract Piece copy();
}
