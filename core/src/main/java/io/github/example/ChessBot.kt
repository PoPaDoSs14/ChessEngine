package io.github.example

import com.badlogic.gdx.graphics.Color

class ChessBot(val color: Color) {

    private val opponentColor: Color
        get() = if(color == Color.WHITE) Color.WHITE else Color.BLACK

    fun getBestMove(board: Array<Array<ChessPiece?>>): Pair<Int, Int>? {
        return null
    }


    private fun getMovePieces(board: Array<Array<ChessPiece?>>): List<Move> {

        val moves = mutableListOf<Move>()

        for (row in board.indices) {
            for (col in board[row].indices) {
                val piece = board[row][col]
                if (piece != null && piece.color == color){
                    val validMoves = piece.getValidMoves(row, col, board)

                    for (move in validMoves) {
                        moves.add(Move(move.first, move.second))
                    }
                }
            }
        }
        return moves
    }

    private fun pieceValue(piece: ChessPiece): Int {
        return when (piece.pieceType) {
            ChessPieceType.PAWN -> 1
            ChessPieceType.HORSE -> 3
            ChessPieceType.ELEPHANT -> 3
            ChessPieceType.CASTLE -> 5
            ChessPieceType.QUEEN -> 9
            ChessPieceType.KING -> 0
        }
    }
}
