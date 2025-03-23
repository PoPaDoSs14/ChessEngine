package io.github.example

import com.badlogic.gdx.graphics.Color

class ChessBot(val color: Color) {

    private val previousMoves = mutableSetOf<Move>()

    private val opponentColor: Color
        get() = if(color == Color.WHITE) Color.WHITE else Color.BLACK

    fun getBestMove(board: Array<Array<ChessPiece?>>): Pair<Move, Int>? {
        var bestMove: Move? = null
        var bestValue = Int.MIN_VALUE

        for (move in getMovePieces(board, color)) {
            val newBoard = makeMove(board, move, color)
            var moveValue = evaluateBoard(newBoard)

            if (isCentralSquare(move.to)) {
                moveValue += 5
            }

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

    private fun isCentralSquare(square: Pair<Int, Int>): Boolean {
        val (row, col) = square
        return (row == 3 && col in 3..4) || (row == 4 && col in 3..4)
    }


    private fun getMovePieces(board: Array<Array<ChessPiece?>>, color: Color): List<Move> {
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

    private fun pieceValue(piece: ChessPiece, row: Int): Int {
        return when (piece.pieceType) {
            ChessPieceType.PAWN -> {
                when (row) {
                    0, 1 -> 0
                    2 -> 1
                    3 -> 2
                    4 -> 3
                    5 -> 4
                    6, 7 -> 5
                    else -> 0
                }
            }
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
                        color -> pieceValue(piece, row)
                        opponentColor -> -pieceValue(piece, row)
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
                            score += pieceValue(targetPiece)
                        }
                    }
                }
            }
        }

        for (row in board.indices) {
            for (col in board[row].indices) {
                val piece = board[row][col]
                if (piece != null && piece.color == color) {
                    val validMoves = piece.getValidMoves(row, col, board)
                    if (piece.pieceType != ChessPieceType.PAWN) {
                        score += 5
                    }
                }
            }
        }

        score += evaluateThreats(board)

        return score
    }

    private fun evaluateThreats(board: Array<Array<ChessPiece?>>): Int {
        var threatScore = 0

        for (row in board.indices) {
            for (col in board[row].indices) {
                val piece = board[row][col]
                if (piece != null && piece.color == color) {
                    val opponentMoves = getMovePieces(board, opponentColor)
                    for (move in opponentMoves) {
                        if (move.to == Pair(row, col)) {
                            threatScore -= pieceValue(piece)
                        }
                    }
                }
            }
        }

        return threatScore
    }

    private fun getBestOpponentMoveValue(board: Array<Array<ChessPiece?>>): Int {
        var bestValue = Int.MIN_VALUE
        for (move in getMovePieces(board, opponentColor)) {
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
