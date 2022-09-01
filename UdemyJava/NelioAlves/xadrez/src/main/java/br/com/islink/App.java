package br.com.islink;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
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
        List<ChessPiece> captured = new ArrayList<>();
        
        while (true) {
            try {
                UI.clearScreen();
                UI.printMatch(chessMatch, captured);
                
                System.out.print("\nSource: ");
                ChessPosition source = UI.readChessPosition(scanner);
                chessMatch.validateSourcePosition(new Position(8 - source.getRow(), source.getColumn() - 'a'));
                
                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(), possibleMoves);

                System.out.print("\nTarget: ");
                ChessPosition target = UI.readChessPosition(scanner);
                chessMatch.validateTargetPosition(
                    new Position(8 - source.getRow(), source.getColumn() - 'a'),
                    new Position(8 - target.getRow(), target.getColumn() - 'a')
                );

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
            
                if (capturedPiece != null) {
					captured.add(capturedPiece);
				}
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
