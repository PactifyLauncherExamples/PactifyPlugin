package nz.pactifylauncher.plugin.bukkit.api;

import lombok.experimental.UtilityClass;
import nz.pactifylauncher.plugin.bukkit.api.player.PlayersService;

/**
 * Pactify Launcher API for Bukkit.
 */
@UtilityClass
public class PactifyAPI {
    private static Impl implementation;

    /**
     * @deprecated You certainly don't want to use this method.
     */
    @Deprecated
    public static void setImplementation(Impl implementation) {
        PactifyAPI.implementation = implementation;
    }

    /**
     * Returns the {@linkplain PlayersService players service}; containing players-related API methods.
     *
     * @return Returns the {@linkplain PlayersService players service}
     */
    public static PlayersService players() {
        return implementation.getPlayersService();
    }

    public interface Impl {
        PlayersService getPlayersService();
    }
}
