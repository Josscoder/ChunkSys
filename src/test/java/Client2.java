import net.chunkmind.chunksys.client.ChunkClient;
import net.chunkmind.chunksys.protocol.packet.Packet;
import net.chunkmind.chunksys.protocol.packet.base.BatchPacket;
import net.chunkmind.chunksys.protocol.packet.base.MessagePacket;
import net.chunkmind.chunksys.settings.RabbitMQSettings;

import java.util.ArrayList;
import java.util.List;

public class Client2 {

    public static void main(String[] args) {
        ChunkClient chunkClient = new ChunkClient();
        chunkClient.startup(RabbitMQSettings.builder().build());

        List<Packet> packets = new ArrayList<>();

        for (int i = 0; i <= 1000; i++) {
            MessagePacket messagePacket = new MessagePacket();
            messagePacket.setMessage("Hola mundo " + i);
            packets.add(messagePacket);
        }

        BatchPacket batchPacket = new BatchPacket();
        batchPacket.getBatchOfPackets().addAll(packets);

        chunkClient.sendPacketToServer(batchPacket);
    }
}
