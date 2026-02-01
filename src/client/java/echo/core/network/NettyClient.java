package echo.core.network;

/**
 * Klient TCP/Netty do komunikacji z backendem cheata.
 */
public class NettyClient {

    private final String ip;
    private final int port;
    private boolean connected;

    public NettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void connect() {
        // Bootstrap Netty
        this.connected = true;
    }

    public void disconnect() {
        this.connected = false;
    }

    public void sendPacket(Object packet) {
        // Encode and send
    }
}