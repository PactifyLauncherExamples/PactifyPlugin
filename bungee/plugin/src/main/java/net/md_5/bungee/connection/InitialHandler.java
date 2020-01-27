package net.md_5.bungee.connection;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.connection.PendingConnection;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * This class of the bungeecord-proxy package has been partially reproduced. It is excluded by maven-shade-plugin.
 * FIXME: Instead we need to add bungeecord-proxy to our dependencies (but we need to find a trusted repository).
 */
public class InitialHandler implements PendingConnection {
    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public int getVersion() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public InetSocketAddress getVirtualHost() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public ListenerInfo getListener() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public String getUUID() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public UUID getUniqueId() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void setUniqueId(UUID uuid) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public boolean isOnlineMode() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void setOnlineMode(boolean b) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public boolean isLegacy() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public InetSocketAddress getAddress() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void disconnect(String s) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void disconnect(BaseComponent... baseComponents) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void disconnect(BaseComponent baseComponent) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public boolean isConnected() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public Unsafe unsafe() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public String getExtraDataInHandshake() {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
