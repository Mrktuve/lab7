package client.network;

import common.network.Request;
import common.network.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkClient {

    private final String host;
    private final int port;

    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Response sendRequest(Request request) {
        try (
                Socket socket = new Socket(host, port);

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            System.out.println("Connected to server");
            out.writeObject(request);
            out.flush();

            return (Response) in.readObject();

        } catch (Exception e) {
            return new Response(false, "Connection error: " + e.getMessage());
        }
    }
}