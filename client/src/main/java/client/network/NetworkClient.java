package client.network;

import common.network.Request;
import common.network.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkClient {

    private final String host;
    private final int port;
    private Socket socket;  // ← Добавляем поле
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
        connect();  // ← Подключаемся сразу
    }

    // ← Метод подключения
    private void connect() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to server");
        } catch (Exception e) {
            System.err.println("Failed to connect: " + e.getMessage());
        }
    }

    public Response sendRequest(Request request) {
        try {
            // Проверяем соединение
            if (socket == null || socket.isClosed()) {
                connect();
            }

            out.writeObject(request);
            out.flush();

            Response response = (Response) in.readObject();
            return response;

        } catch (Exception e) {
            return new Response(false, "Connection error: " + e.getMessage());
        }
    }

    // ← Метод закрытия
    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Disconnected from server");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}