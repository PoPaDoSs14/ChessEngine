package io.github.example

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Array

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
class Main : ApplicationAdapter() {
    private lateinit var shapeRenderer: ShapeRenderer
    private val boardSize = 8
    private val pieces = Array(boardSize) { arrayOfNulls<ChessPiece>(boardSize) }

    override fun create() {
        shapeRenderer = ShapeRenderer()
        initializePieces()
    }

    private fun initializePieces() {
        val testTexture = Texture("libgdx.png")
        val testSprite = Sprite(testTexture)
        // Например, установка начальных фигур. Черные и белые пешки.
        for (i in 0 until boardSize) {
            pieces[1][i] = ChessPiece(0, Color.WHITE, testSprite, ChessPieceType.PAWN) // Черные пешки
            pieces[6][i] = ChessPiece(0, Color.BLACK, testSprite, ChessPieceType.PAWN) // Белые пешки
        }
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
        // Здесь можно отрисовывать фигуры. Для простоты будем рисовать кружки вместо реальных фигур.
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                pieces[row][col]?.let { piece ->
                    shapeRenderer.setColor(piece.color)
                    val x = (col * squareSize + squareSize / 2).toFloat()
                    val y = (row * squareSize + squareSize / 2).toFloat()
                    shapeRenderer.circle(x, y, squareSize / 4f)
                }
            }
        }
        shapeRenderer.end()
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}
