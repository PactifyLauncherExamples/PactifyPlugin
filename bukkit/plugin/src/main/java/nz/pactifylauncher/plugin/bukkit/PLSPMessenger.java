package nz.pactifylauncher.plugin.bukkit;

import lombok.RequiredArgsConstructor;
import nz.pactifylauncher.plugin.bukkit.util.PacketOutBuffer;
import org.bukkit.entity.Player;
import pactify.client.api.mcprotocol.util.NotchianPacketUtil;
import pactify.client.api.plsp.PLSPPacket;
import pactify.client.api.plsp.PLSPPacketHandler;
import pactify.client.api.plsp.PLSPProtocol;

import java.util.logging.Level;

@RequiredArgsConstructor
public class PLSPMessenger {
    private static final String PLSP_CHANNEL = "PLSP";

    private final PactifyPlugin plugin;

    public void enable() {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, PLSP_CHANNEL);
    }

    public void disable() {
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, PLSP_CHANNEL);
    }

    public void sendPLSPMessage(Player player, PLSPPacket<PLSPPacketHandler.ClientHandler> message) {
        try {
            PacketOutBuffer buf = new PacketOutBuffer();
            PLSPProtocol.PacketData<?> packetData = PLSPProtocol.getClientPacketByClass(message.getClass());
            NotchianPacketUtil.writeString(buf, packetData.getId(), Short.MAX_VALUE);
            message.write(buf);
            player.sendPluginMessage(plugin, PLSP_CHANNEL, buf.toBytes());
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Exception sending PLSP message to "
                    + (player != null ? player.getName() : "null") + ":", e);
        }
    }
}
