package server.network;

import common.network.Response;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ResponseSender {

    public void send(
            Socket socket,
            Response response
    ) {

        try {

            ObjectOutputStream out =
                    new ObjectOutputStream(
                            socket
                                    .getOutputStream()
                    );

            out.writeObject(
                    response
            );

            out.flush();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}