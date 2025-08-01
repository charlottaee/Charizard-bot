package best.spaghetcodes.duckdueller.core.gui

import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

object GLUtils {

    private fun setupRenderState() {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glDisable(GL11.GL_CULL_FACE)
        GL11.glShadeModel(GL11.GL_SMOOTH)
    }

    private fun restoreRenderState() {
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glShadeModel(GL11.GL_FLAT)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glPopAttrib()
    }

    private fun setColor(color: Color) {
        GL11.glColor4f(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
    }

    private fun _drawRect(x: Float, y: Float, width: Float, height: Float) {
        val x2 = x + width
        val y2 = y + height
        GL11.glBegin(GL11.GL_QUADS)
        GL11.glVertex2f(x, y2)
        GL11.glVertex2f(x2, y2)
        GL11.glVertex2f(x2, y)
        GL11.glVertex2f(x, y)
        GL11.glEnd()
    }

    private fun _drawArc(x: Float, y: Float, radius: Float, startAngle: Int, endAngle: Int) {
        GL11.glBegin(GL11.GL_TRIANGLE_FAN)
        GL11.glVertex2f(x, y)
        for (i in startAngle..endAngle) {
            val angle = Math.toRadians(i.toDouble())
            GL11.glVertex2f(x + (cos(angle) * radius).toFloat(), y + (sin(angle) * radius).toFloat())
        }
        GL11.glEnd()
    }

    fun drawRoundedRect(x: Float, y: Float, width: Float, height: Float, radius: Float, color: Color) {
        if (radius <= 0) {
            drawRect(x, y, width, height, color)
            return
        }

        setupRenderState()
        val transparentColor = Color(color.red, color.green, color.blue, (color.alpha * 0.9f).toInt())
        setColor(transparentColor)

        val x2 = x + width
        val y2 = y + height
        _drawRect(x, y + radius, width, height - radius * 2)
        _drawRect(x + radius, y, width - radius * 2, height)
        _drawArc(x + radius, y + radius, radius, 180, 270)
        _drawArc(x2 - radius, y + radius, radius, 270, 360)
        _drawArc(x2 - radius, y2 - radius, radius, 0, 90)
        _drawArc(x + radius, y2 - radius, radius, 90, 180)

        restoreRenderState()
    }

    fun drawGradientRoundedRect(x: Float, y: Float, width: Float, height: Float, radius: Float, colorTop: Color, colorBottom: Color) {
        setupRenderState()
        val x2 = x + width
        val y2 = y + height
        val transparentTop = Color(colorTop.red, colorTop.green, colorTop.blue, (colorTop.alpha * 0.9f).toInt())
        val transparentBottom = Color(colorBottom.red, colorBottom.green, colorBottom.blue, (colorBottom.alpha * 0.9f).toInt())
        GL11.glBegin(GL11.GL_QUADS)
        setColor(transparentTop)
        GL11.glVertex2f(x + radius, y + radius)
        GL11.glVertex2f(x2 - radius, y + radius)
        setColor(transparentBottom)
        GL11.glVertex2f(x2 - radius, y2 - radius)
        GL11.glVertex2f(x + radius, y2 - radius)
        GL11.glEnd()
        GL11.glBegin(GL11.GL_QUADS)
        setColor(transparentTop)
        GL11.glVertex2f(x, y + radius)
        GL11.glVertex2f(x + radius, y + radius)
        setColor(transparentBottom)
        GL11.glVertex2f(x + radius, y2 - radius)
        GL11.glVertex2f(x, y2 - radius)
        GL11.glEnd()

        GL11.glBegin(GL11.GL_QUADS)
        setColor(transparentTop)
        GL11.glVertex2f(x2 - radius, y + radius)
        GL11.glVertex2f(x2, y + radius)
        setColor(transparentBottom)
        GL11.glVertex2f(x2, y2 - radius)
        GL11.glVertex2f(x2 - radius, y2 - radius)
        GL11.glEnd()
        GL11.glBegin(GL11.GL_QUADS)
        setColor(transparentTop)
        GL11.glVertex2f(x + radius, y)
        GL11.glVertex2f(x2 - radius, y)
        GL11.glVertex2f(x2 - radius, y + radius)
        GL11.glVertex2f(x + radius, y + radius)
        GL11.glEnd()

        GL11.glBegin(GL11.GL_QUADS)
        setColor(transparentBottom)
        GL11.glVertex2f(x + radius, y2 - radius)
        GL11.glVertex2f(x2 - radius, y2 - radius)
        GL11.glVertex2f(x2 - radius, y2)
        GL11.glVertex2f(x + radius, y2)
        GL11.glEnd()
        drawGradientArc(x + radius, y + radius, radius, 180, 270, transparentTop)
        drawGradientArc(x2 - radius, y + radius, radius, 270, 360, transparentTop)
        drawGradientArc(x2 - radius, y2 - radius, radius, 0, 90, transparentBottom)
        drawGradientArc(x + radius, y2 - radius, radius, 90, 180, transparentBottom)

        restoreRenderState()
    }

    fun drawGradientRoundedTopRect(x: Float, y: Float, width: Float, height: Float, radius: Float, colorTop: Color, colorBottom: Color) {
        setupRenderState()
        val x2 = x + width
        val y2 = y + height
        val transparentTop = Color(colorTop.red, colorTop.green, colorTop.blue, (colorTop.alpha * 0.9f).toInt())
        val transparentBottom = Color(colorBottom.red, colorBottom.green, colorBottom.blue, (colorBottom.alpha * 0.9f).toInt())
        GL11.glBegin(GL11.GL_QUADS)
        setColor(transparentTop)
        GL11.glVertex2f(x, y + radius)
        GL11.glVertex2f(x2, y + radius)
        setColor(transparentBottom)
        GL11.glVertex2f(x2, y2)
        GL11.glVertex2f(x, y2)
        GL11.glEnd()
        setColor(transparentTop)
        _drawRect(x + radius, y, width - radius * 2, radius)
        _drawArc(x + radius, y + radius, radius, 180, 270)
        _drawArc(x2 - radius, y + radius, radius, 270, 360)

        restoreRenderState()
    }

    fun drawRect(x: Float, y: Float, width: Float, height: Float, color: Color) {
        setupRenderState()
        setColor(color)
        _drawRect(x,y,width,height)
        restoreRenderState()
    }

    private fun drawGradientArc(x: Float, y: Float, radius: Float, startAngle: Int, endAngle: Int, centerColor: Color) {
        GL11.glBegin(GL11.GL_TRIANGLE_FAN)
        setColor(centerColor)
        GL11.glVertex2f(x, y)
        for (i in startAngle..endAngle) {
            val angle = Math.toRadians(i.toDouble())
            GL11.glVertex2f(x + (cos(angle) * radius).toFloat(), y + (sin(angle) * radius).toFloat())
        }
        GL11.glEnd()
    }
}