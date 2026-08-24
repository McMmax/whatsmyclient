package dev.terrorify.whatsmyclient.badlion

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerPluginMessageEvent
import net.minestom.server.network.packet.server.play.PluginMessagePacket
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap

/**
 * Badlion Client ModAPI Bridge.
 *
 * Implements communication channels for Badlion Client:
 * - `badlion:mods` / `badlion:modapi`: Mod enforcement, disabling disallowed client mods.
 * - `badlion:waypoints`: Server-managed waypoints and markers.
 * - `badlion:timers`: Visual countdowns and event timers.
 */
object BadlionProtocol {

    const val CHANNEL_MODS = "badlion:mods"
    const val CHANNEL_MODAPI = "badlion:modapi"
    const val CHANNEL_WAYPOINTS = "badlion:waypoints"
    const val CHANNEL_TIMERS = "badlion:timers"

    private val badlionUsers = ConcurrentHashMap.newKeySet<Player>()

    /** Wire up Badlion channel listeners in Minestom. */
    fun install() {
        val node = MinecraftServer.getGlobalEventHandler()

        node.addListener(PlayerPluginMessageEvent::class.java) { event ->
            val identifier = event.identifier
            if (identifier == CHANNEL_MODS || identifier == CHANNEL_MODAPI ||
                identifier.startsWith("badlion:")
            ) {
                badlionUsers.add(event.player)
            }
        }
    }

    /** Returns true if the player is confirmed running Badlion Client. */
    fun isBadlionUser(player: Player): Boolean = badlionUsers.contains(player)

    /** Disables specific built-in Badlion mods for the player while on the server. */
    fun setDisallowedMods(player: Player, mods: Map<String, Boolean>) {
        val jsonEntries = mods.entries.joinToString(",") { (mod, disabled) ->
            "\"$mod\":{\"disabled\":$disabled}"
        }
        val payload = "{$jsonEntries}".toByteArray(StandardCharsets.UTF_8)
        player.sendPacket(PluginMessagePacket(CHANNEL_MODS, payload))
    }

    /** Broadcasts a temporary or permanent 3D waypoint to Badlion users. */
    fun createWaypoint(player: Player, name: String, x: Int, y: Int, z: Int, r: Int = 255, g: Int = 255, b: Int = 255) {
        val payload = """{"name":"$name","x":$x,"y":$y,"z":$z,"r":$r,"g":$g,"b":$b}""".toByteArray(StandardCharsets.UTF_8)
        player.sendPacket(PluginMessagePacket(CHANNEL_WAYPOINTS, payload))
    }

    /** Creates a visual countdown timer on the Badlion user's HUD. */
    fun createTimer(player: Player, title: String, seconds: Int) {
        val payload = """{"title":"$title","time":$seconds}""".toByteArray(StandardCharsets.UTF_8)
        player.sendPacket(PluginMessagePacket(CHANNEL_TIMERS, payload))
    }
}
