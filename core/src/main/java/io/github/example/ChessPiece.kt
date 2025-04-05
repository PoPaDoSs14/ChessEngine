package io.github.example

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite


data class ChessPiece(
    val chessId: Int,
    val color: Color,
    val chessSprite: Sprite,
    val pieceType: ChessPieceType,
    val moveStrategy: MoveStrategy
): GameObject(
    chessSprite)
{

    fun getValidMoves(row: Int, col: Int, board: Array<Array<ChessPiece?>>): List<Pair<Int, Int>> {
        return moveStrategy.getValidMoves(row, col, board)
    }


}
