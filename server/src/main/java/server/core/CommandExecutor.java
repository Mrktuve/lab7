package server.core;

import common.commands.*;
import common.model.Worker;
import common.network.Request;
import common.network.Response;
import server.auth.AuthManager;

public class CommandExecutor {

    private final CollectionManager manager;
    private final AuthManager authManager;

    public CommandExecutor(CollectionManager manager, AuthManager authManager) {
        this.manager = manager;
        this.authManager = authManager;
    }

    public Response execute(Request request) {
        Command command = request.getCommand();

        if (command == null) {
            return new Response(false, "Command is null");
        }

        String login = request.getLogin();
        String password = request.getPassword();


        if (command instanceof Register) {
            boolean success = authManager.register(login, password);
            return new Response(success, success ? "Registered" : "Registration failed");
        }


        if (command instanceof Login) {
            boolean success = authManager.login(login, password);
            return new Response(success, success ? "Logged in" : "Wrong login/password");
        }


        if (!authManager.authenticate(login, password)) {
            return new Response(false, "You are not authorized");
        }

        try {
            if (command instanceof Add add) {
                Worker worker = add.getWorker();
                worker.setOwnerLogin(login);

                boolean success = manager.add(worker);
                return new Response(success, success ? "Worker added" : "DB add error");
            }

            if (command instanceof Show) {
                StringBuilder sb = new StringBuilder();
                for (Worker worker : manager.getCollection()) {
                    sb.append(worker).append("\n");
                }
                return new Response(true, sb.toString());
            }

            if (command instanceof Info) {
                return new Response(true, "Collection type: " + manager
                                .getCollection()
                                .getClass()
                                .getSimpleName()
                                + "\nSize: " + manager.getCollection().size()
                );
            }

            if (command instanceof RemoveById cmd) {
                Worker worker = manager.findById(cmd.getId());
                if (worker == null) {
                    return new Response(false, "Worker not found");
                }

                if (!worker.getOwnerLogin().equals(login)) {
                    return new Response(false, "Not your object");
                }

                boolean removed = manager.removeById(cmd.getId());
                return new Response(removed, removed ? "Removed" : "Remove failed");
            }


            if (command instanceof UpdateId cmd) {
                Worker worker = cmd.getWorker();
                boolean updated = manager.update(cmd.getId(), worker, login);
                return new Response(updated, updated ? "Updated" : "Cannot update чужой объект");
            }

            if (command instanceof Clear) {
                manager.clearOwned(login);
                return new Response(true, "All your workers removed");
            }


            if (command instanceof RemoveLower cmd) {
                int removed = manager.removeLower(cmd.getWorker(), login);
                return new Response(true, "Removed: " + removed);
            }


            if (command instanceof RemoveAnyByStatus cmd) {
                boolean removed = manager.removeAnyByStatus(cmd.getStatus().name(), login);
                return new Response(removed, removed ? "Removed" : "No owned worker found");
            }


            if (command instanceof AddIfMax cmd) {
                Worker worker = cmd.getWorker();
                worker.setOwnerLogin(login);

                boolean added = manager.addIfMax(worker);
                return new Response(added, added ? "Added" : "Not max");
            }

            return new Response(false, "Unknown command");

        } catch (Exception e) {
            e.printStackTrace();

            return new Response(false, "Execution error: " + e.getMessage());
        }

    }
}