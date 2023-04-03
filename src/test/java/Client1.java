import net.chunkmind.chunksys.client.ChunkClient;
import net.chunkmind.chunksys.settings.RabbitMQSettings;

public class Client1 {

    public static void main(String[] args) {
        ChunkClient chunkClient = new ChunkClient();
        chunkClient.startup(RabbitMQSettings.builder().build());
    }
}
