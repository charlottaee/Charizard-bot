package best.spaghetcodes.duckdueller.core

import best.spaghetcodes.duckdueller.DuckDueller
import best.spaghetcodes.duckdueller.bot.bots.*
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import java.io.File
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

object Config {

    private val gson = GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create()
    private val configFile = File(DuckDueller.configLocation)
    @Expose var currentBot = 0
        set(value) {
            field = value
            if (bots.containsKey(value)) {
                DuckDueller.swapBot(bots[value]!!)
            }
        }
    @Expose var apiKey = ""
    @Expose var lobbyMovement = true
    @Expose var disableChatMessages = false
    @Expose var throwAfterGames = 0
    @Expose var disconnectAfterGames = 0
    @Expose var disconnectAfterMinutes = 0
    @Expose var minCPS = 10
    @Expose var maxCPS = 14
    @Expose var lookSpeedHorizontal = 10
    @Expose var lookSpeedVertical = 5
    @Expose var lookRand = 0.3f
    @Expose var maxDistanceLook = 150
    @Expose var maxDistanceAttack = 5
    @Expose var sendAutoGG = true
    @Expose var ggMessage = "gg"
    @Expose var ggDelay = 100
    @Expose var sendStartMessage = false
    @Expose var startMessage = "GL HF!"
    @Expose var startMessageDelay = 100
    @Expose var autoRqDelay = 2500
    @Expose var rqNoGame = 30
    @Expose var paperRequeue = true
    @Expose var fastRequeue = true
    @Expose var enableDodging = true
    @Expose var dodgeWins = 4000
    @Expose var dodgeWS = 15
    @Expose var dodgeWLR = 3.0f
    @Expose var dodgePlayersList = ""
    @Expose var dodgeLostTo = true
    @Expose var dodgeNoStats = true
    @Expose var strictDodging = false
    @Expose var sendWebhookMessages = false
    @Expose var webhookURL = ""
    @Expose var sendWebhookStats = false
    @Expose var sendWebhookDodge = false
    @Expose var boxingFish = false
    val bots = mapOf(0 to Sumo(), 1 to Boxing(), 2 to Classic(), 3 to OP(), 4 to Combo())

    init {
        load()
    }

    /**
     * Legacy function from Vigilant. Does nothing, as loading is
     * now handled in the init block, but this is kept to resolve
     * "Unresolved reference" errors in other files.
     */
    fun preload() {
    }

    /**
     * Saves the current configuration to the JSON file.
     */
    fun save() {
        try {
            configFile.parentFile.mkdirs()
            configFile.writeText(gson.toJson(this))
        } catch (e: Exception) {
            System.err.println("Failed to save Charizard config!")
            e.printStackTrace()
        }
    }

    /**
     * Legacy function to ensure compatibility. It simply calls save().
     */
    fun writeData() {
        save()
    }

    /**
     * Loads the configuration from the JSON file.
     */
    fun load() {
        try {
            if (configFile.exists()) {
                val loadedConfig = gson.fromJson(configFile.readText(), this::class.java)
                this::class.memberProperties.forEach { prop ->
                    if (prop is KMutableProperty<*>) {
                        val field = prop.javaField
                        if (field != null) {
                            field.isAccessible = true
                            val loadedValue = field.get(loadedConfig)
                            prop.setter.call(this, loadedValue)
                        }
                    }
                }
            } else {
                save()
            }
        } catch (e: Exception) {
            System.err.println("Failed to load Charizard config, it may be corrupt. Using default values.")
            e.printStackTrace()
            save()
        }
    }
}