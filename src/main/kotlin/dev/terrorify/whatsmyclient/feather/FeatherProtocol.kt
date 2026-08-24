package dev.terrorify.whatsmyclient.feather

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerPluginMessageEvent
import net.minestom.server.network.packet.server.play.PluginMessagePacket
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap

/**
 * Feather Client Server API Protocol Bridge.
 *
 * Implements communication channels for Feather Client:
 * - `feather:client` / `feather:core`: Handshake, detection, and Discord Rich Presence.
 * - `feather:mods`: Mod enforcement and disabling disallowed client features.
 */
object FeatherProtocol {

    const val CHANNEL_CLIENT = "feather:client"
    const val CHANNEL_CORE = "feather:core"
    const val CHANNEL_MODS = "feather:mods"

    private val featherUsers = ConcurrentHashMap.newKeySet<Player>()

    /** Wire up Feather channel listeners in Minestom. */
    fun install() {
        val node = MinecraftServer.getGlobalEventHandler()

        node.addListener(PlayerPluginMessageEvent::class.java) { event ->
            val identifier = event.identifier
            if (identifier == CHANNEL_CLIENT || identifier == CHANNEL_CORE || identifier.startsWith("feather:")) {
                featherUsers.add(event.player)
            }
        }
    }

    /** Returns true if the player is confirmed running Feather Client. */
    fun isFeatherUser(player: Player): Boolean = featherUsers.contains(player)

    /** Disables specific built-in Feather mods for the player (e.g. Waypoints, FreeLook, Fullbright). */
    fun setDisallowedMods(player: Player, mods: Map<String, Boolean>) {
        val jsonEntries = mods.entries.joinToString(",") { (mod, disabled) ->
            "\"$mod\":{\"disabled\":$disabled}"
        }
        val payload = "{$jsonEntries}".toByteArray(StandardCharsets.UTF_8)
        player.sendPacket(PluginMessagePacket(CHANNEL_MODS, payload))
    }

    /** Updates the player's Feather Discord Rich Presence status. */
    fun setDiscordRpc(player: Player, state: String, details: String, smallImage: String? = null, largeImage: String? = null) {
        val json = buildString {
            append("""{"state":"$state","details":"$details"""")
            if (smallImage != null) append(""", "smallImage":"$smallImage"""")
            if (largeImage != null) append(""", "largeImage":"$largeImage"""")
            append("}")
        }
        val payload = json.toByteArray(StandardCharsets.UTF_8)
        player.sendPacket(PluginMessagePacket(CHANNEL_CORE, payload))
    }
}
