package io.github.example

import com.badlogic.gdx.graphics.g2d.Sprite

data class ChessPiece(
    val chessId: Int,
    val color: Int,
    val chessSprite: Sprite,
    val chessPieceType: ChessPieceType
): GameObject(
    chessId,
    chessSprite)
{

    companion object {
        const val WHITE = 0
        const val BLACK = 1
    }

}
