package io.github.example

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Array

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
class Main : ApplicationAdapter() {
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var spriteBatch: SpriteBatch
    private val boardSize = 8
    private val pieces = Array(boardSize) { arrayOfNulls<ChessPiece>(boardSize) }

    private lateinit var whitePawnTexture: Texture
    private lateinit var blackPawnTexture: Texture
    // Добавьте текстуры для других фигур.

    override fun create() {
        shapeRenderer = ShapeRenderer()
        spriteBatch = SpriteBatch()
        initializeTextures()
        initializePieces()
    }

    private fun initializeTextures() {
        // Загрузка текстур для фигур
        whitePawnTexture = Texture("W_pawn.png")
        blackPawnTexture = Texture("B_pawn.png")

    }

    private fun initializePieces() {
        pieces[1][0] = ChessPiece(0, Color.WHITE, Sprite(blackPawnTexture), ChessPieceType.PAWN) // Черные пешки
        pieces[1][1] = ChessPiece(0, Color.WHITE, Sprite(blackPawnTexture), ChessPieceType.PAWN)
        // Заполнить остальные черные пешки
        pieces[6][0] = ChessPiece(0, Color.BLACK, Sprite(whitePawnTexture), ChessPieceType.PAWN) // Белые пешки
        pieces[6][1] = ChessPiece(0, Color.BLACK, Sprite(whitePawnTexture), ChessPieceType.PAWN)

        // Добавить остальные фигуры (ладьи, кони, слоны, ферзи, короли) здесь...
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f) // Устанавливаем цвет фона (белый)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Получаем размеры окна
        val screenWidth = Gdx.graphics.width
        val screenHeight = Gdx.graphics.height

        // Вычисляем размер квадрата
        val squareSize = Math.min(screenWidth, screenHeight) / boardSize

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        // Рисуем шахматную доску
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                // Определяем цвет квадрата (бежевый и темно-коричневый)
                val color = if ((row + col) % 2 == 0) Color(0.8f, 0.7f, 0.5f, 1f) else Color(0.4f, 0.2f, 0.1f, 1f)
                shapeRenderer.setColor(color)

                // Рисуем квадрат
                shapeRenderer.rect(col * squareSize.toFloat(), row * squareSize.toFloat(), squareSize.toFloat(), squareSize.toFloat())
            }
        }

        shapeRenderer.end()

        // Отображаем фигуры
        drawPieces(squareSize)
    }

    private fun drawPieces(squareSize: Int) {
        spriteBatch.begin()
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                pieces[row][col]?.let { piece ->
                    val x = (col * squareSize + squareSize / 2 - piece.sprite.width / 2).toFloat()
                    val y = (row * squareSize + squareSize / 2 - piece.sprite.height / 2).toFloat()
                    piece.sprite.setPosition(x, y)
                    piece.sprite.draw(spriteBatch)
                }
            }
        }
        spriteBatch.end()
    }

    override fun dispose() {
        shapeRenderer.dispose()
        spriteBatch.dispose()
        whitePawnTexture.dispose()
        blackPawnTexture.dispose()
    }
}
