package io.github.example

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite


data class ChessPiece(
    val chessId: Int,
    val color: Color,
    val chessSprite: Sprite,
    val pieceType: ChessPieceType
): GameObject(
    chessId,
    chessSprite)
{


}
