package dev.terrorify.whatsmyclient.features

import dev.terrorify.whatsmyclient.ClientType
import dev.terrorify.whatsmyclient.WhatsMyClient
import dev.terrorify.whatsmyclient.alpine.AlpineProtocol
import dev.terrorify.whatsmyclient.badlion.BadlionProtocol
import dev.terrorify.whatsmyclient.feather.FeatherProtocol
import dev.terrorify.whatsmyclient.labymod.LabyModProtocol
import dev.terrorify.whatsmyclient.norisk.NoRiskProtocol
import net.minestom.server.entity.Player
import java.util.concurrent.CopyOnWriteArraySet

/**
 * Universal Mod Enforcer.
 *
 * Allows server administrators to maintain a single, cross-client list of disallowed
 * or enforced features. Translates universal [ClientFeature] rules into client-specific
 * payloads (Badlion, Feather, LabyMod, Alpine, NoRisk, Lunar) automatically.
 */
object GlobalModEnforcer {

    private val disabledFeatures = CopyOnWriteArraySet<ClientFeature>()

    /** Mark one or more universal features as disallowed. */
    fun disable(vararg features: ClientFeature) {
        disabledFeatures.addAll(features)
    }

    /** Re-enable one or more universal features. */
    fun enable(vararg features: ClientFeature) {
        disabledFeatures.removeAll(features.toSet())
    }

    /** Returns all currently disallowed features. */
    fun getDisallowedFeatures(): Set<ClientFeature> = disabledFeatures.toSet()

    /**
     * Enforce the global mod rules onto a player.
     * Identifies the player's client and sends the tailored disallow payload.
     */
    fun enforce(player: Player) {
        val client = WhatsMyClient.clientType(player)

        // 1. Badlion Client
        if (client == ClientType.BADLION || BadlionProtocol.isBadlionUser(player)) {
            val map = mutableMapOf<String, Boolean>()
            for (feature in disabledFeatures) {
                feature.badlionName?.let { map[it] = true }
            }
            if (map.isNotEmpty()) {
                BadlionProtocol.setDisallowedMods(player, map)
            }
        }

        // 2. Feather Client
        if (client == ClientType.FEATHER || FeatherProtocol.isFeatherUser(player)) {
            val map = mutableMapOf<String, Boolean>()
            for (feature in disabledFeatures) {
                feature.featherName?.let { map[it] = true }
            }
            if (map.isNotEmpty()) {
                FeatherProtocol.setDisallowedMods(player, map)
            }
        }

        // 3. LabyMod
        if (client == ClientType.LABYMOD || LabyModProtocol.isLabyModUser(player)) {
            val map = mutableMapOf<String, Boolean>()
            for (feature in disabledFeatures) {
                feature.labyModPermission?.let { map[it] = false } // false = disallowed
            }
            if (map.isNotEmpty()) {
                LabyModProtocol.setPermissions(player, map)
            }
        }

        // 4. Alpine Client (Ridge)
        if (client == ClientType.ALPINE || AlpineProtocol.isAlpineUser(player)) {
            val map = mutableMapOf<String, Boolean>()
            for (feature in disabledFeatures) {
                feature.alpineName?.let { map[it] = true }
            }
            if (map.isNotEmpty()) {
                AlpineProtocol.setDisallowedMods(player, map)
            }
        }

        // 5. NoRisk Client
        if (client == ClientType.NORISK || NoRiskProtocol.isNoRiskUser(player)) {
            val map = mutableMapOf<String, Boolean>()
            for (feature in disabledFeatures) {
                feature.noRiskName?.let { map[it] = true }
            }
            if (map.isNotEmpty()) {
                NoRiskProtocol.setDisallowedMods(player, map)
            }
        }
    }
}
