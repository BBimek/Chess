package org.example;

import javax.swing.*;

public class Main extends JFrame {
    public Main(){
        Board board = new Board();
        ChessBoard chessBoard = new ChessBoard(board);
        this.add(chessBoard);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());


    }
}