package io.github.example

import com.badlogic.gdx.graphics.Color

class ChessBot(val color: Color) {

    private val previousMoves = mutableSetOf<Move>()

    private val opponentColor: Color
        get() = if(color == Color.WHITE) Color.WHITE else Color.BLACK

    fun getBestMove(board: Array<Array<ChessPiece?>>): Pair<Move, Int>? {
        var bestMove: Move? = null
        var bestValue = Int.MIN_VALUE

        for (move in getMovePieces(board)) {
            val newBoard = makeMove(board, move, color)
            val moveValue = evaluateBoard(newBoard)

            val opponentBestMoveValue = getBestOpponentMoveValue(newBoard)

            var adjustedValue = moveValue - opponentBestMoveValue

            if (previousMoves.contains(move)) {
                adjustedValue -= 5
            }

            if (adjustedValue > bestValue) {
                bestValue = adjustedValue
                bestMove = move
            }
        }

        bestMove?.let { previousMoves.add(it) }

        return bestMove?.let { Pair(it, bestValue) }
    }


    private fun getMovePieces(board: Array<Array<ChessPiece?>>): List<Move> {
        val moves = mutableListOf<Move>()

        for (row in board.indices) {
            for (col in board[row].indices) {
                val piece = board[row][col]
                if (piece != null && piece.color == color) {
                    val validMoves = piece.getValidMoves(row, col, board)

                    for (move in validMoves) {
                        moves.add(Move(Pair(row, col), move, piece))
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

    private fun evaluateBoard(board: Array<Array<ChessPiece?>>): Int {
        var score = 0
        for (row in board.indices) {
            for (col in board[row].indices) {
                val piece = board[row][col]
                if (piece != null) {
                    score += when (piece.color) {
                        color -> pieceValue(piece)
                        opponentColor -> -pieceValue(piece)
                        else -> 0
                    }
                }
            }
        }

        for (row in board.indices) {
            for (col in board[row].indices) {
                val piece = board[row][col]
                if (piece != null && piece.color == color) {
                    val validMoves = piece.getValidMoves(row, col, board)
                    for (move in validMoves) {
                        val targetPiece = board[move.first][move.second]
                        if (targetPiece != null && targetPiece.color == opponentColor) {
                            score += 10
                        }
                    }
                }
            }
        }

        return score
    }

    private fun getBestOpponentMoveValue(board: Array<Array<ChessPiece?>>): Int {
        var bestValue = Int.MIN_VALUE
        for (move in getMovePieces(board)) {
            val newBoard = makeMove(board, move, opponentColor)
            val moveValue = evaluateBoard(newBoard)
            if (moveValue > bestValue) {
                bestValue = moveValue
            }
        }
        return bestValue
    }

    private fun makeMove(board: Array<Array<ChessPiece?>>, move: Move, color: Color): Array<Array<ChessPiece?>> {

        val newBoard = board.map { it.clone() }.toTypedArray()

        val fromRow = move.from.first
        val fromCol = move.from.second
        val toRow = move.to.first
        val toCol = move.to.second

        val piece = newBoard[fromRow][fromCol]
        if (piece == null || piece.color != color) {
            throw IllegalArgumentException("Нет фигуры для перемещения или цвет фигуры не совпадает.")
        }

        if (toRow !in 0..7 || toCol !in 0..7) {
            throw IllegalArgumentException("Целевая позиция вне границ доски.")
        }

        val targetPiece = newBoard[toRow][toCol]
        if (targetPiece != null && targetPiece.color == color) {
            throw IllegalArgumentException("Нельзя переместить на позицию, занятую своей фигурой.")
        }

        newBoard[toRow][toCol] = piece
        newBoard[fromRow][fromCol] = null

        return newBoard
    }
}
