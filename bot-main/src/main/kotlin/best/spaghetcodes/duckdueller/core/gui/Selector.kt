package best.spaghetcodes.duckdueller.core.gui

import best.spaghetcodes.duckdueller.core.Config
import net.minecraft.client.Minecraft
import java.awt.Color
import kotlin.reflect.KMutableProperty0
class Selector(
    private val text: String,
    private val prop: KMutableProperty0<Int>,
    private val options: List<String>
) : Component(0, 0, 0, 26) {

    private val font = Minecraft.getMinecraft().fontRendererObj

    private val valueColor = Color(225, 100, 0)
    private val hoverColor = Color(200, 200, 200, 50)
    private val labelColor = Color.WHITE

    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (isMouseOver(mouseX, mouseY)) {
            GLUtils.drawRect(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), hoverColor)
        }

        val currentIndex = prop.get()
        val currentOptionText = if (currentIndex in options.indices) "< ${options[currentIndex]} >" else "< Invalid >"
        val textY = y + (height - font.FONT_HEIGHT) / 2 + 1

        font.drawStringWithShadow(text, (x + 4).toFloat(), textY.toFloat(), labelColor.rgb)
        font.drawStringWithShadow(currentOptionText, (x + width - font.getStringWidth(currentOptionText) - 4).toFloat(), textY.toFloat(), valueColor.rgb)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        if (isMouseOver(mouseX, mouseY)) {
            val currentIndex = prop.get()
            if (mouseButton == 0) {
                prop.set((currentIndex + 1) % options.size)
            } else if (mouseButton == 1) {
                prop.set((currentIndex - 1 + options.size) % options.size)
            }
            Config.save()
        }
        return false
    }
}