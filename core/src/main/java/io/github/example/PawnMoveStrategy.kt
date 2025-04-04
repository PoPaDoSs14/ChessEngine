package io.github.example

import com.badlogic.gdx.graphics.Color

class PawnMoveStrategy : MoveStrategy {

    val isFirstMove = true

    override fun getValidMoves(row: Int, col: Int, board: Array<Array<ChessPiece?>>): List<Pair<Int, Int>> {
        val moves = mutableListOf<Pair<Int, Int>>()
        val direction = if (board[row][col]?.color == Color.WHITE) -1 else 1

        if (isValidPosition(row + direction, col, board) && board[row + direction][col] == null) {
            moves.add(Pair(row + direction, col))
        }

        if (isFirstMove && isValidPosition(row + 2 * direction, col, board) && board[row + 2 * direction][col] == null) {
            if (board[row + direction][col] == null) {
                moves.add(Pair(row + 2 * direction, col))
            }
        }

        if (isValidPosition(row + direction, col - 1, board)) {
            val targetPiece = board[row + direction][col - 1]
            if (targetPiece != null && targetPiece.color != board[row][col]?.color) {
                moves.add(Pair(row + direction, col - 1))
            }
        }

        if (isValidPosition(row + direction, col + 1, board)) {
            val targetPiece = board[row + direction][col + 1]
            if (targetPiece != null && targetPiece.color != board[row][col]?.color) {
                moves.add(Pair(row + direction, col + 1))
            }
        }

        return moves
    }

    private fun isValidPosition(row: Int, col: Int, board: Array<Array<ChessPiece?>>): Boolean {
        return row in board.indices && col in board[row].indices
    }
}
