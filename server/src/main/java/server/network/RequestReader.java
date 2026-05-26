package server.network;

import common.network.Request;

import java.io.ObjectInputStream;
import java.net.Socket;

public class RequestReader {

    public Request read(Socket socket) {

        try {
            //создаёт поток ввода, обёртывая байтовый поток сокета в объект для чтения сериализованных данных
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //считывает след обьект и вовзращает методу
            return (Request) in.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}