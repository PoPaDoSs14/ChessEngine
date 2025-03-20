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
}
