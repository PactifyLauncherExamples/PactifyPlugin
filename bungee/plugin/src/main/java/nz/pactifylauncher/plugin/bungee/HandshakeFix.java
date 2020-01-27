package nz.pactifylauncher.plugin.bungee;

import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.event.EventHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * When IP Forwarding is enabled, BungeeCord don't send the "extra data in handshake" to the spigot server...
 * But it is required to detect the Pactify Launcher from Spigot. So we fix that!
 */
public class HandshakeFix implements Listener {
    private static final Pattern PACTIFY_HOSTNAME_PATTERN = Pattern.compile("\u0000(PAC[0-9A-F]{5})\u0000");

    @EventHandler
    public void onPlayerHandshake(PlayerHandshakeEvent event) {
        InitialHandler con = (InitialHandler) event.getConnection();
        Matcher m = PACTIFY_HOSTNAME_PATTERN.matcher(con.getExtraDataInHandshake());
        if (m.find()) {
            // send the Pactify Launcher handshake using \u0002 instead of \u0000 (so that will not break IP forwarding)
            event.getHandshake().setHost(event.getHandshake().getHost() + "\u0002" + m.group(1) + "\u0002");
        }
    }
}
