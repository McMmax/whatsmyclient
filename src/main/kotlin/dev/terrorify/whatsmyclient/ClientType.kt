package dev.terrorify.whatsmyclient

/**
 * Recognizable Minecraft client distributions.
 */
enum class ClientType {
    UNKNOWN,
    VANILLA,
    LUNAR_CLIENT,
    BADLION,
    FEATHER,
    LABYMOD,
    ALPINE,
    NORISK,
    FABRIC,
    FORGE,
    GEYSER;

    val isCustomClient: Boolean
        get() = this != UNKNOWN && this != VANILLA
}
