package net.chunkmind.chunksys.protocol.packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public interface IPacket {

    byte getPid();

    void encode(ByteArrayDataOutput output);

    void decode(ByteArrayDataInput input);

    boolean isAsync();
}
