package nz.pactifylauncher.plugin.bungee;

import net.md_5.bungee.api.plugin.Plugin;

public class PactifyPlugin extends Plugin {
    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, new HandshakeFix());
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled!");
    }
}
