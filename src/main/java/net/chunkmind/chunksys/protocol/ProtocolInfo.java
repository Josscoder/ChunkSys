package net.chunkmind.chunksys.protocol;

public interface ProtocolInfo {

    String CLIENT_CHANNEL = "chunksys-client";
    String SERVER_CHANNEL = "chunksys-server";

    byte MESSAGE_PACKET = 0x01;
    byte BATCH_PACKET = 0x02;
}
