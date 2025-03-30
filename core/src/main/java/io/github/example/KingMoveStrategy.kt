package io.github.example

class KingMoveStrategy : MoveStrategy {
    override fun getValidMoves(row: Int, col: Int, board: Array<Array<ChessPiece?>>): List<Pair<Int, Int>> {
        val validMoves = mutableListOf<Pair<Int, Int>>()
        val directions = listOf(
            Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
            Pair(0, -1), Pair(0, 1),
            Pair(1, -1), Pair(1, 0), Pair(1, 1)
        )

        for (direction in directions) {
            val newRow = row + direction.first
            val newCol = col + direction.second

            if (isValidMove(newRow, newCol, board)) {
                validMoves.add(Pair(newRow, newCol))
            }
        }

        return validMoves
    }

    private fun isValidMove(row: Int, col: Int, board: Array<Array<ChessPiece?>>): Boolean {
        if (row < 0 || row >= board.size || col < 0 || col >= board[row].size) {
            return false
        }
        val piece = board[row][col]
        return piece == null || piece.color != board[row][col]?.color
    }
}
