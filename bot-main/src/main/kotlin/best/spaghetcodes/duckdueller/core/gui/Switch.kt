package best.spaghetcodes.duckdueller.core.gui

import best.spaghetcodes.duckdueller.core.Config
import net.minecraft.client.Minecraft
import java.awt.Color
import kotlin.reflect.KMutableProperty0
class Switch(
    private val text: String,
    private val prop: KMutableProperty0<Boolean>
) : Component(0, 0, 0, 26) {

    private val font = Minecraft.getMinecraft().fontRendererObj

    private val enabledColor = Color(225, 100, 0, 255)
    private val disabledColor = Color(60, 60, 60, 255)
    private val knobColor = Color(255, 255, 255, 255)
    private val hoverColor = Color(200, 200, 200, 50)
    private val textColor = Color.WHITE

    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (isMouseOver(mouseX, mouseY)) {
            GLUtils.drawRect(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), hoverColor)
        }
        val textY = y + (height - font.FONT_HEIGHT) / 2 + 1
        font.drawStringWithShadow(text, (x + 4).toFloat(), textY.toFloat(), textColor.rgb)

        val enabled = prop.get()
        val switchWidth = 28f
        val switchHeight = 12f
        val switchX = (x + width - switchWidth - 4).toFloat()
        val switchY = (y + (height - switchHeight) / 2).toFloat()
        val trackCornerRadius = 6f

        val knobSize = 8f
        val knobY = (y + (height - knobSize) / 2).toFloat()
        val knobCornerRadius = 4f
        val knobX = if (enabled) {
            switchX + switchWidth - knobSize - 2f
        } else {
            switchX + 2f
        }

        val trackColor = if (enabled) enabledColor else disabledColor
        GLUtils.drawRoundedRect(switchX, switchY, switchWidth, switchHeight, trackCornerRadius, trackColor)
        GLUtils.drawRoundedRect(knobX, knobY, knobSize, knobSize, knobCornerRadius, knobColor)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        if (isMouseOver(mouseX, mouseY) && mouseButton == 0) {
            prop.set(!prop.get())
            Config.save()
        }
        return false
    }
}