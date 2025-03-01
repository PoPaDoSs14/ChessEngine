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
    private val squareSize = 50 // Размер квадрата в пикселях

    override fun create() {
        shapeRenderer = ShapeRenderer()
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                val color = if ((row + col) % 2 == 0) Color.WHITE else Color.BLACK
                shapeRenderer.setColor(color)

                shapeRenderer.rect(col * squareSize.toFloat(), row * squareSize.toFloat(), squareSize.toFloat(), squareSize.toFloat())
            }
        }

        shapeRenderer.end()
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}
