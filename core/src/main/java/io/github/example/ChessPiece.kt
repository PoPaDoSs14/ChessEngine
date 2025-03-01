package io.github.example

import com.badlogic.gdx.graphics.g2d.Sprite

data class ChessPiece(
    val chessId: Int,
    val chessSprite: Sprite
): GameObject(
    chessId,
    chessSprite)
{

}
