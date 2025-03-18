package io.github.example

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
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

    private var selectedPiece: ChessPiece? = null
    private var selectedRow = -1
    private var selectedCol = -1

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
        pieces[0][0] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_rook.png")), ChessPieceType.CASTLE, RookMoveStrategy())
        pieces[0][1] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_horse.png")), ChessPieceType.HORSE, KnightMoveStrategy())
        pieces[0][2] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_elephant.png")), ChessPieceType.ELEPHANT, PawnMoveStrategy())
        pieces[0][3] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_queen.png")), ChessPieceType.QUEEN, PawnMoveStrategy())
        pieces[0][4] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_king.png")), ChessPieceType.KING, PawnMoveStrategy())
        pieces[0][5] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_elephant.png")), ChessPieceType.ELEPHANT, PawnMoveStrategy())
        pieces[0][6] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_horse.png")), ChessPieceType.HORSE, KnightMoveStrategy())
        pieces[0][7] = ChessPiece(0, Color.BLACK, Sprite(Texture("B_rook.png")), ChessPieceType.CASTLE, RookMoveStrategy())

        // Установка черных пешек
        for (i in 0 until boardSize) {
            pieces[1][i] = ChessPiece(0, Color.BLACK, Sprite(blackPawnTexture), ChessPieceType.PAWN, PawnMoveStrategy()) // Черные пешки
        }

        // Установка белых пешек
        for (i in 0 until boardSize) {
            pieces[6][i] = ChessPiece(0, Color.WHITE, Sprite(whitePawnTexture), ChessPieceType.PAWN, PawnMoveStrategy()) // Белые пешки
        }

        // Установка белых фигур
        pieces[7][0] = ChessPiece(0, Color.WHITE, Sprite(Texture("W_rook.png")), ChessPieceType.CASTLE, RookMoveStrategy())
        pieces[7][1] = ChessPiece(0, Color.WHITE, Sprite(Texture("W_horse.png")), ChessPieceType.HORSE, KnightMoveStrategy())
        pieces[7][2] = ChessPiece(0, Color.WHITE, Sprite(Texture("W_elephant.png")), ChessPieceType.ELEPHANT, PawnMoveStrategy())
        pieces[7][3] = ChessPiece(0, Color.WHITE, Sprite(Texture("W_queen.png")), ChessPieceType.QUEEN, PawnMoveStrategy())
        pieces[7][4] = ChessPiece(0, Color.WHITE, Sprite(Texture("W_king.png")), ChessPieceType.KING, PawnMoveStrategy())
        pieces[7][5] = ChessPiece(0, Color.WHITE, Sprite(Texture("W_elephant.png")), ChessPieceType.ELEPHANT, PawnMoveStrategy())
        pieces[7][6] = ChessPiece(0, Color.WHITE, Sprite(Texture("W_horse.png")), ChessPieceType.HORSE, KnightMoveStrategy())
        pieces[7][7] = ChessPiece(0, Color.WHITE, Sprite(Texture("W_rook.png")), ChessPieceType.CASTLE, RookMoveStrategy())
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f) // Устанавливаем цвет фона (белый)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val screenWidth = Gdx.graphics.width
        val screenHeight = Gdx.graphics.height
        val squareSize = Math.min(screenWidth, screenHeight) / boardSize

        // Рисуем шахматную доску
        drawBoard(squareSize)

        // Обработка ввода при нажатии мыши
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            val touchX = Gdx.input.x
            val touchY = screenHeight - Gdx.input.y // Переводим координаты Y в систему координат LibGDX

            val col = (touchX / squareSize).toInt()
            val row = (touchY / squareSize).toInt()

            if (row in 0 until boardSize && col in 0 until boardSize) {
                if (selectedPiece == null) { // Выбор фигуры
                    selectedPiece = pieces[row][col]
                    if (selectedPiece != null) {
                        selectedRow = row
                        selectedCol = col
                    }
                } else { // Перемещение фигуры
                    val validMoves = selectedPiece!!.getValidMoves(selectedRow, selectedCol, pieces)
                    if (validMoves.contains(Pair(row, col))) {
                        pieces[row][col] = selectedPiece
                        pieces[selectedRow][selectedCol] = null
                    }
                    selectedPiece = null // Сброс выбора
                }
            }
        }

        // Отображаем возможные ходы, если фигура выбрана
        if (selectedPiece != null) {
            val validMoves = selectedPiece!!.getValidMoves(selectedRow, selectedCol, pieces)
            for (move in validMoves) {
                drawCircleOnSquare(squareSize, move.first, move.second) // Рисуем кружок на допустимых клетках
            }
        }

        // Отображаем фигуры
        drawPieces(squareSize)
    }

    private fun drawCircleOnSquare(squareSize: Int, row: Int, col: Int) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color.BLACK
        shapeRenderer.circle((col * squareSize + squareSize / 2).toFloat(), (row * squareSize + squareSize / 2).toFloat(), squareSize / 8f) // Кружок размером в 1/8 клетки
        shapeRenderer.end()
    }

    private fun drawBoard(squareSize: Int) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                val color = if ((row + col) % 2 == 0) Color(0.8f, 0.7f, 0.5f, 1f) else Color(0.4f, 0.2f, 0.1f, 1f)
                shapeRenderer.setColor(color)
                shapeRenderer.rect(col * squareSize.toFloat(), row * squareSize.toFloat(), squareSize.toFloat(), squareSize.toFloat())
            }
        }
        shapeRenderer.end()
    }

    private fun drawPieces(squareSize: Int) {
        spriteBatch.begin()
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                pieces[row][col]?.let { piece ->
                    val x = (col * squareSize + (squareSize - piece.chessSprite.width) / 2).toFloat()
                    val y = (row * squareSize + (squareSize - piece.chessSprite.height) / 2).toFloat()

                    piece.chessSprite.setSize(squareSize * 0.8f, squareSize * 0.8f)
                    piece.chessSprite.setPosition(x, y)
                    piece.chessSprite.draw(spriteBatch)
                }
            }
        }
        spriteBatch.end()

        // Начинаем рисовать выделение после отрисовки всех фигур
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)

        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                pieces[row][col]?.let { piece ->
                    // Выполняем выделение только для выбранной фигуры
                    if (selectedPiece == piece) {
                        val x = (col * squareSize + (squareSize - piece.chessSprite.width) / 2).toFloat()
                        val y = (row * squareSize + (squareSize - piece.chessSprite.height) / 2).toFloat()

                        shapeRenderer.color = Color.RED // Цвет выделения
                        shapeRenderer.rect(x, y, squareSize * 0.8f, squareSize * 0.8f) // Рисуем рамку выделения
                    }
                }
            }
        }
        shapeRenderer.end()
    }

    override fun dispose() {
        shapeRenderer.dispose()
        spriteBatch.dispose()
        whitePawnTexture.dispose()
        blackPawnTexture.dispose()
    }
}
