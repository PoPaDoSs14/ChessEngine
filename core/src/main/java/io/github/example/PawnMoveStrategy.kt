package io.github.example

import com.badlogic.gdx.graphics.Color

class PawnMoveStrategy : MoveStrategy {
    override fun getValidMoves(row: Int, col: Int, board: Array<Array<ChessPiece?>>): List<Pair<Int, Int>> {
        val moves = mutableListOf<Pair<Int, Int>>()
        val direction = if (board[row][col]?.color == Color.WHITE) -1 else 1 // направление движения

        // Проверка на пустую ячейку впереди
        if (board[row + direction][col] == null) {
            moves.add(Pair(row + direction, col))
        }

        // Добавьте другие условия для валидных движений

        return moves
    }
}
