package dev.terrorify.whatsmyclient

/**
 * The client a player is connected with, as detected by whatsmyclient's providers. Drives which
 * client-specific capabilities are available (e.g. Lunar's WorldEditCUI selection rendering).
 */
enum class ClientType {
    /** Unknown / vanilla — only universal capabilities apply. */
    UNKNOWN,

    /** Lunar Client (detected via Apollo). */
    LUNAR,
}
