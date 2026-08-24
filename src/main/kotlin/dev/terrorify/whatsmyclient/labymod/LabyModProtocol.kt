package dev.terrorify.whatsmyclient.labymod

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerPluginMessageEvent
import net.minestom.server.network.packet.server.play.PluginMessagePacket
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap

/**
 * LabyMod 3 & 4 Server Protocol Bridge.
 *
 * Facilitates native LabyMod feature communication over `labymod3:main` and `labymod:neo` channels.
 * Supports economy HUD integration, playing gamemode display, subtitles, markers, and feature permissions.
 */
object LabyModProtocol {

    const val CHANNEL_MAIN = "labymod3:main"
    const val CHANNEL_NEO = "labymod:neo"

    private val labymodUsers = ConcurrentHashMap.newKeySet<Player>()

    /** Wire up LabyMod channel listeners in Minestom. */
    fun install() {
        val node = MinecraftServer.getGlobalEventHandler()
        
        node.addListener(PlayerPluginMessageEvent::class.java) { event ->
            val identifier = event.identifier
            if (identifier == CHANNEL_MAIN || identifier == CHANNEL_NEO) {
                labymodUsers.add(event.player)
            }
        }
    }

    /** Returns true if the player is confirmed running LabyMod. */
    fun isLabyModUser(player: Player): Boolean = labymodUsers.contains(player)

    /** Send raw JSON message to LabyMod client. */
    fun sendLabyModMessage(player: Player, messageKey: String, jsonContent: String) {
        val payload = """{"key":"$messageKey","value":$jsonContent}"""
        val bytes = writeVarIntString(payload)
        val packet = PluginMessagePacket(CHANNEL_MAIN, bytes)
        player.sendPacket(packet)
    }

    /** Display or update player balance in LabyMod's native Economy HUD. */
    fun sendEconomyDisplay(player: Player, balance: Double, visible: Boolean = true, decimal: Boolean = false) {
        val json = """{"visible":$visible,"balance":$balance,"decimal":$decimal}"""
        sendLabyModMessage(player, "economy_display", json)
    }

    /** Set the current gamemode / subtitle displayed under the server name in LabyMod. */
    fun setServerGamemode(player: Player, gamemodeName: String) {
        val json = """{"show_gamemode":true,"gamemode_name":"$gamemodeName"}"""
        sendLabyModMessage(player, "server_gamemode", json)
    }

    /** Set floating subtitle above a player's head for LabyMod users. */
    fun setSubtitle(player: Player, targetUuid: String, subtitleText: String, size: Double = 0.8) {
        val json = """[{"uuid":"$targetUuid","size":$size,"text":"$subtitleText"}]"""
        sendLabyModMessage(player, "c_sub_title", json)
    }

    /** Enforce or grant specific LabyMod client feature permissions. */
    fun setPermissions(player: Player, permissions: Map<String, Boolean>) {
        val entries = permissions.entries.joinToString(",") { "\"${it.key}\":${it.value}" }
        sendLabyModMessage(player, "permissions", "{$entries}")
    }

    /** Encodes string prefixed with standard Minecraft VarInt length byte. */
    private fun writeVarIntString(string: String): ByteArray {
        val stringBytes = string.toByteArray(StandardCharsets.UTF_8)
        val buffer = java.io.ByteArrayOutputStream()
        var value = stringBytes.size
        while (true) {
            if ((value and 0x7F.inv()) == 0) {
                buffer.write(value)
                break
            }
            buffer.write((value and 0x7F) or 0x80)
            value = value ushr 7
        }
        buffer.write(stringBytes)
        return buffer.toByteArray()
    }
}
