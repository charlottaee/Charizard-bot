package best.spaghetcodes.duckdueller.core.gui

import net.minecraft.client.gui.Gui

abstract class Component(var x: Int, var y: Int, var width: Int, open var height: Int) {

    abstract fun draw(mouseX: Int, mouseY: Int, partialTicks: Float)

    abstract fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int): Boolean

    open fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {}

    open fun keyTyped(typedChar: Char, keyCode: Int) {}

    fun isMouseOver(mouseX: Int, mouseY: Int): Boolean {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height
    }
}