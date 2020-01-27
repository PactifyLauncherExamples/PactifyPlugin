package nz.pactifylauncher.plugin.bukkit.command;

import lombok.RequiredArgsConstructor;
import nz.pactifylauncher.plugin.bukkit.PactifyPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class CheckCommand implements CommandExecutor {
    private final PactifyPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /pacheck <player>");
            return true;
        }

        Player player;
        try {
            player = plugin.getServer().getPlayer(UUID.fromString(args[0]));
        } catch (IllegalArgumentException ignored) {
            player = plugin.getServer().getPlayer(args[0]);
        }
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player " + args[0] + " is not online");
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Player " + player.getName() + " is "
                + (plugin.getPlayersService().hasLauncher(player) ? ChatColor.GREEN + "using" : ChatColor.RED + "not using")
                + ChatColor.YELLOW + " the Pactify Launcher");
        return true;
    }
}
