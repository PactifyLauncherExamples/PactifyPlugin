package nz.pactifylauncher.plugin.bukkit;

import lombok.Getter;
import nz.pactifylauncher.plugin.bukkit.api.PactifyAPI;
import nz.pactifylauncher.plugin.bukkit.command.CheckCommand;
import nz.pactifylauncher.plugin.bukkit.command.ListCommand;
import nz.pactifylauncher.plugin.bukkit.command.StatsCommand;
import nz.pactifylauncher.plugin.bukkit.conf.Conf;
import nz.pactifylauncher.plugin.bukkit.conf.YamlConfProvider;
import nz.pactifylauncher.plugin.bukkit.player.PPlayersService;
import org.bukkit.plugin.java.JavaPlugin;

public class PactifyPlugin extends JavaPlugin implements PactifyAPI.Impl {
    private @Getter Conf conf;
    private final @Getter PPlayersService playersService = new PPlayersService(this);

    @Override
    public void onLoad() {
        PactifyAPI.setImplementation(this);
        conf = YamlConfProvider.load(this);
        System.out.println("conf=" + conf);
    }

    @Override
    public void onEnable() {
        playersService.enable();
        registerCommands();
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        playersService.disable();
        getLogger().info("Disabled!");
    }

    private void registerCommands() {
        getCommand("pactifycheck").setExecutor(new CheckCommand(this));
        getCommand("pactifylist").setExecutor(new ListCommand(this));
        getCommand("pactifystats").setExecutor(new StatsCommand(this));
    }
}
