package br.com.islink.chess;

import br.com.islink.boardgame.Board;
import br.com.islink.boardgame.Piece;
import br.com.islink.boardgame.Position;

public abstract class ChessPiece extends Piece {
    private Color color;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    protected boolean hasOpponentPiece(Position Position) {
        ChessPiece chessPiece = (ChessPiece) getBoard().getPiece(position);
        return chessPiece != null && chessPiece.getColor() != color;
    }
}
