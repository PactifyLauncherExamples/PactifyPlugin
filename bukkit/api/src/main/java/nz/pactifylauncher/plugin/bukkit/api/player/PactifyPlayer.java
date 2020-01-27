package nz.pactifylauncher.plugin.bukkit.api.player;

import org.bukkit.entity.Player;

/**
 * Class which represents a {@link Player} with the additional Pactify Plugin methods!
 */
public interface PactifyPlayer {
    /**
     * Returns the {@linkplain Player Bukkit Player} (cannot be null).
     *
     * @return the {@linkplain Player Bukkit Player}
     */
    Player getPlayer();

    /**
     * Check whether this player is using the Pactify Launcher.
     *
     * @return {@code true} if this player uses the Pactify Launcher; {@code false} otherwise
     */
    boolean hasLauncher();

    /**
     * Returns the used Pactify Launcher protocol version.
     *
     * @return the protocol version; or zero if this player does not use the Pactify Launcher
     */
    int getLauncherProtocolVersion();
}
