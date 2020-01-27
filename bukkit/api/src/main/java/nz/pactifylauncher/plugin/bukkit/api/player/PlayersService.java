package nz.pactifylauncher.plugin.bukkit.api.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

/**
 * Note: All of these players-related methods only work from the {@link PlayerLoginEvent} (after the LOWEST priority).
 * So don't try to call them during the {@link AsyncPlayerPreLoginEvent}!
 */
public interface PlayersService {
    /**
     * Returns the {@link PactifyPlayer} related to a player.
     *
     * @param playerUid UUID of the player
     * @return the {@link PactifyPlayer} related to the player online with this UUID; or {@code null} if no player is
     * online with this UUID
     */
    PactifyPlayer getPlayer(UUID playerUid);

    /**
     * Returns the {@link PactifyPlayer} related to a player.
     *
     * @param player player
     * @return the {@link PactifyPlayer} related to this player; or {@code null} if this player is not online
     */
    PactifyPlayer getPlayer(Player player);

    /**
     * Check whether a player is using the Pactify launcher.
     *
     * @param playerUid UUID of the player to check
     * @return {@code true} if a player is online with this UUID and uses the Pactify Launcher; {@code false} otherwise
     */
    boolean hasLauncher(UUID playerUid);

    /**
     * Check whether a player is using the Pactify launcher.
     *
     * @param player player to check
     * @return {@code true} if the player is online and uses the Pactify Launcher; {@code false} otherwise
     */
    boolean hasLauncher(Player player);
}
