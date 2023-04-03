package net.chunkmind.chunksys.protocol.packet;

import net.chunkmind.chunksys.protocol.packet.annotation.AsyncPacket;

public abstract class Packet implements IPacket {

    @Override
    public boolean isAsync() {
        return getClass().isAnnotationPresent(AsyncPacket.class);
    }
}
