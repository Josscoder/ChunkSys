package net.chunkmind.chunksys;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Getter;
import net.chunkmind.chunksys.protocol.codec.ProtocolCodec;
import net.chunkmind.chunksys.protocol.packet.IPacket;
import net.chunkmind.chunksys.protocol.packet.base.MessagePacket;
import net.chunkmind.chunksys.settings.RabbitMQSettings;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

@Getter
public abstract class ChunkService {

    protected Connection connection = null;
    protected Channel channel = null;

    public void startup(RabbitMQSettings rabbitMQSettings) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQSettings.getHost());
        factory.setPort(rabbitMQSettings.getPort());
        factory.setCredentialsProvider(rabbitMQSettings.getCredentialsProvider());

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        ProtocolCodec.registerPacketResponse(MessagePacket.class, packet -> {
            MessagePacket messagePacket = (MessagePacket) packet;
            System.out.println("Message received: " + messagePacket.message);
        });
    }

    public CompletableFuture<Boolean> sendPacket(String channelId, IPacket packet) {
        if (channel == null || !channel.isOpen()) {
            return CompletableFuture.completedFuture(false);
        }

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Runnable runnable = () -> {
            byte[] encodedPacket = ProtocolCodec.encodePacket(packet);
            try {
                channel.basicPublish("", channelId, null, encodedPacket);
                future.complete(true);
            } catch (IOException e) {
                future.completeExceptionally(e.getCause());
            }
        };

        if (packet.isAsync()) {
            CompletableFuture.runAsync(runnable);
        } else {
            runnable.run();
        }

        return future;
    }

    public void subscribeChannel(String channelId) {
        subscribeChannel(channelId, ProtocolCodec::handlePacketResponse);
    }

    public void subscribeChannel(String channelId, Consumer<IPacket> response) {
        try {
            channel.queueDeclare(channelId, false, false, false, null);
            channel.basicConsume(channelId, true, (consumerTag, delivery) -> {
                IPacket packet = ProtocolCodec.decodePacket(delivery.getBody());
                if (packet == null) {
                    return;
                }

                Runnable runnable = () -> response.accept(packet);;

                if (packet.isAsync()) {
                    CompletableFuture.runAsync(runnable);
                } else {
                    runnable.run();
                }

            }, consumerTag -> {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }

            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
