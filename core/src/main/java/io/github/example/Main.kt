package io.github.example

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Array

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
class Main : ApplicationAdapter() {
    private lateinit var shapeRenderer: ShapeRenderer
    private val boardSize = 8

    override fun create() {
        shapeRenderer = ShapeRenderer()
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
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}
