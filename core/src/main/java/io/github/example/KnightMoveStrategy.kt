package io.github.example

class KnightMoveStrategy : MoveStrategy {
    override fun getValidMoves(row: Int, col: Int, board: Array<Array<ChessPiece?>>): List<Pair<Int, Int>> {
        val moves = mutableListOf<Pair<Int, Int>>()
        val knightMoves = arrayOf(
            Pair(2, 1), Pair(2, -1), Pair(-2, 1), Pair(-2, -1),
            Pair(1, 2), Pair(1, -2), Pair(-1, 2), Pair(-1, -2)
        )

        for (move in knightMoves) {
            val newRow = row + move.first
            val newCol = col + move.second

            if (newRow in 0..7 && newCol in 0..7) {
                val targetPiece = board[newRow][newCol]
                if (targetPiece == null || targetPiece.color != board[row][col]?.color) {
                    moves.add(Pair(newRow, newCol))
                }
            }
        }

        return moves
    }
}
