package best.spaghetcodes.duckdueller.core.gui

import best.spaghetcodes.duckdueller.core.Config
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Keyboard

class Gui : GuiScreen() {

    private val frames = ArrayList<Frame>()
    private val topRowFrames = ArrayList<Frame>()
    private val bottomRowFrames = ArrayList<Frame>()
    private var focusedTextField: TextField? = null

    private val frameWidth = 155
    private val horizontalGap = 15
    private val verticalGap = 15
    private val topRowY = 20

    override fun initGui() {
        super.initGui()
        frames.clear()
        topRowFrames.clear()
        bottomRowFrames.clear()
        focusedTextField = null
        Keyboard.enableRepeatEvents(true)

        topRowFrames.add(Frame(0, 0, frameWidth, "General", buildGeneralComponents()))
        topRowFrames.add(Frame(0, 0, frameWidth, "Combat", buildCombatComponents()))
        topRowFrames.add(Frame(0, 0, frameWidth, "Queue Dodging", buildDodgeComponents()))
        topRowFrames.add(Frame(0, 0, frameWidth, "AutoGG", buildAutoGgComponents()))

        bottomRowFrames.add(Frame(0, 0, frameWidth, "Auto Requeue", buildRequeueComponents()))
        bottomRowFrames.add(Frame(0, 0, frameWidth, "Webhook", buildWebhookComponents()))
        bottomRowFrames.add(Frame(0, 0, frameWidth, "Misc", buildMiscComponents()))

        frames.addAll(topRowFrames)
        frames.addAll(bottomRowFrames)

        updateLayout()
    }

    private fun updateLayout() {
        var currentX = 20
        for (frame in topRowFrames) {
            frame.x = currentX
            frame.y = topRowY
            currentX += frame.width + horizontalGap
        }

        val tallestTopFrameHeight = topRowFrames.maxByOrNull { it.height }?.height ?: 0
        val bottomRowY = topRowY + tallestTopFrameHeight + verticalGap

        currentX = 20
        for (frame in bottomRowFrames) {
            frame.x = currentX
            frame.y = bottomRowY
            currentX += frame.width + horizontalGap
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        for (frame in frames) {
            frame.draw(mouseX, mouseY, partialTicks)
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        var layoutNeedsUpdate = false
        var newlyFocused: TextField? = null
        for (frame in frames.asReversed()) {
            if (frame.isMouseOver(mouseX, mouseY)) {
                val result = frame.processClick(mouseX, mouseY, mouseButton)
                if (result.needsLayoutUpdate) layoutNeedsUpdate = true
                if (result.focusedTextField != null) newlyFocused = result.focusedTextField
                break
            }
        }
        if (newlyFocused == null) {
            if (focusedTextField != null) {
                focusedTextField?.unfocus()
                focusedTextField = null
            }
        }
        else if (newlyFocused != focusedTextField) {
            focusedTextField?.unfocus()
            focusedTextField = newlyFocused
            focusedTextField?.focus()
        }

        if (layoutNeedsUpdate) {
            updateLayout()
        }
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        var isDragging = false
        for (frame in frames) {
            frame.handleMouseDrag(mouseX, mouseY)
            if (frame.isCurrentlyDragging()) {
                isDragging = true
            }
        }
        if (isDragging) {
            updateLayout()
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        for (frame in frames) {
            frame.mouseReleased(mouseX, mouseY, state)
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (focusedTextField != null) {
            focusedTextField!!.keyTyped(typedChar, keyCode)
            if (!focusedTextField!!.isFocused()) {
                focusedTextField = null
            }
        } else {
            if (keyCode == Keyboard.KEY_ESCAPE || keyCode == this.mc.gameSettings.keyBindInventory.keyCode) {
                this.mc.displayGuiScreen(null)
            }
        }
    }

    override fun onGuiClosed() {
        super.onGuiClosed()
        Keyboard.enableRepeatEvents(false)
        Config.save()
    }

    override fun doesGuiPauseGame() = false
    private fun buildGeneralComponents(): ArrayList<Component> = arrayListOf(
        Selector("Current Bot", Config::currentBot, Config.bots.values.map { it.javaClass.simpleName }),
        Switch("Lobby Movement", Config::lobbyMovement),
        Switch("Disable Messages", Config::disableChatMessages),
        Slider("Throw After Games", Config::throwAfterGames, 0.0, 1000.0, 10.0),
        Slider("DC After Games", Config::disconnectAfterGames, 0.0, 10000.0, 100.0),
        Slider("DC After Mins", Config::disconnectAfterMinutes, 0.0, 500.0, 30.0)
    )
    private fun buildCombatComponents(): ArrayList<Component> = arrayListOf(
        Slider("Min CPS", Config::minCPS, 6.0, 20.0, 1.0),
        Slider("Max CPS", Config::maxCPS, 6.0, 20.0, 1.0),
        Slider("Look Speed H", Config::lookSpeedHorizontal, 1.0, 15.0, 1.0),
        Slider("Look Speed V", Config::lookSpeedVertical, 1.0, 15.0, 1.0),
        Slider("Look Random", Config::lookRand, 0.0, 2.0, 0.1),
        Slider("Max Look Dist", Config::maxDistanceLook, 100.0, 180.0, 5.0),
        Slider("Max Attack Dist", Config::maxDistanceAttack, 3.0, 20.0, 1.0)
    )
    private fun buildDodgeComponents(): ArrayList<Component> = arrayListOf(
        Switch("Enable Dodging", Config::enableDodging),
        Slider("Dodge Wins", Config::dodgeWins, 500.0, 20000.0, 100.0),
        Slider("Dodge WS", Config::dodgeWS, 10.0, 100.0, 5.0),
        Slider("Dodge W/L", Config::dodgeWLR, 2.0, 15.0, 0.5),
        Switch("Dodge Lost To", Config::dodgeLostTo),
        Switch("Dodge No Stats", Config::dodgeNoStats),
        Switch("Strict Dodging", Config::strictDodging)
    )
    private fun buildAutoGgComponents(): ArrayList<Component> = arrayListOf(
        Switch("Enable AutoGG", Config::sendAutoGG),
        TextField("GG Message", Config::ggMessage),
        Slider("AutoGG Delay", Config::ggDelay, 50.0, 1000.0, 50.0),
        Switch("Game Start Msg", Config::sendStartMessage),
        TextField("Start Message", Config::startMessage),
        Slider("Start Msg Delay", Config::startMessageDelay, 50.0, 1000.0, 50.0)
    )
    private fun buildRequeueComponents(): ArrayList<Component> = arrayListOf(
        Slider("Requeue Delay", Config::autoRqDelay, 500.0, 5000.0, 50.0),
        Slider("RQ After No Game", Config::rqNoGame, 15.0, 60.0, 5.0),
        Switch("Paper Requeue", Config::paperRequeue),
        Switch("Fast Requeue", Config::fastRequeue)
    )
    private fun buildWebhookComponents(): ArrayList<Component> = arrayListOf(
        Switch("Send Webhooks", Config::sendWebhookMessages),
        TextField("Webhook URL", Config::webhookURL),
        Switch("Send Queue Stats", Config::sendWebhookStats),
        Switch("Send Dodge Alerts", Config::sendWebhookDodge)
    )
    private fun buildMiscComponents(): ArrayList<Component> = arrayListOf(
        Switch("Boxing Fish", Config::boxingFish)
    )
}