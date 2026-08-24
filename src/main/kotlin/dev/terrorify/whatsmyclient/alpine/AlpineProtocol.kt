package dev.terrorify.whatsmyclient.alpine

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerPluginMessageEvent
import net.minestom.server.network.packet.server.play.PluginMessagePacket
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap

/**
 * Alpine Client (Ridge) Server Protocol Bridge.
 *
 * Implements communication channels for Alpine Client:
 * - `ac:handshake`: Initial client identification handshake.
 * - `ac:play`: Dynamic server notifications, cooldown bars, and mod disallows.
 */
object AlpineProtocol {

    const val CHANNEL_HANDSHAKE = "ac:handshake"
    const val CHANNEL_PLAY = "ac:play"

    private val alpineUsers = ConcurrentHashMap.newKeySet<Player>()

    /** Wire up Alpine channel listeners in Minestom. */
    fun install() {
        val node = MinecraftServer.getGlobalEventHandler()

        node.addListener(PlayerPluginMessageEvent::class.java) { event ->
            val identifier = event.identifier
            if (identifier == CHANNEL_HANDSHAKE || identifier == CHANNEL_PLAY || identifier.startsWith("ac:")) {
                alpineUsers.add(event.player)
            }
        }
    }

    /** Returns true if the player is confirmed running Alpine Client. */
    fun isAlpineUser(player: Player): Boolean = alpineUsers.contains(player)

    /** Disables specific built-in Alpine mods for the player (e.g. FreeLook, Fullbright, Minimap). */
    fun setDisallowedMods(player: Player, mods: Map<String, Boolean>) {
        val jsonEntries = mods.entries.joinToString(",") { (mod, disabled) ->
            "\"$mod\":{\"disabled\":$disabled}"
        }
        val payload = """{"type":"disallowed_mods","data":{$jsonEntries}}""".toByteArray(StandardCharsets.UTF_8)
        player.sendPacket(PluginMessagePacket(CHANNEL_PLAY, payload))
    }

    /** Broadcasts a notification popup to Alpine users. */
    fun sendNotification(player: Player, title: String, message: String, durationMs: Int = 3000) {
        val payload = """{"type":"notification","title":"$title","message":"$message","duration":$durationMs}"""
            .toByteArray(StandardCharsets.UTF_8)
        player.sendPacket(PluginMessagePacket(CHANNEL_PLAY, payload))
    }
}
