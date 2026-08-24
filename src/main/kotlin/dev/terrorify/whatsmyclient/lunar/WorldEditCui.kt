package dev.terrorify.whatsmyclient.lunar

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerPluginMessageEvent
import net.minestom.server.network.packet.server.common.PluginMessagePacket
import java.nio.charset.StandardCharsets
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * The standard WorldEditCUI plugin-channel protocol (EngineHub WECUI).
 *
 * WorldEditCUI initiates the handshake by sending `v|<version>` to the server.
 * Once that has arrived, WorldEdit sends raw UTF-8 selection updates back on the
 * same channel: `s|cuboid`, then its `p|...` corner packets. Modern clients use
 * `worldedit:cui`; pre-1.13 clients use `WECUI`.
 */
internal object WorldEditCui {

    private const val MODERN_CHANNEL = "worldedit:cui"
    private const val LEGACY_CHANNEL = "WECUI"
    private val installed = AtomicBoolean()
    private val clientChannels = ConcurrentHashMap<UUID, String>()
    private val selections = ConcurrentHashMap<UUID, CuboidSelection>()

    /** Install the client-initiated CUI handshake listener exactly once. */
    fun install() {
        if (!installed.compareAndSet(false, true)) return

        val events = MinecraftServer.getGlobalEventHandler()
        events.addListener(PlayerPluginMessageEvent::class.java) { event ->
            val channel = event.identifier
            if (channel != MODERN_CHANNEL && channel != LEGACY_CHANNEL) return@addListener

            val message = String(event.message, StandardCharsets.UTF_8)
            if (!message.matches(Regex("v\\|\\d+"))) return@addListener

            clientChannels[event.player.uuid] = channel
            selections[event.player.uuid]?.let { sendSelection(event.player, channel, it) }
        }
        events.addListener(PlayerDisconnectEvent::class.java) { event ->
            clientChannels.remove(event.player.uuid)
            selections.remove(event.player.uuid)
        }
    }

    /**
     * Kept for source compatibility with existing callers. The client owns the
     * handshake, so there is intentionally no server-to-client registration here.
     */
    fun register(@Suppress("UNUSED_PARAMETER") player: Player) {
        install()
    }

    /** Draw the cuboid defined by two arbitrary corners (not pre-sorted). */
    fun showCuboid(player: Player, x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) {
        val volume = (Math.abs(x2 - x1) + 1).toLong() *
            (Math.abs(y2 - y1) + 1) * (Math.abs(z2 - z1) + 1)
        val selection = CuboidSelection(x1, y1, z1, x2, y2, z2, volume)
        selections[player.uuid] = selection
        clientChannels[player.uuid]?.let { sendSelection(player, it, selection) }
    }

    /** Clear the rendered selection (re-selecting the shape wipes its points). */
    fun clear(player: Player) {
        selections.remove(player.uuid)
        clientChannels[player.uuid]?.let { send(player, it, "s|cuboid") }
    }

    private fun sendSelection(player: Player, channel: String, selection: CuboidSelection) {
        send(player, channel, "s|cuboid")
        send(player, channel, "p|0|${selection.x1}|${selection.y1}|${selection.z1}|${selection.volume}")
        send(player, channel, "p|1|${selection.x2}|${selection.y2}|${selection.z2}|${selection.volume}")
    }

    private fun send(player: Player, channel: String, message: String) {
        player.sendPacket(PluginMessagePacket(channel, message.toByteArray(StandardCharsets.UTF_8)))
    }

    private data class CuboidSelection(
        val x1: Int, val y1: Int, val z1: Int,
        val x2: Int, val y2: Int, val z2: Int,
        val volume: Long,
    )
}
