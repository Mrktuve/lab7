package server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionAcceptor {
    private final ServerSocket serverSocket;

    // Конструктор - требует порт
    public ConnectionAcceptor(int port) throws IOException {
        // Создаёт и привязывает сокет к указанному порту
        this.serverSocket = new ServerSocket(port);
        System.out.println("Сервер работает на этом порту " + port);
    }

    // Метод ожидания входящего соединения
    public Socket accept() throws IOException {
        // Клиент подключается, возвращает новый Socket
        Socket client = serverSocket.accept();
        // client.getInetAddress() - IP-адрес подключившегося клиента
        System.out.println("Подключенный клиент: " + client.getInetAddress());
        // возвращает сокет
        return client;
    }

    public void close() throws IOException {
        // освобождает порт
        serverSocket.close();
    }
}