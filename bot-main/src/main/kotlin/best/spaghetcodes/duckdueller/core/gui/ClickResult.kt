package best.spaghetcodes.duckdueller.core.gui

data class ClickResult(
    val needsLayoutUpdate: Boolean = false,
    val focusedTextField: TextField? = null
)