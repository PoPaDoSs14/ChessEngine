package io.github.example

import com.badlogic.gdx.graphics.g2d.Sprite

data class ChessPiece(
    val chessId: Int,
    val chessSprite: Sprite,
    val chessPieseType: ChessPieseType
): GameObject(
    chessId,
    chessSprite)
{

}
