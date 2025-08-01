package best.spaghetcodes.duckdueller.core.gui
import best.spaghetcodes.duckdueller.core.Config
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.util.ChatAllowedCharacters
import org.lwjgl.input.Keyboard
import java.awt.Color
import kotlin.reflect.KMutableProperty0
class TextField(
    private val text: String,
    private val prop: KMutableProperty0<String>,
    private val onEnter: () -> Unit = {}
) : Component(0, 0, 0, 28) {
    private val font = Minecraft.getMinecraft().fontRendererObj
    private var focused = false
    private var cursorPosition = prop.get().length
    private var cursorBlink = 0
    private var lastBlink = System.currentTimeMillis()

    private val boxBackgroundColor = Color(20, 20, 20, 255)
    private val boxBorderColor = Color(80, 80, 80, 255)
    private val boxFocusedBorderColor = Color(225, 100, 0, 255)
    private val textColor = Color.WHITE
    private val labelColor = Color.LIGHT_GRAY

    private var originalText = ""
    fun focus() {
        if (!focused) {
            this.focused = true
            this.originalText = prop.get()
            this.cursorPosition = prop.get().length
        }
    }

    fun unfocus() {
        if (focused) {
            this.focused = false
            Config.save()
        }
    }

    fun isFocused(): Boolean = focused

    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val labelY = y + 3
        font.drawStringWithShadow(text, (x + 4).toFloat(), labelY.toFloat(), labelColor.rgb)

        val textFieldHeight = 12f
        val textFieldY = y + height - textFieldHeight - 3f
        val textFieldX = x + 4f
        val textFieldWidth = width - 8f
        val cornerRadius = 3f

        val currentBorderColor = if (focused) boxFocusedBorderColor else boxBorderColor
        GLUtils.drawRoundedRect(textFieldX - 1, textFieldY - 1, textFieldWidth + 2, textFieldHeight + 2, cornerRadius, currentBorderColor)
        GLUtils.drawRoundedRect(textFieldX, textFieldY, textFieldWidth, textFieldHeight, cornerRadius, boxBackgroundColor)

        val currentText = prop.get()
        val drawnText = font.trimStringToWidth(currentText, textFieldWidth.toInt() - 8)
        val textX = textFieldX + 4f
        val textY = textFieldY + (textFieldHeight - font.FONT_HEIGHT) / 2f + 1f
        font.drawStringWithShadow(drawnText, textX, textY, textColor.rgb)

        if (focused) {
            if (System.currentTimeMillis() - lastBlink > 500) {
                cursorBlink = 1 - cursorBlink
                lastBlink = System.currentTimeMillis()
            }
            if (cursorBlink == 1) {
                val safeCursorPosition = if (cursorPosition > drawnText.length) drawnText.length else cursorPosition
                val cursorText = drawnText.substring(0, safeCursorPosition)
                val cursorX = textX + font.getStringWidth(cursorText).toFloat()
                Gui.drawRect(cursorX.toInt(), (textFieldY + 1).toInt(), (cursorX + 1).toInt(), (textFieldY + textFieldHeight - 2).toInt(), textColor.rgb)
            }
        }
    }
    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        if (isMouseOver(mouseX, mouseY)) {
            val textX = x + 8
            var bestDistance = Int.MAX_VALUE
            var bestIndex = 0
            val currentText = prop.get()
            for (i in 0..currentText.length) {
                val dist = Math.abs(mouseX - (textX + font.getStringWidth(currentText.substring(0, i))))
                if (dist < bestDistance) {
                    bestDistance = dist
                    bestIndex = i
                }
            }
            cursorPosition = bestIndex
        }
        return false
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (!focused) return

        val currentText = prop.get()
        when (keyCode) {
            Keyboard.KEY_ESCAPE -> {
                prop.set(originalText)
                focused = false
            }
            Keyboard.KEY_RETURN, Keyboard.KEY_NUMPADENTER -> {
                focused = false
                onEnter()
                Config.save()
            }
            Keyboard.KEY_BACK -> {
                if (cursorPosition > 0) {
                    prop.set(currentText.substring(0, cursorPosition - 1) + currentText.substring(cursorPosition))
                    cursorPosition--
                }
            }
            Keyboard.KEY_DELETE -> {
                if (cursorPosition < currentText.length) {
                    prop.set(currentText.substring(0, cursorPosition) + currentText.substring(cursorPosition + 1))
                }
            }
            Keyboard.KEY_LEFT -> if (cursorPosition > 0) cursorPosition--
            Keyboard.KEY_RIGHT -> if (cursorPosition < currentText.length) cursorPosition++
            Keyboard.KEY_HOME -> cursorPosition = 0
            Keyboard.KEY_END -> cursorPosition = currentText.length
            else -> {
                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    prop.set(currentText.substring(0, cursorPosition) + typedChar + currentText.substring(cursorPosition))
                    cursorPosition++
                }
            }
        }
    }
}