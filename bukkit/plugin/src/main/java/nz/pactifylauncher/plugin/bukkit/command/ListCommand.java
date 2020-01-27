package nz.pactifylauncher.plugin.bukkit.command;

import lombok.RequiredArgsConstructor;
import nz.pactifylauncher.plugin.bukkit.PactifyPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ListCommand implements CommandExecutor {
    private final PactifyPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> pactifyList = new ArrayList<>();
        List<String> vanillaList = new ArrayList<>();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (plugin.getPlayersService().hasLauncher(player)) {
                pactifyList.add(player.getName());
            } else {
                vanillaList.add(player.getName());
            }
        }
        pactifyList.sort(String::compareToIgnoreCase);
        vanillaList.sort(String::compareToIgnoreCase);

        sender.sendMessage(ChatColor.YELLOW + "Players using the Pactify Launcher: "
                + (pactifyList.isEmpty() ? ChatColor.GRAY + "(none)" : ChatColor.GREEN + String.join(", ", pactifyList)));
        sender.sendMessage(ChatColor.YELLOW + "Players not using the Pactify Launcher: "
                + (vanillaList.isEmpty() ? ChatColor.GRAY + "(none)" : ChatColor.RED + String.join(", ", vanillaList)));
        return true;
    }
}
