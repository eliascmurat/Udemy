package br.com.islink;

import java.util.Scanner;

import br.com.islink.chess.ChessMatch;
import br.com.islink.chess.ChessPiece;
import br.com.islink.chess.ChessPosition;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        
        while (true) {
            UI.printBoard(chessMatch.getPieces());
            
            System.out.println();
            System.out.print("Source: ");
            ChessPosition source = UI.readChessPosition(scanner);
            
            System.out.print("Target: ");
            ChessPosition target = UI.readChessPosition(scanner);
            
            ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
        
            System.out.println();
        }
    }
}
