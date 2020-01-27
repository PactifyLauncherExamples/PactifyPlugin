package nz.pactifylauncher.plugin.bukkit.command;

import lombok.RequiredArgsConstructor;
import nz.pactifylauncher.plugin.bukkit.PactifyPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class StatsCommand implements CommandExecutor {
    private final PactifyPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int onlineCnt = 0;
        int pactifyCnt = 0;
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            ++onlineCnt;
            if (plugin.getPlayersService().hasLauncher(player)) {
                ++pactifyCnt;
            }
        }

        sender.sendMessage(ChatColor.YELLOW + "Players using the Pactify Launcher: "
                + ChatColor.GREEN + pactifyCnt + ChatColor.YELLOW + "/" + onlineCnt);
        return true;
    }
}
