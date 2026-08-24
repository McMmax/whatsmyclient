package dev.terrorify.whatsmyclient

import dev.terrorify.whatsmyclient.lunar.WorldEditCui
import net.minestom.server.entity.Player

/**
 * Universal client-capability API. Minestom-native today; the provider seam (currently Lunar Client)
 * is meant to extend to Spigot/Fabric backends later.
 *
 * Capabilities are exposed as plain calls that route to the player's detected client. The first
 * capability is **selection visuals**, drawn via Lunar's built-in WorldEditCUI (the `worldedit:cui`
 * channel) — no client mod needed for Lunar users. Apollo-based detection + the CUI enable toggle are
 * layered on next; for now WECUI messages are primed on join and sent to everyone (non-WECUI clients
 * ignore them).
 */
object WhatsMyClient {

    /** Wire up provider hooks. Call once after the server is initialized, before players join. */
    @JvmStatic
    fun init() {
        WorldEditCui.install()
    }

    /** Detected client for a player. (Lunar detection via Apollo is added with the Apollo provider.) */
    @JvmStatic
    fun clientType(@Suppress("UNUSED_PARAMETER") player: Player): ClientType = ClientType.UNKNOWN

    // ── Selection visuals ─────────────────────────────────────────────────────────────────────

    /** Show a cuboid selection box defined by two corners. */
    @JvmStatic
    fun showSelection(player: Player, x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) {
        WorldEditCui.showCuboid(player, x1, y1, z1, x2, y2, z2)
    }

    /** Hide any selection box. */
    @JvmStatic
    fun clearSelection(player: Player) {
        WorldEditCui.clear(player)
    }
}
