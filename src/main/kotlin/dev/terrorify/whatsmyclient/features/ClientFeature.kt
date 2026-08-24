package dev.terrorify.whatsmyclient.features

/**
 * Universal catalog of client features, HUD modules, and enhancements.
 * Maps cross-client names into a unified, server-authoritative rule set.
 */
enum class ClientFeature(
    val badlionName: String? = null,
    val featherName: String? = null,
    val labyModPermission: String? = null,
    val alpineName: String? = null,
    val noRiskName: String? = null
) {
    MINIMAP(
        badlionName = "MiniMap",
        featherName = "MiniMap",
        labyModPermission = "gui_minimap",
        alpineName = "minimap",
        noRiskName = "minimap"
    ),
    FREELOOK(
        badlionName = "Freelook",
        featherName = "FreeLook",
        labyModPermission = "perspective_360",
        alpineName = "freelook",
        noRiskName = "freelook"
    ),
    WAYPOINTS(
        badlionName = "Waypoints",
        featherName = "Waypoints",
        labyModPermission = "waypoints",
        alpineName = "waypoints",
        noRiskName = "waypoints"
    ),
    SCHEMATICA(
        badlionName = "Schematica",
        featherName = "Schematica",
        labyModPermission = "schematica",
        alpineName = "schematica",
        noRiskName = null
    ),
    FULLBRIGHT(
        badlionName = "Fullbright",
        featherName = "Fullbright",
        labyModPermission = "gamma",
        alpineName = "fullbright",
        noRiskName = "fullbright"
    ),
    HEALTH_TAGS(
        badlionName = "NameTags",
        featherName = "NameTags",
        labyModPermission = "name_tags",
        alpineName = "healthtags",
        noRiskName = "healthtags"
    ),
    INVENTORY_PREVIEW(
        badlionName = "InventoryPreview",
        featherName = "InventoryPreview",
        labyModPermission = "inventory",
        alpineName = "invpreview",
        noRiskName = null
    ),
    COOLDOWNS(
        badlionName = "Cooldowns",
        featherName = "Cooldowns",
        labyModPermission = "cooldowns",
        alpineName = "cooldowns",
        noRiskName = "cooldowns"
    ),
    AUTO_TEXT(
        badlionName = "AutoText",
        featherName = null,
        labyModPermission = "chat_autotext",
        alpineName = null,
        noRiskName = null
    );
}
