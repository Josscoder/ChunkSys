package net.chunkmind.chunksys.client;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.chunkmind.chunksys.ChunkService;
import net.chunkmind.chunksys.protocol.ProtocolInfo;
import net.chunkmind.chunksys.protocol.packet.IPacket;
import net.chunkmind.chunksys.settings.RabbitMQSettings;

import java.util.concurrent.CompletableFuture;

public class ChunkClient extends ChunkService {

    @Override
    public void startup(RabbitMQSettings rabbitMQSettings) {
        super.startup(rabbitMQSettings);

        subscribeChannel(ProtocolInfo.CLIENT_CHANNEL);
    }

    @CanIgnoreReturnValue
    public CompletableFuture<Boolean> sendPacketToClients(IPacket packet) {
        return super.sendPacket(ProtocolInfo.CLIENT_CHANNEL, packet);
    }

    @CanIgnoreReturnValue
    public CompletableFuture<Boolean> sendPacketToServer(IPacket packet) {
        return super.sendPacket(ProtocolInfo.SERVER_CHANNEL, packet);
    }
}
