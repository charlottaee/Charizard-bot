package best.spaghetcodes.duckdueller.core.gui

import net.minecraft.client.Minecraft
import java.awt.Color

class Frame(
    x: Int, y: Int, width: Int,
    val title: String,
    val components: ArrayList<Component>
) : Component(x, y, width, 0) {

    private var dragging = false
    private var dragX = 0
    private var dragY = 0

    var isOpen = true
    private val titleBarHeight = 16
    private val componentGap = 8
    private val cornerRadius = 4f
    private val titleGradientTop = Color(230, 140, 0, 255)
    private val titleGradientBottom = Color(200, 90, 0, 255)
    private val backgroundColor = Color(0, 0, 0, 255)
    private val titleTextColor = Color.WHITE

    private val openHeight: Int

    override var height: Int
        get() = if (isOpen) openHeight else titleBarHeight
        set(value) { /* Read-only */ }

    init {
        var calculatedHeight = titleBarHeight
        if (components.isNotEmpty()) {
            calculatedHeight += components.sumBy { it.height }
            calculatedHeight += (components.size) * componentGap
        }
        openHeight = calculatedHeight
        updateComponentPositions()
    }
    fun processClick(mouseX: Int, mouseY: Int, mouseButton: Int): ClickResult {
        if (isMouseOverTitle(mouseX, mouseY)) {
            when (mouseButton) {
                0 -> {
                    dragging = true
                    dragX = mouseX - x
                    dragY = mouseY - y
                }
                1 -> {
                    isOpen = !isOpen
                    return ClickResult(needsLayoutUpdate = true)
                }
            }
        } else if (isOpen && isMouseOver(mouseX, mouseY)) {
            for (component in components) {
                if (component.isMouseOver(mouseX, mouseY)) {
                    component.mouseClicked(mouseX, mouseY, mouseButton)
                    if (component is TextField) {
                        return ClickResult(focusedTextField = component)
                    }
                    break
                }
            }
        }
        return ClickResult()
    }

    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (dragging) {
            this.x = mouseX - dragX
            this.y = mouseY - dragY
        }
        GLUtils.drawRoundedRect(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), cornerRadius, backgroundColor)
        GLUtils.drawGradientRoundedTopRect(x.toFloat(), y.toFloat(), width.toFloat(), titleBarHeight.toFloat(), cornerRadius, titleGradientTop, titleGradientBottom)
        val font = Minecraft.getMinecraft().fontRendererObj
        font.drawStringWithShadow(title, (x + 5).toFloat(), (y + (titleBarHeight - font.FONT_HEIGHT) / 2).toFloat(), titleTextColor.rgb)
        if (isOpen) {
            updateComponentPositions()
            for (component in components) {
                component.draw(mouseX, mouseY, partialTicks)
            }
        }
    }

    private fun updateComponentPositions() {
        var yOffset = titleBarHeight + componentGap
        for (component in components) {
            component.x = this.x
            component.y = this.y + yOffset
            component.width = this.width
            yOffset += component.height + componentGap
        }
    }
    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        return false
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        if (state == 0) dragging = false
        if (isOpen) {
            for (component in components) {
                component.mouseReleased(mouseX, mouseY, state)
            }
        }
    }

    fun handleMouseDrag(mouseX: Int, mouseY: Int) {
        if (isOpen) {
            for (component in components) {
                if (component is Slider) {
                    component.mouseDragged(mouseX)
                }
            }
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (isOpen) {
            for (component in components) {
                component.keyTyped(typedChar, keyCode)
            }
        }
    }

    fun isCurrentlyDragging(): Boolean = dragging

    private fun isMouseOverTitle(mouseX: Int, mouseY: Int): Boolean {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + titleBarHeight
    }
}