package best.spaghetcodes.duckdueller.core.gui

import best.spaghetcodes.duckdueller.core.Config
import net.minecraft.client.Minecraft
import java.awt.Color
import kotlin.math.roundToInt
import kotlin.reflect.KMutableProperty0
class Slider(
    private val text: String,
    private val prop: KMutableProperty0<out Number>,
    private val min: Double,
    private val max: Double,
    private val increment: Double
) : Component(0, 0, 0, 30) {

    private var dragging = false
    private val font = Minecraft.getMinecraft().fontRendererObj

    private val sliderTrackColor = Color(60, 60, 60, 255)
    private val sliderFillColor = Color(225, 100, 0, 255)
    private val hoverColor = Color(200, 200, 200, 50)
    private val valueColor = Color.LIGHT_GRAY
    private val labelColor = Color.WHITE
    private val sliderPadding = 12

    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (isMouseOver(mouseX, mouseY) || dragging) {
            GLUtils.drawRect(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), hoverColor)
        }

        val currentValue = prop.get().toDouble()
        val displayValue = if (prop.get() is Float) "%.2f".format(currentValue) else currentValue.roundToInt().toString()
        val textY = y + 5
        font.drawStringWithShadow(text, (x + 4).toFloat(), textY.toFloat(), labelColor.rgb)
        font.drawStringWithShadow(displayValue, (x + width - font.getStringWidth(displayValue) - 4).toFloat(), textY.toFloat(), valueColor.rgb)

        val sliderX = (x + sliderPadding).toFloat()
        val sliderVisualWidth = (width - sliderPadding * 2).toFloat()
        val sliderBarY = y + height - 10f

        val sliderBarHeight = 4f
        val cornerRadius = 2f
        val fillPercent = ((currentValue - min) / (max - min)).coerceIn(0.0, 1.0)
        val fillWidth = (sliderVisualWidth * fillPercent).toFloat()

        GLUtils.drawRoundedRect(sliderX, sliderBarY, sliderVisualWidth, sliderBarHeight, cornerRadius, sliderTrackColor)

        if (fillWidth > 0) {
            GLUtils.drawRoundedRect(sliderX, sliderBarY, fillWidth, sliderBarHeight, cornerRadius, sliderFillColor)
        }
    }

    private fun updateValue(mouseX: Int) {
        val sliderLogicalX = x + sliderPadding
        val sliderLogicalWidth = width - sliderPadding * 2

        val percent = ((mouseX - sliderLogicalX).toDouble() / sliderLogicalWidth).coerceIn(0.0, 1.0)

        var value = min + (max - min) * percent
        value = (value / increment).roundToInt() * increment
        value = value.coerceIn(min, max)

        when (prop.get()) {
            is Int -> (prop as KMutableProperty0<Int>).set(value.roundToInt())
            is Float -> (prop as KMutableProperty0<Float>).set(value.toFloat())
            is Double -> (prop as KMutableProperty0<Double>).set(value)
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        if (isMouseOver(mouseX, mouseY) && mouseButton == 0) {
            dragging = true
            updateValue(mouseX)
        }
        return false
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        if (dragging && state == 0) {
            dragging = false
            Config.save()
        }
    }

    fun mouseDragged(mouseX: Int) {
        if (dragging) {
            updateValue(mouseX)
        }
    }
}