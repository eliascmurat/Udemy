package br.com.islink.chess.pieces;

import br.com.islink.boardgame.Board;
import br.com.islink.chess.ChessPiece;
import br.com.islink.chess.Color;

public class Rook extends ChessPiece {
    public Rook(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "R";
    }    
}
