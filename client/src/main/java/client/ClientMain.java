package client;

import client.auth.ClientAuthManager;
import client.input.CommandBuilder;
import client.input.ConsoleReader;
import client.network.NetworkClient;
import common.commands.Command;
import common.network.Request;
import common.network.Response;


public class ClientMain {

    public static void main(String[] args) {


        NetworkClient client = new NetworkClient("localhost", 2222);

        ClientAuthManager auth = new ClientAuthManager();

        ConsoleReader reader = new ConsoleReader();
        CommandBuilder builder = new CommandBuilder(reader);

        System.out.println(
                "Type register or login"
        );

        while (true) {

            try {
                Command command = builder.build();
                if (command == null) {
                    continue;
                }

                String commandName = command.getName();

                if (commandName.equals("register") || commandName.equals("login")) {

                    System.out.print("Login: ");
                    String login = reader.readLine();

                    System.out.print("Password: ");
                    String password = reader.readLine();



                    Request request = new Request(command, login, password);

                    Response response = client.sendRequest(request);

                    System.out.println(response.getMessage());

                    if (response.isSuccess()) {
                        auth.setCredentials(login, password);

                        System.out.println("Authorized");
                    }

                    continue;
                }


                if (!auth.isAuthorized()) {

                    System.out.println("Please login first");

                    continue;
                }

                Request request = new Request(command, auth.getLogin(), auth.getPassword());

                Response response = client.sendRequest(request);

                System.out.println(response.getMessage());

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}