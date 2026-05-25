package server.network;

import common.network.Response;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ResponseSender {

    public void send(Socket socket, Response response) {

        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.writeObject(response);
            out.flush();

            socket.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}