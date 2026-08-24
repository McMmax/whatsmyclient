package dev.terrorify.whatsmyclient.norisk

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerPluginMessageEvent
import net.minestom.server.network.packet.server.play.PluginMessagePacket
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap

/**
 * NoRisk Client Server API Protocol Bridge.
 *
 * Implements communication channels for NoRisk Client:
 * - `norisk:client` / `norisk:core`: Handshake, detection, and profile verification.
 * - `nrc:play`: Mod enforcement, waypoints, and notifications.
 */
object NoRiskProtocol {

    const val CHANNEL_CLIENT = "norisk:client"
    const val CHANNEL_CORE = "norisk:core"
    const val CHANNEL_PLAY = "nrc:play"

    private val noRiskUsers = ConcurrentHashMap.newKeySet<Player>()

    /** Wire up NoRisk channel listeners in Minestom. */
    fun install() {
        val node = MinecraftServer.getGlobalEventHandler()

        node.addListener(PlayerPluginMessageEvent::class.java) { event ->
            val identifier = event.identifier
            if (identifier == CHANNEL_CLIENT || identifier == CHANNEL_CORE || identifier.startsWith("norisk:") || identifier.startsWith("nrc:")) {
                noRiskUsers.add(event.player)
            }
        }
    }

    /** Returns true if the player is confirmed running NoRisk Client. */
    fun isNoRiskUser(player: Player): Boolean = noRiskUsers.contains(player)

    /** Disables specific built-in NoRisk mods for the player (e.g. FreeLook, Fullbright, Minimap). */
    fun setDisallowedMods(player: Player, mods: Map<String, Boolean>) {
        val jsonEntries = mods.entries.joinToString(",") { (mod, disabled) ->
            "\"$mod\":{\"disabled\":$disabled}"
        }
        val payload = """{"action":"disallowed_mods","mods":{$jsonEntries}}""".toByteArray(StandardCharsets.UTF_8)
        player.sendPacket(PluginMessagePacket(CHANNEL_PLAY, payload))
    }

    /** Broadcasts a notification popup to NoRisk users. */
    fun sendNotification(player: Player, title: String, message: String) {
        val payload = """{"action":"notification","title":"$title","message":"$message"}"""
            .toByteArray(StandardCharsets.UTF_8)
        player.sendPacket(PluginMessagePacket(CHANNEL_PLAY, payload))
    }
}
