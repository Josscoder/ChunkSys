package net.chunkmind.chunksys.protocol.codec;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.chunkmind.chunksys.protocol.packet.IPacket;
import net.chunkmind.chunksys.protocol.packet.base.BatchPacket;
import net.chunkmind.chunksys.protocol.packet.base.MessagePacket;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ProtocolCodec {

    private static final Map<Byte, Class<? extends IPacket>> PACKETS = new HashMap<>();
    private static final Map<Class<? extends IPacket>, Consumer<IPacket>> RESPONSES = new HashMap<>();

    static {
        registerPacket(new MessagePacket(), new BatchPacket());
    }

    public static void registerPacket(IPacket...packets) {
        Arrays.stream(packets).forEach(packet -> PACKETS.put(packet.getPid(), packet.getClass()));
    }

    public static void unregisterPacket(byte pid) {
        PACKETS.remove(pid);
    }

    public static IPacket createPacketInstance(byte pid) {
        Class<? extends IPacket> clazz = PACKETS.get(pid);
        if (clazz != null) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    public static byte[] encodePacket(IPacket packet) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeByte(packet.getPid());

        packet.encode(output);

        return output.toByteArray();
    }

    public static IPacket decodePacket(byte[] bytes) {
        ByteArrayDataInput input = ByteStreams.newDataInput(bytes);

        byte packetPid = input.readByte();
        IPacket packet = createPacketInstance(packetPid);
        if (packet == null) {
            return null;
        }

        packet.decode(input);

        return packet;
    }

    public static void registerPacketResponse(Class<? extends IPacket> packet, Consumer<IPacket> response) {
        RESPONSES.put(packet, response);
    }

    public static void unregisterPacketResponse(Class<? extends IPacket> packet) {
        RESPONSES.remove(packet);
    }

    public static void handlePacketResponse(IPacket packet) {
        RESPONSES.forEach((key, value) -> {
            if (key.equals(packet.getClass())) {
                value.accept(packet);
            }
        });
    }
}
