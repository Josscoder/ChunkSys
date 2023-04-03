import net.chunkmind.chunksys.client.ChunkClient;
import net.chunkmind.chunksys.protocol.packet.base.MessagePacket;
import net.chunkmind.chunksys.settings.RabbitMQSettings;

public class Client2 {

    public static void main(String[] args) {
        ChunkClient chunkClient = new ChunkClient();
        chunkClient.startup(RabbitMQSettings.builder().build());

        chunkClient.sendPacketToClients(new MessagePacket(){{
            message = "Hola mundo clients";
        }});

        chunkClient.sendPacketToServer(new MessagePacket(){{
            message = "Hola mundo server";
        }});
    }
}
