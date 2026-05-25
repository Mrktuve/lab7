package server.network;

import common.network.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class RequestReader {

    public Request read(Socket socket) {
        try {

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            return (Request)
                    in.readObject();

        } catch (
                IOException | ClassNotFoundException e
        ) {
            return null;
        }
    }
}