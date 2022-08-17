package br.com.islink.chess;

import br.com.islink.boardgame.Board;
import br.com.islink.boardgame.Position;
import br.com.islink.chess.pieces.King;
import br.com.islink.chess.pieces.Rook;

public class ChessMatch {
    private Board board;

    public ChessMatch() {
        board = new Board(8, 8);
        initialSetup();
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.getPiece(i, j);
            }
        }
        return mat;
    }

    private void initialSetup() {
        board.placePiece(new Rook(board, Color.BLACK), new Position(0, 0));
        board.placePiece(new Rook(board, Color.BLACK), new Position(0, 7));
        board.placePiece(new King(board, Color.BLACK), new Position(0, 4));
    }
}
