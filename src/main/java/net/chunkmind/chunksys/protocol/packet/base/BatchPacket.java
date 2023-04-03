package net.chunkmind.chunksys.protocol.packet.base;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import net.chunkmind.chunksys.protocol.ProtocolInfo;
import net.chunkmind.chunksys.protocol.codec.ProtocolCodec;
import net.chunkmind.chunksys.protocol.packet.IPacket;
import net.chunkmind.chunksys.protocol.packet.Packet;

import java.util.ArrayList;
import java.util.List;

public class BatchPacket extends Packet {

    @Getter
    private final List<Packet> batchOfPackets = new ArrayList<>();

    @Override
    public byte getPid() {
        return ProtocolInfo.BATCH_PACKET;
    }

    @Override
    public void encode(ByteArrayDataOutput output) {
        output.writeInt(batchOfPackets.size());
        batchOfPackets.forEach(packet -> {
            byte[] bytes = ProtocolCodec.encodePacket(packet);
            output.writeInt(bytes.length);
            output.write(bytes);
        });
    }

    @Override
    public void decode(ByteArrayDataInput input) {
        int packets = input.readInt();

        for (int i = 0; i < packets; i++) {
            byte[] bytes = new byte[input.readInt()];

            input.readFully(bytes);

            IPacket packet = ProtocolCodec.decodePacket(bytes);
            if (packet != null) {
                batchOfPackets.add((Packet) packet);
            }
        }
    }
}
