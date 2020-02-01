package nz.pactifylauncher.plugin.bukkit.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import pactify.client.api.mcprotocol.AbstractNotchianPacketBuffer;

public class PacketOutBuffer extends AbstractNotchianPacketBuffer<PacketOutBuffer> {
    private final ByteArrayDataOutput handle = ByteStreams.newDataOutput();

    @Override
    public PacketOutBuffer writeBytes(byte[] src) {
        handle.write(src);
        return this;
    }

    @Override
    public PacketOutBuffer writeBytes(byte[] src, int offset, int len) {
        handle.write(src, offset, len);
        return this;
    }

    @Override
    public PacketOutBuffer writeBoolean(boolean value) {
        handle.writeBoolean(value);
        return this;
    }

    @Override
    public PacketOutBuffer writeByte(int value) {
        handle.writeByte(value);
        return this;
    }

    @Override
    public PacketOutBuffer writeShort(int value) {
        handle.writeShort(value);
        return this;
    }

    @Override
    public PacketOutBuffer writeInt(int value) {
        handle.writeInt(value);
        return this;
    }

    @Override
    public PacketOutBuffer writeLong(long value) {
        handle.writeLong(value);
        return this;
    }

    @Override
    public PacketOutBuffer writeFloat(float value) {
        handle.writeFloat(value);
        return this;
    }

    @Override
    public PacketOutBuffer writeDouble(double value) {
        handle.writeDouble(value);
        return this;
    }

    @Override
    public PacketOutBuffer writeVarInt(int value) {
        while ((value & -128) != 0) {
            handle.writeByte(value & 127 | 128);
            value >>>= 7;
        }
        handle.writeByte(value);
        return this;
    }

    @Override
    public PacketOutBuffer writeVarLong(long value) {
        while ((value & -128L) != 0L) {
            this.writeByte((int) (value & 127L) | 128);
            value >>>= 7;
        }
        this.writeByte((int) value);
        return this;
    }

    @Override
    public PacketOutBuffer writeByteArray(byte[] bytes) {
        this.writeVarInt(bytes.length);
        this.writeBytes(bytes);
        return this;
    }

    @Override
    public PacketOutBuffer writeVarIntArray(int[] ints) {
        this.writeVarInt(ints.length);
        for (int value : ints) {
            this.writeVarInt(value);
        }
        return this;
    }

    @Override
    public PacketOutBuffer writeLongArray(long[] longs) {
        this.writeVarInt(longs.length);
        for (long value : longs) {
            this.writeLong(value);
        }
        return this;
    }

    public byte[] toBytes() {
        return handle.toByteArray();
    }
}
