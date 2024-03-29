package br.com.islink.chess;

import br.com.islink.boardgame.Board;
import br.com.islink.boardgame.Piece;
import br.com.islink.boardgame.Position;

public abstract class ChessPiece extends Piece {
    private Color color;
    private int moveCount;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getMoveCount() {
		return moveCount;
	}

	public void increaseMoveCount() {
		moveCount++;
	}

	public void decreaseMoveCount() {
		moveCount--;
	}

    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(position);
    }

	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = (ChessPiece) getBoard().getPiece(position);
		return p != null && p.getColor() != color;
	}
}
