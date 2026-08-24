package dev.terrorify.whatsmyclient

import dev.terrorify.whatsmyclient.alpine.AlpineProtocol
import dev.terrorify.whatsmyclient.badlion.BadlionProtocol
import dev.terrorify.whatsmyclient.feather.FeatherProtocol
import dev.terrorify.whatsmyclient.features.ClientFeature
import dev.terrorify.whatsmyclient.features.GlobalModEnforcer
import dev.terrorify.whatsmyclient.labymod.LabyModProtocol
import dev.terrorify.whatsmyclient.lunar.WorldEditCui
import dev.terrorify.whatsmyclient.norisk.NoRiskProtocol
import net.minestom.server.entity.Player

/**
 * Universal client-capability API. Minestom-native today; the provider seam
 * is engineered to extend across Spigot and Fabric backends.
 *
 * Supported Clients & Ecosystems:
 * - **Lunar Client**: Selection visuals via WorldEditCUI (`worldedit:cui`) and Apollo hooks.
 * - **Badlion Client**: Mod disallow enforcement, custom HUD timers, and waypoints (`badlion:mods`).
 * - **Feather Client**: Mod enforcement, custom Discord RPC, and client detection (`feather:client`).
 * - **LabyMod**: Native economy displays, server gamemode headers, subtitles, and permissions (`labymod3:main`).
 * - **Alpine Client (Ridge)**: Mod enforcement, notifications, and HUD cooldowns (`ac:play`).
 * - **NoRisk Client**: Mod enforcement and server notifications (`nrc:play`).
 */
object WhatsMyClient {

    /** Wire up all client provider hooks. Call once on server boot. */
    @JvmStatic
    fun init() {
        WorldEditCui.install()
        LabyModProtocol.install()
        BadlionProtocol.install()
        FeatherProtocol.install()
        AlpineProtocol.install()
        NoRiskProtocol.install()
    }

    /** Detected client for a player. */
    @JvmStatic
    fun clientType(player: Player): ClientType {
        return when {
            LabyModProtocol.isLabyModUser(player) -> ClientType.LABYMOD
            BadlionProtocol.isBadlionUser(player) -> ClientType.BADLION
            FeatherProtocol.isFeatherUser(player) -> ClientType.FEATHER
            AlpineProtocol.isAlpineUser(player) -> ClientType.ALPINE
            NoRiskProtocol.isNoRiskUser(player) -> ClientType.NORISK
            else -> ClientType.UNKNOWN
        }
    }

    // ── Universal Global Mod Enforcement ───────────────────────────────────────────────────

    /** Disallow one or more universal features globally across all supported client APIs. */
    @JvmStatic
    fun disableFeatures(vararg features: ClientFeature) {
        GlobalModEnforcer.disable(*features)
    }

    /** Re-enable universal features globally. */
    @JvmStatic
    fun enableFeatures(vararg features: ClientFeature) {
        GlobalModEnforcer.enable(*features)
    }

    /** Enforce all registered global rules onto the player's client. */
    @JvmStatic
    fun enforceRules(player: Player) {
        GlobalModEnforcer.enforce(player)
    }

    // ── Selection visuals (Lunar Client / WorldEdit CUI) ───────────────────────────────────

    /** Show a cuboid selection box defined by two corners. */
    @JvmStatic
    fun showSelection(player: Player, x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) {
        WorldEditCui.showCuboid(player, x1, y1, z1, x2, y2, z2)
    }

    /** Hide any active selection box. */
    @JvmStatic
    fun clearSelection(player: Player) {
        WorldEditCui.clear(player)
    }

    // ── LabyMod Integrations ───────────────────────────────────────────────────────────────

    /** Update or display balance in LabyMod's native Economy HUD. */
    @JvmStatic
    fun sendLabyEconomy(player: Player, balance: Double, visible: Boolean = true, decimal: Boolean = false) {
        LabyModProtocol.sendEconomyDisplay(player, balance, visible, decimal)
    }

    /** Set the gamemode or state displayed under server title in LabyMod. */
    @JvmStatic
    fun setLabyGamemode(player: Player, gamemodeName: String) {
        LabyModProtocol.setServerGamemode(player, gamemodeName)
    }

    /** Set a floating subtitle above a target player's head for LabyMod users. */
    @JvmStatic
    fun setLabySubtitle(player: Player, targetUuid: String, subtitleText: String) {
        LabyModProtocol.setSubtitle(player, targetUuid, subtitleText)
    }

    // ── Badlion Client Integrations ─────────────────────────────────────────────────────────

    /** Disable specific client mods for Badlion users (e.g., Minimap, Schematica, Waypoints). */
    @JvmStatic
    fun setBadlionDisallowedMods(player: Player, mods: Map<String, Boolean>) {
        BadlionProtocol.setDisallowedMods(player, mods)
    }

    /** Render a server-managed waypoint on Badlion Client. */
    @JvmStatic
    fun createBadlionWaypoint(player: Player, name: String, x: Int, y: Int, z: Int, r: Int = 255, g: Int = 255, b: Int = 255) {
        BadlionProtocol.createWaypoint(player, name, x, y, z, r, g, b)
    }

    /** Render a visual HUD countdown timer on Badlion Client. */
    @JvmStatic
    fun createBadlionTimer(player: Player, title: String, seconds: Int) {
        BadlionProtocol.createTimer(player, title, seconds)
    }

    // ── Feather Client Integrations ─────────────────────────────────────────────────────────

    /** Disable specific client mods for Feather users (e.g., FreeLook, Fullbright, Waypoints). */
    @JvmStatic
    fun setFeatherDisallowedMods(player: Player, mods: Map<String, Boolean>) {
        FeatherProtocol.setDisallowedMods(player, mods)
    }

    /** Update player's Discord Rich Presence on Feather Client. */
    @JvmStatic
    fun setFeatherDiscordRpc(player: Player, state: String, details: String) {
        FeatherProtocol.setDiscordRpc(player, state, details)
    }

    // ── Alpine Client (Ridge) Integrations ──────────────────────────────────────────────────

    /** Send an in-game notification popup to Alpine users. */
    @JvmStatic
    fun sendAlpineNotification(player: Player, title: String, message: String, durationMs: Int = 3000) {
        AlpineProtocol.sendNotification(player, title, message, durationMs)
    }

    // ── NoRisk Client Integrations ─────────────────────────────────────────────────────────

    /** Send an in-game notification popup to NoRisk users. */
    @JvmStatic
    fun sendNoRiskNotification(player: Player, title: String, message: String) {
        NoRiskProtocol.sendNotification(player, title, message)
    }
}
