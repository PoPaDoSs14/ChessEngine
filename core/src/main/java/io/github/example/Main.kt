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
    private var pieces = Array(boardSize) { arrayOfNulls<ChessPiece>(boardSize) }

    private lateinit var whitePawnTexture: Texture
    private lateinit var blackPawnTexture: Texture
    private lateinit var whiteRookTexture: Texture
    private lateinit var whiteElephantTexture: Texture
    private lateinit var whiteHorseTexture: Texture
    private lateinit var whiteQueenTexture: Texture
    private lateinit var whiteKingTexture: Texture
    private lateinit var blackRookTexture: Texture
    private lateinit var blackElephantTexture: Texture
    private lateinit var blackHorseTexture: Texture
    private lateinit var blackQueenTexture: Texture
    private lateinit var blackKingTexture: Texture

    private var selectedPiece: ChessPiece? = null
    private var selectedRow = -1
    private var selectedCol = -1

    private var moveNowColor = Color.WHITE
    private val playerColor = Color.BLACK
    private val botColor = Color.WHITE

    private val botColor1 = Color.BLACK
    private val botColor2 = Color.WHITE

    private var timeSinceLastMove = 0f // Время с последнего хода
    private val moveDelay = 0.5f // Задержка между ходами (в секундах)

    private val chessBot1 = ChessBot(botColor1)
    private val chessBot2 = ChessBot(botColor2)

    private var currentGameMode = GameMode.BOT_VS_BOT // Начинаем с режима "бот против бота"
    private val depth = 3

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
        whiteKingTexture = Texture("W_king.png")
        whiteRookTexture = Texture("W_rook.png")
        whiteHorseTexture = Texture("W_horse.png")
        whiteElephantTexture = Texture("W_elephant.png")
        whiteQueenTexture = Texture("W_queen.png")
        blackKingTexture = Texture("B_king.png")
        blackRookTexture = Texture("B_rook.png")
        blackHorseTexture = Texture("B_horse.png")
        blackElephantTexture = Texture("B_elephant.png")
        blackQueenTexture = Texture("B_queen.png")

    }

    private fun initializePieces() {
        // Установка черных фигур
        pieces[0][0] = ChessPiece(0, Color.BLACK, Sprite(blackRookTexture), ChessPieceType.CASTLE, RookMoveStrategy())
        pieces[0][1] = ChessPiece(0, Color.BLACK, Sprite(blackHorseTexture), ChessPieceType.HORSE, KnightMoveStrategy())
        pieces[0][2] = ChessPiece(0, Color.BLACK, Sprite(blackElephantTexture), ChessPieceType.ELEPHANT, ElephantMoveStrategy())
        pieces[0][3] = ChessPiece(0, Color.BLACK, Sprite(blackQueenTexture), ChessPieceType.QUEEN, QueenMoveStrategy())
        pieces[0][4] = ChessPiece(0, Color.BLACK, Sprite(blackKingTexture), ChessPieceType.KING, KingMoveStrategy())
        pieces[0][5] = ChessPiece(0, Color.BLACK, Sprite(blackElephantTexture), ChessPieceType.ELEPHANT, ElephantMoveStrategy())
        pieces[0][6] = ChessPiece(0, Color.BLACK, Sprite(blackHorseTexture), ChessPieceType.HORSE, KnightMoveStrategy())
        pieces[0][7] = ChessPiece(0, Color.BLACK, Sprite(blackRookTexture), ChessPieceType.CASTLE, RookMoveStrategy())

        // Установка черных пешек
        for (i in 0 until boardSize) {
            pieces[1][i] = ChessPiece(0, Color.BLACK, Sprite(blackPawnTexture), ChessPieceType.PAWN, PawnMoveStrategy()) // Черные пешки
        }

        // Установка белых пешек
        for (i in 0 until boardSize) {
            pieces[6][i] = ChessPiece(0, Color.WHITE, Sprite(whitePawnTexture), ChessPieceType.PAWN, PawnMoveStrategy()) // Белые пешки
        }

        // Установка белых фигур
        pieces[7][0] = ChessPiece(0, Color.WHITE, Sprite(whiteRookTexture), ChessPieceType.CASTLE, RookMoveStrategy())
        pieces[7][1] = ChessPiece(0, Color.WHITE, Sprite(whiteHorseTexture), ChessPieceType.HORSE, KnightMoveStrategy())
        pieces[7][2] = ChessPiece(0, Color.WHITE, Sprite(whiteElephantTexture), ChessPieceType.ELEPHANT, ElephantMoveStrategy())
        pieces[7][3] = ChessPiece(0, Color.WHITE, Sprite(whiteQueenTexture), ChessPieceType.QUEEN, QueenMoveStrategy())
        pieces[7][4] = ChessPiece(0, Color.WHITE, Sprite(whiteKingTexture), ChessPieceType.KING, KingMoveStrategy())
        pieces[7][5] = ChessPiece(0, Color.WHITE, Sprite(whiteElephantTexture), ChessPieceType.ELEPHANT, ElephantMoveStrategy())
        pieces[7][6] = ChessPiece(0, Color.WHITE, Sprite(whiteHorseTexture), ChessPieceType.HORSE, KnightMoveStrategy())
        pieces[7][7] = ChessPiece(0, Color.WHITE, Sprite(whiteRookTexture), ChessPieceType.CASTLE, RookMoveStrategy())
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f) // Устанавливаем цвет фона (белый)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val screenWidth = Gdx.graphics.width
        val screenHeight = Gdx.graphics.height
        val squareSize = Math.min(screenWidth, screenHeight) / boardSize

        // Рисуем шахматную доску
        drawBoard(squareSize)

        // Проверяем наличие королей
        if (!isKingPresent(Color.WHITE) || !isKingPresent(Color.BLACK)) {
            resetGame() // Сброс игры, если король отсутствует
            return
        }

        timeSinceLastMove += Gdx.graphics.deltaTime // Увеличиваем время с последнего хода

        when (currentGameMode) {
            GameMode.BOT_VS_BOT -> {
                if (timeSinceLastMove >= moveDelay) {
                    if (moveNowColor == botColor1 || moveNowColor == botColor2) {
                        val currentBot = if (moveNowColor == botColor1) chessBot1 else chessBot2
                        val bestMoveResult = currentBot.getBestMove(pieces, depth)
                        if (bestMoveResult != null) {
                            val (bestMove, score) = bestMoveResult
                            // Выполняем лучший ход
                            pieces[bestMove.to.first][bestMove.to.second] = bestMove.piece
                            pieces[bestMove.from.first][bestMove.from.second] = null
                            // Смена цвета после хода
                            moveNowColor = if (moveNowColor == Color.WHITE) Color.BLACK else Color.WHITE
                        }
                    }
                    timeSinceLastMove = 0f // Сбрасываем таймер после хода
                }
            }
            GameMode.PLAYER_VS_BOT -> {
                if (Gdx.input.justTouched()) {
                    // Логика для обработки ввода игрока и выполнения его хода.
                    handlePlayerInput(screenHeight, squareSize)
                } else if (timeSinceLastMove >= moveDelay && moveNowColor == botColor2) { // Если ход бота
                    val bestMoveResult = chessBot2.getBestMove(pieces, depth)
                    if (bestMoveResult != null) {
                        val (bestMove, score) = bestMoveResult
                        pieces[bestMove.to.first][bestMove.to.second] = bestMove.piece
                        pieces[bestMove.from.first][bestMove.from.second] = null
                        moveNowColor = Color.BLACK // Смена цвета после хода бота
                    }
                    timeSinceLastMove = 0f // Сбрасываем таймер после хода бота
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

    private fun handlePlayerInput(screenHeight: Int, squareSize: Int) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            val touchX = Gdx.input.x
            val touchY = screenHeight - Gdx.input.y // Переводим координаты Y в систему координат LibGDX

            val col = (touchX / squareSize).toInt()
            val row = (touchY / squareSize).toInt()

            if (row in 0 until boardSize && col in 0 until boardSize) {
                if (selectedPiece == null) { // Выбор фигуры
                    val piece = pieces[row][col]
                    if (piece != null && piece.color == playerColor) { // Проверяем цвет фигуры
                        selectedPiece = piece
                        selectedRow = row
                        selectedCol = col
                    }
                } else { // Перемещение фигуры
                    val validMoves = selectedPiece!!.getValidMoves(selectedRow, selectedCol, pieces)
                    if (validMoves.contains(Pair(row, col))) {
                        pieces[row][col] = selectedPiece
                        pieces[selectedRow][selectedCol] = null
                        // Смена цвета после хода
                        moveNowColor = if (moveNowColor == Color.WHITE) Color.BLACK else Color.WHITE
                    }
                    selectedPiece = null // Сброс выбора
                }
            }
        }
    }

    private fun isKingPresent(color: Color): Boolean {
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                val piece = pieces[row][col]
                if (piece?.pieceType == ChessPieceType.KING && piece?.color == color) {
                    return true // Король найден
                }
            }
        }
        return false // Король не найден
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

    private fun resetGame() {
        pieces = Array(boardSize) { arrayOfNulls<ChessPiece>(boardSize) }
        moveNowColor = playerColor
        selectedPiece = null
        selectedRow = -1
        selectedCol = -1

        initializePieces()
    }

    private fun drawPieces(squareSize: Int) {
        spriteBatch.begin()
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                pieces[row][col]?.let { piece ->
                    val x = (col * squareSize + (squareSize - piece.chessSprite.width) / 2)
                    val y = (row * squareSize + (squareSize - piece.chessSprite.height) / 2)

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
