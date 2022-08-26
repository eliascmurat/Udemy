package br.com.islink;

import java.util.InputMismatchException;
import java.util.Scanner;

import br.com.islink.boardgame.Position;
import br.com.islink.chess.ChessException;
import br.com.islink.chess.ChessMatch;
import br.com.islink.chess.ChessPiece;
import br.com.islink.chess.ChessPosition;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        
        while (true) {
            try {
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces());
                System.out.println();
                
                System.out.print("Source: ");
                ChessPosition source = UI.readChessPosition(scanner);
                chessMatch.validateSourcePosition(new Position(8 - source.getRow(), source.getColumn() - 'a'));
                
                System.out.print("Target: ");
                ChessPosition target = UI.readChessPosition(scanner);
                chessMatch.validateTargetPosition(
                    new Position(8 - source.getRow(), source.getColumn() - 'a'),
                    new Position(8 - target.getRow(), target.getColumn() - 'a')
                );

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
            } catch (ChessException e) {
                System.out.println(e.getMessage());
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        }
    }
}
