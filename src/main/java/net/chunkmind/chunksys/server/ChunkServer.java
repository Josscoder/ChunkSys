package net.chunkmind.chunksys.server;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.chunkmind.chunksys.ChunkService;
import net.chunkmind.chunksys.protocol.ProtocolInfo;
import net.chunkmind.chunksys.protocol.packet.IPacket;
import net.chunkmind.chunksys.settings.RabbitMQSettings;

import java.util.concurrent.CompletableFuture;

public class ChunkServer extends ChunkService {

    @Override
    public void startup(RabbitMQSettings rabbitMQSettings) {
        super.startup(rabbitMQSettings);

        subscribeChannel(ProtocolInfo.SERVER_CHANNEL);
    }

    @CanIgnoreReturnValue
    public CompletableFuture<Boolean> sendPacket(IPacket packet) {
        return super.sendPacket(ProtocolInfo.CLIENT_CHANNEL, packet);
    }
}