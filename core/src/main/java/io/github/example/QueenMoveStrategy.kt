package io.github.example

class QueenMoveStrategy : MoveStrategy {
    override fun getValidMoves(row: Int, col: Int, board: Array<Array<ChessPiece?>>): List<Pair<Int, Int>> {
        val moves = mutableListOf<Pair<Int, Int>>()

        val directions = arrayOf(
            Pair(0, 1),
            Pair(1, 0),
            Pair(0, -1),
            Pair(-1, 0),
            Pair(1, 1),
            Pair(-1, -1),
            Pair(-1, 1),
            Pair(1, -1),
        )

        for (direction in directions) {
            var newRow = row
            var newCol = col

            // Двигаемся в выбранном направлении, пока не выйдем за границы доски
            while (true) {
                newRow += direction.first
                newCol += direction.second

                // Проверка выхода за границы доски
                if (newRow !in 0..7 || newCol !in 0..7) {
                    break
                }

                val targetPiece = board[newRow][newCol]
                if (targetPiece == null) {
                    // Если клетка пустая, добавляем её в список возможных ходов
                    moves.add(Pair(newRow, newCol))
                } else {
                    // Если на клетке есть фигура, проверяем цвет
                    if (targetPiece.color != board[row][col]?.color) {
                        // Если фигура противника, добавляем её в список возможных ходов
                        moves.add(Pair(newRow, newCol))
                    }
                    // В любом случае, после встречи с фигурой, движение останавливается
                    break
                }
            }
        }

        return moves
    }
}
