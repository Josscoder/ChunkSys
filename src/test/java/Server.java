import net.chunkmind.chunksys.server.ChunkServer;
import net.chunkmind.chunksys.settings.RabbitMQSettings;

public class Server {

    public static void main(String[] args) {
        ChunkServer chunkServer = new ChunkServer();
        chunkServer.startup(RabbitMQSettings.builder().build());
    }
}
