package best.spaghetcodes.duckdueller.core.gui

import net.minecraft.client.Minecraft
import java.awt.Color

class Button(
    x: Int, y: Int, width: Int, height: Int,
    val text: String,
    private val onClick: () -> Unit
) : Component(x, y, width, height) {
    private val idleColor = Color(25, 25, 25, 220)
    private val borderColor = Color(80, 80, 80, 255)
    private val hoverGradientTop = Color(230, 140, 0, 255)
    private val hoverGradientBottom = Color(200, 90, 0, 255)
    private val textColor = Color.WHITE.rgb

    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val cornerRadius = 4f
        val isHovered = isMouseOver(mouseX, mouseY)
        if (isHovered) {
            GLUtils.drawGradientRoundedRect(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), cornerRadius, hoverGradientTop, hoverGradientBottom)
        } else {
            GLUtils.drawRoundedRect(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), cornerRadius, idleColor)
        }
        val font = Minecraft.getMinecraft().fontRendererObj
        font.drawStringWithShadow(
            text,
            (x + width / 2 - font.getStringWidth(text) / 2).toFloat(),
            (y + (height - 8) / 2).toFloat(),
            textColor
        )
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        if (isMouseOver(mouseX, mouseY) && mouseButton == 0) {
            onClick()
        }
        return false
    }
}