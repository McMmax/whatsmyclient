package dev.terrorify.whatsmyclient

/**
 * Recognizable Minecraft client distributions.
 */
enum class ClientType {
    UNKNOWN,
    VANILLA,
    LUNAR_CLIENT,
    LABYMOD,
    BADLION,
    FEATHER,
    FABRIC,
    FORGE,
    GEYSER;

    val isCustomClient: Boolean
        get() = this != UNKNOWN && this != VANILLA
}
