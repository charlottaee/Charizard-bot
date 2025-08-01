package best.spaghetcodes.duckdueller.core

import best.spaghetcodes.duckdueller.core.gui.Gui
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import org.lwjgl.input.Keyboard

object KeyBindings {
    val toggleBotKeyBinding = KeyBinding("charizard.toggleBot", Keyboard.KEY_SEMICOLON, "Charizard")
    val configGuiKeyBinding = KeyBinding("charizard.configGui", Keyboard.KEY_RSHIFT, "Charizard")

    fun register() {
        ClientRegistry.registerKeyBinding(toggleBotKeyBinding)
        ClientRegistry.registerKeyBinding(configGuiKeyBinding)
    }

    @SubscribeEvent
    fun onTick(ev: ClientTickEvent) {
        if (configGuiKeyBinding.isPressed) {
            Minecraft.getMinecraft().displayGuiScreen(Gui())
        }
    }

}