package br.com.islink.chess.pieces;

import br.com.islink.boardgame.Board;
import br.com.islink.chess.ChessPiece;
import br.com.islink.chess.Color;

public class King extends ChessPiece {
    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }  
}
