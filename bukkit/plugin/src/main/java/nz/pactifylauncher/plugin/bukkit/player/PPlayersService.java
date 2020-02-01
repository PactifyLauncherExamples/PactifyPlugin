package nz.pactifylauncher.plugin.bukkit.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nz.pactifylauncher.plugin.bukkit.PactifyPlugin;
import nz.pactifylauncher.plugin.bukkit.api.player.PactifyPlayer;
import nz.pactifylauncher.plugin.bukkit.api.player.PlayersService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

@RequiredArgsConstructor
public class PPlayersService implements PlayersService, Listener {
    private final @Getter PactifyPlugin plugin;
    private final Map<UUID, PPactifyPlayer> players = new HashMap<>();

    public void enable() {
        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // (Re-)Initialize already connected players
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PPactifyPlayer pactifyPlayer;
            players.put(player.getUniqueId(), pactifyPlayer = new PPactifyPlayer(this, player));
            try {
                pactifyPlayer.init();
                pactifyPlayer.join();
            } catch (Throwable t) {
                plugin.getLogger().log(Level.WARNING, "An error occurred initializing " + player.getName() + ":", t);
            }
        }
    }

    public void disable() {
        // Unregister events
        HandlerList.unregisterAll(this);
    }

    @Override
    public PPactifyPlayer getPlayer(UUID playerUid) {
        return players.get(playerUid);
    }

    @Override
    public PPactifyPlayer getPlayer(Player player) {
        return player == null ? null : getPlayer(player.getUniqueId());
    }

    @Override
    public boolean hasLauncher(UUID playerUid) {
        PactifyPlayer pactifyPlayer = getPlayer(playerUid);
        return pactifyPlayer != null && pactifyPlayer.hasLauncher();
    }

    @Override
    public boolean hasLauncher(Player player) {
        PactifyPlayer pactifyPlayer = getPlayer(player);
        return pactifyPlayer != null && pactifyPlayer.hasLauncher();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        // Store the hostname in a metadata to keep it between reloads
        event.getPlayer().setMetadata("PactifyPlugin:hostname", new FixedMetadataValue(plugin, event.getHostname()));

        // Then initialize the PactifyPlayer instance
        PPactifyPlayer pactifyPlayer;
        players.put(event.getPlayer().getUniqueId(), pactifyPlayer = new PPactifyPlayer(this, event.getPlayer()));
        pactifyPlayer.init();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLoginMonitor(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            // PlayerQuitEvent is not triggered if PlayerLoginEvent is disallowed
            playerQuit(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PPactifyPlayer pactifyPlayer = getPlayer(event.getPlayer());
        pactifyPlayer.join();
        pactifyPlayer.doJoinActions(pactifyPlayer.hasLauncher()
                ? plugin.getConf().getLoginWithPactify()
                : plugin.getConf().getLoginWithoutPactify());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerQuit(event.getPlayer());
    }

    private void playerQuit(Player player) {
        PPactifyPlayer pactifyPlayer = players.remove(player.getUniqueId());
        if (pactifyPlayer != null) {
            pactifyPlayer.free(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin() != plugin) {
            return;
        }

        // Free registered players
        // NOTE: This is not in the disable() method because we can't send Plugin Messages with an already disabled
        // plugin!
        for (Iterator<PPactifyPlayer> it = players.values().iterator(); it.hasNext(); ) {
            PPactifyPlayer pactifyPlayer = it.next();
            it.remove();
            try {
                pactifyPlayer.free(false);
            } catch (Throwable t) {
                plugin.getLogger().log(Level.WARNING, "An error occurred freeing "
                        + pactifyPlayer.getPlayer().getName() + ":", t);
            }
        }
    }
}
