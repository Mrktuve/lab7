package server;

import server.auth.AuthManager;
import server.core.CollectionManager;
import server.core.CommandExecutor;
import server.database.DatabaseManager;
import server.database.UserDAO;
import server.database.WorkerDAO;
import server.network.ServerNetworkHandler;

public class ServerMain {

    public static void main(
            String[] args
    ) {

        try {

            DatabaseManager db =
                    new DatabaseManager(
                            "s502880",
                            "lzCd,5009"
                    );

            db.connect();

            UserDAO userDAO =
                    new UserDAO(db);

            WorkerDAO workerDAO =
                    new WorkerDAO(db);

            AuthManager auth =
                    new AuthManager(
                            userDAO
                    );

            CollectionManager
                    manager =
                    new CollectionManager(
                            workerDAO
                    );

            CommandExecutor
                    executor =
                    new CommandExecutor(
                            manager,
                            auth
                    );

            ServerNetworkHandler
                    server =
                    new ServerNetworkHandler(
                            2222,
                            executor
                    );

            server.start();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}