package io.github.example

import com.badlogic.gdx.graphics.Color

class PawnMoveStrategy : MoveStrategy {

    private var firstMove = true

    override fun getValidMoves(row: Int, col: Int, board: Array<Array<ChessPiece?>>): List<Pair<Int, Int>> {
        val moves = mutableListOf<Pair<Int, Int>>()
        val direction = if (board[row][col]?.color == Color.WHITE) -1 else 1

        if (isValidPosition(row + direction, col, board) && board[row + direction][col] == null) {
            moves.add(Pair(row + direction, col))
        }

        if (firstMove && isValidPosition(row + 2 * direction, col, board) && board[row + 2 * direction][col] == null) {
            if (board[row + direction][col] == null) {
                moves.add(Pair(row + 2 * direction, col))
            }
        }

        return moves
    }

    fun markFirstMoveDone() {
        firstMove = false
    }

    private fun isValidPosition(row: Int, col: Int, board: Array<Array<ChessPiece?>>): Boolean {
        return row in board.indices && col in board[row].indices
    }
}
