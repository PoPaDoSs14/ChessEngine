package io.github.example

import com.badlogic.gdx.graphics.Color

class PawnMoveStrategy : MoveStrategy {
    override fun getValidMoves(row: Int, col: Int, board: Array<Array<ChessPiece?>>): List<Pair<Int, Int>> {
        val moves = mutableListOf<Pair<Int, Int>>()
        println("Current position: ${moves.toString()}")
        val direction = if (board[row][col]?.color == Color.WHITE) 1 else -1
        println("Current position: ${board.toString()}")

        // Ход на одну клетку вперед
        if (row + direction in 0..7 && board[row + direction][col] == null) {
            moves.add(Pair(row + direction, col))
        }


        // Получение допустимых атакующих ходов
        if (row + direction in 0..7) {
            if (col > 0 && board[row + direction][col - 1]?.color != board[row][col]?.color) {
                moves.add(Pair(row + direction, col - 1)) // Левая диагональ
            }
            if (col < 7 && board[row + direction][col + 1]?.color != board[row][col]?.color) {
                moves.add(Pair(row + direction, col + 1)) // Правая диагональ
            }
        }

        return moves
    }
}
