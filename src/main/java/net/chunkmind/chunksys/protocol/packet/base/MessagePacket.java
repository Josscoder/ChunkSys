package net.chunkmind.chunksys.protocol.packet.base;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import lombok.Setter;
import net.chunkmind.chunksys.protocol.ProtocolInfo;
import net.chunkmind.chunksys.protocol.packet.Packet;

public class MessagePacket extends Packet {

    @Getter
    @Setter
    private String message;

    @Override
    public byte getPid() {
        return ProtocolInfo.MESSAGE_PACKET;
    }

    @Override
    public void encode(ByteArrayDataOutput output) {
        output.writeUTF(message);
    }

    @Override
    public void decode(ByteArrayDataInput input) {
        message = input.readUTF();
    }
}
