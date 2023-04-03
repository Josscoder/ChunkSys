package net.chunkmind.chunksys.settings;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.CredentialsProvider;
import com.rabbitmq.client.impl.DefaultCredentialsProvider;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RabbitMQSettings {

    @Builder.Default
    private final String host = ConnectionFactory.DEFAULT_HOST;

    @Builder.Default
    private final int port = ConnectionFactory.USE_DEFAULT_PORT;

    @Builder.Default
    private final CredentialsProvider credentialsProvider = new DefaultCredentialsProvider(
            ConnectionFactory.DEFAULT_USER,
            ConnectionFactory.DEFAULT_PASS
    );
}
