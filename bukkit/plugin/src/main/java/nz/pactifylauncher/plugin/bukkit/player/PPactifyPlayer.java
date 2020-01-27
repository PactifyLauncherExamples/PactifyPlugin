package nz.pactifylauncher.plugin.bukkit.player;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import nz.pactifylauncher.plugin.bukkit.api.player.PactifyPlayer;
import nz.pactifylauncher.plugin.bukkit.conf.Conf;
import nz.pactifylauncher.plugin.bukkit.util.SchedulerUtil;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Data
public class PPactifyPlayer implements PactifyPlayer {
    private static final Pattern PACTIFY_HOSTNAME_PATTERN = Pattern.compile("[\u0000\u0002]PAC([0-9A-F]{5})[\u0000\u0002]");

    private final PPlayersService service;
    private final Player player;
    private final Set<Integer> scheduledTasks = new HashSet<>();
    private int launcherProtocolVersion;

    public void init(boolean onJoin) {
        List<MetadataValue> hostnameMeta = player.getMetadata("PactifyPlugin:hostname");
        if (!hostnameMeta.isEmpty()) {
            String hostname = hostnameMeta.get(0).asString();
            Matcher m = PACTIFY_HOSTNAME_PATTERN.matcher(hostname);
            if (m.find()) {
                launcherProtocolVersion = Math.max(1, Integer.parseInt(m.group(1), 16));
            }
        } else {
            service.getPlugin().getLogger().warning("Unable to verify the launcher of " + player.getName()
                    + ": it probably logged when the plugin was disabled!");
        }
    }

    public void free(boolean onQuit) {
        SchedulerUtil.cancelTasks(service.getPlugin(), scheduledTasks);
    }

    public void doJoinActions(Conf.JoinActions actions) {
        if (actions != null) {
            doJoinActions(actions.getMessages(), player::sendMessage);
            doJoinActions(actions.getCommands(), action -> {
                service.getPlugin().getServer().dispatchCommand(
                        service.getPlugin().getServer().getConsoleSender(),
                        action.replace("{{name}}", player.getName())
                                .replace("{{uuid}}", player.getUniqueId().toString()));
            });
        }
    }

    private void doJoinActions(List<Conf.Action> actions, Consumer<String> handler) {
        if (actions != null) {
            for (Conf.Action action : actions) {
                if (action.getDelay() <= 0) {
                    handler.accept(action.getValue());
                } else {
                    SchedulerUtil.scheduleSyncDelayedTask(service.getPlugin(), scheduledTasks,
                            () -> handler.accept(action.getValue()), action.getDelay());
                }
            }
        }
    }

    @Override
    public boolean hasLauncher() {
        return launcherProtocolVersion > 0;
    }

    @Override
    public int getLauncherProtocolVersion() {
        return launcherProtocolVersion;
    }
}
