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
        // Установка черных фигур
        pieces[0][0] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_rook.png")), ChessPieceType.CASTLE)
        pieces[0][1] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_horse.png")), ChessPieceType.HORSE)
        pieces[0][2] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_elephant.png")), ChessPieceType.ELEPHANT)
        pieces[0][3] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_queen.png")), ChessPieceType.QUEEN)
        pieces[0][4] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_king.png")), ChessPieceType.KING)
        pieces[0][5] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_elephant.png")), ChessPieceType.ELEPHANT)
        pieces[0][6] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_horse.png")), ChessPieceType.HORSE)
        pieces[0][7] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_rook.png")), ChessPieceType.CASTLE)

        // Установка черных пешек
        for (i in 0 until boardSize) {
            pieces[1][i] = ChessPiece(0, Color.BLACK, Sprite(blackPawnTexture), ChessPieceType.PAWN) // Черные пешки
        }

        // Установка белых пешек
        for (i in 0 until boardSize) {
            pieces[6][i] = ChessPiece(0, Color.WHITE, Sprite(whitePawnTexture), ChessPieceType.PAWN) // Белые пешки
        }

        // Установка белых фигур
        pieces[7][0] = ChessPiece(0, Color.WHITE, Sprite(Texture("B_rook.png")), ChessPieceType.CASTLE)
        pieces[7][1] = ChessPiece(0, Color.WHITE, Sprite(Texture("B_horse.png")), ChessPieceType.HORSE)
        pieces[7][2] = ChessPiece(0, Color.WHITE, Sprite(Texture("B_elephant.png")), ChessPieceType.ELEPHANT)
        pieces[7][3] = ChessPiece(0, Color.WHITE, Sprite(Texture("B_queen.png")), ChessPieceType.QUEEN)
        pieces[7][4] = ChessPiece(0, Color.WHITE, Sprite(Texture("W_king.png")), ChessPieceType.KING)
        pieces[7][5] = ChessPiece(0, Color.WHITE, Sprite(Texture("B_elephant.png")), ChessPieceType.ELEPHANT)
        pieces[7][6] = ChessPiece(0, Color.WHITE, Sprite(Texture("B_horse.png")), ChessPieceType.HORSE)
        pieces[7][7] = ChessPiece(0, Color.WHITE, Sprite(Texture("B_rook.png")), ChessPieceType.CASTLE)
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
                    val x = (col * squareSize + (squareSize - piece.sprite.width) / 2).toFloat()
                    val y = (row * squareSize + (squareSize - piece.sprite.height) / 2).toFloat()

                    // Установка размера спрайта
                    piece.sprite.setSize(squareSize * 0.8f, squareSize * 0.8f) // Устанавливаем размеры фигуры в 80% от размера квадрата
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
