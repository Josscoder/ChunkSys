import net.chunkmind.chunksys.protocol.codec.ProtocolCodec;
import net.chunkmind.chunksys.protocol.packet.base.BatchPacket;
import net.chunkmind.chunksys.protocol.packet.base.MessagePacket;
import net.chunkmind.chunksys.server.ChunkServer;
import net.chunkmind.chunksys.settings.RabbitMQSettings;

public class Server {

    public static void main(String[] args) {
        ChunkServer chunkServer = new ChunkServer();
        chunkServer.startup(RabbitMQSettings.builder().build());

        ProtocolCodec.registerPacketResponse(MessagePacket.class, packet -> {
            MessagePacket messagePacket = (MessagePacket) packet;

            System.out.println("Message received: " + messagePacket.getMessage());
        });

        ProtocolCodec.registerPacketResponse(BatchPacket.class, packet -> {
            BatchPacket batchPacket = (BatchPacket) packet;

            batchPacket.getBatchOfPackets().forEach(msgPacket -> {
                MessagePacket messagePacket = (MessagePacket) msgPacket;
                System.out.println("Message received: " + messagePacket.getMessage());
            });
        });
    }
}
