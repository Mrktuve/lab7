package server.network;

import common.network.Request;
import common.network.Response;
import server.core.CommandExecutor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetworkHandler {

    private final int port;
    private final CommandExecutor commandExecutor;
    private final RequestReader requestReader = new RequestReader();
    private final ResponseSender responseSender = new ResponseSender();
    private final ExecutorService readPool = Executors.newFixedThreadPool(4);
    private final ExecutorService sendPool = Executors.newCachedThreadPool();

    public ServerNetworkHandler(int port, CommandExecutor executor) {
        this.port = port;
        this.commandExecutor = executor;
    }

    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started: " + port);
            System.out.println("Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                readPool.submit(() -> {
                    Request request = requestReader.read(clientSocket);
                    if (request == null) {
                        return;
                    }

                    new Thread(() -> {

                        Response response = commandExecutor.execute(request);
                        sendPool.submit(() -> responseSender.send(clientSocket, response));

                    }).start();
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}