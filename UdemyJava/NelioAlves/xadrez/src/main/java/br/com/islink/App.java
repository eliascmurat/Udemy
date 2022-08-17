package br.com.islink;

import br.com.islink.chess.ChessMatch;

public class App {
    public static void main(String[] args) {
        ChessMatch chessMatch = new ChessMatch();
        UI.printBoard(chessMatch.getPieces());
    }
}
