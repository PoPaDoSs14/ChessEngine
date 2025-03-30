package io.github.example

import com.badlogic.gdx.graphics.Color

class ChessBot(val color: Color) {

    private val opponentColor: Color
        get() = if(color == Color.WHITE) Color.BLACK else Color.WHITE

    val moveHistory = mutableListOf<Move>()

    fun getBestMove(board: Array<Array<ChessPiece?>>, depth: Int): Pair<Move, Int>? {
        var bestMove: Move? = null
        var bestValue = Int.MIN_VALUE

        for (move in getMovePieces(board, color)) {
            val newBoard = makeMove(board, move, color)
            var moveValue = minimax(newBoard, depth - 1, Int.MIN_VALUE, Int.MAX_VALUE, false)

            if (isCheckmate(newBoard, opponentColor)) {
                return Pair(move, Int.MAX_VALUE)
            }

            if (isCentralSquare(Pair(move.to.first, move.to.second))) {
                moveValue += 10
            }

            val targetPiece = board[move.to.first][move.to.second]
            if (targetPiece != null && targetPiece.color == opponentColor) {
                moveValue += pieceValue(targetPiece, move.to.first)
            }

            if (moveHistory.contains(move)) {
                moveValue -= 10
            }

            if (moveValue > bestValue) {
                bestValue = moveValue
                bestMove = move
            }
        }

        bestMove?.let { moveHistory.add(it) }

        return bestMove?.let { Pair(it, bestValue) }
    }

    fun minimax(board: Array<Array<ChessPiece?>>, depth: Int, alpha: Int, beta: Int, isMaximizing: Boolean): Int {
        if (depth == 0 || isCheckmate(board, opponentColor) || isCheckmate(board, color)) {
            return evaluateBoard(board)
        }

        if (isMaximizing) {
            var maxEval = Int.MIN_VALUE
            var currentAlpha = alpha

            for (move in getMovePieces(board, color)) {
                val newBoard = makeMove(board, move, color)
                val eval = minimax(newBoard, depth - 1, currentAlpha, beta, false)
                maxEval = maxOf(maxEval, eval)
                currentAlpha = maxOf(currentAlpha, eval)

                if (beta <= currentAlpha) {
                    break
                }
            }
            return maxEval
        } else {
            var minEval = Int.MAX_VALUE
            var currentBeta = beta

            for (move in getMovePieces(board, opponentColor)) {
                val newBoard = makeMove(board, move, opponentColor)
                val eval = minimax(newBoard, depth - 1, alpha, currentBeta, true)
                minEval = minOf(minEval, eval)
                currentBeta = minOf(currentBeta, eval)

                if (currentBeta <= alpha) {
                    break
                }
            }
            return minEval
        }
    }

    fun isCheckmate(board: Array<Array<ChessPiece?>>, color: Color): Boolean {
        val kingPosition = findKing(board, color)
        if (kingPosition == null || !isInCheck(board, kingPosition, opponentColor)) {
            return false
        }

        for (row in board.indices) {
            for (col in board[row].indices) {
                val piece = board[row][col]
                if (piece != null && piece.color == color) {
                    val validMoves = piece.getValidMoves(row, col, board)
                    for (move in validMoves) {
                        val newBoard = makeMove(board, Move(Pair(row, col), move, piece), color)
                        if (!isInCheck(newBoard, kingPosition, opponentColor)) {
                            return false
                        }
                    }
                }
            }
        }
        return true
    }

    private fun isInCheck(board: Array<Array<ChessPiece?>>, kingPosition: Pair<Int, Int>, attackerColor: Color): Boolean {
        val opponentMoves = getMovePieces(board, attackerColor)
        return opponentMoves.any { it.to == kingPosition }
    }

    private fun findKing(board: Array<Array<ChessPiece?>>, color: Color): Pair<Int, Int>? {
        for (row in board.indices) {
            for (col in board[row].indices) {
                val piece = board[row][col]
                if (piece?.pieceType == ChessPieceType.KING && piece.color == color) {
                    return Pair(row, col)
                }
            }
        }
        return null
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
            ChessPieceType.HORSE -> 4
            ChessPieceType.ELEPHANT -> 4
            ChessPieceType.CASTLE -> 6
            ChessPieceType.QUEEN -> 10
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
                        if (targetPiece != null && targetPiece.color == opponentColor && targetPiece.pieceType == ChessPieceType.PAWN) {
                            score += 10
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

        for (row in board.indices) {
            for (col in board[row].indices) {
                val piece = board[row][col]
                if (piece != null && piece.color == color) {
                    val validMoves = piece.getValidMoves(row, col, board)
                    for (move in validMoves) {
                        val targetPiece = board[move.first][move.second]
                        if (targetPiece != null && targetPiece.color == opponentColor) {
                            score += pieceValue(targetPiece, move.first)
                        }
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
                            threatScore -= pieceValue(piece, row)
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
