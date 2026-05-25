package server.network;

import common.network.Response;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ResponseSender {

    public void send(Socket socket, Response response) {

        try {
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(os);

            out.writeObject(response);
            out.flush();

            socket.shutdownOutput();
            socket.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}