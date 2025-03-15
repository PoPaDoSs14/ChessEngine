package io.github.example

interface MoveStrategy {
    fun getValidMoves(row: Int, col: Int, board: Array<Array<ChessPiece?>>): List<Pair<Int, Int>>
}
