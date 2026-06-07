package server.core;

import common.model.Worker;
import server.database.WorkerDAO;

import java.util.Comparator;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CollectionManager {

    private final ConcurrentLinkedDeque<Worker> collection = new ConcurrentLinkedDeque<>();
    private final WorkerDAO workerDAO;

    public CollectionManager(WorkerDAO workerDAO) {
        this.workerDAO = workerDAO;
        //  заполняем память данными из БД
        loadCollection();
    }

    private void loadCollection() {
        collection.clear(); // очищаем текующую коллекцию
        collection.addAll(workerDAO.loadCollection()); // забирает все данные из БД
        System.out.println("Collection loaded: " + collection.size());
    }
    //возвращает ссылку на коллекцию
    public ConcurrentLinkedDeque<Worker> getCollection() {
        return collection;
    }

    // сохраняем в БД, если будет true, то сохраняем в локал коллекцию и возвращаем рез
    public boolean add(Worker worker) {
        boolean success = workerDAO.addWorker(worker);
        if (success) {
            collection.add(worker);
        }
        return success;
    }
    // создает последовательность, сортирует по айди, находит первый, но если ничего не найдено, то null
    public Worker findById(long id) {
        return collection.stream().filter(w -> w.getId() == id).findFirst().orElse(null);
    }
    // находит обьект, если нет, то вернет false
    public boolean removeById(long id, String login) {
        Worker worker = findById(id);
        if (worker == null || !worker.getOwnerLogin().equals(login)) {
            return false;
        }

        boolean success = workerDAO.removeWorkerById(id, login);
        if (success) {
            collection.remove(worker);
        }
        return success;
    }

    public void clearOwned(String login) {
        // удаляет все элементы, у которых ownerLogin совпадает с переданным
        collection.removeIf(worker -> worker.getOwnerLogin().equals(login));
    }

    public Worker getMaxWorker() {
        return collection.stream().max(Comparator.comparing(Worker::getSalary)).orElse(null);
    }

    public boolean update(long id, Worker newWorker, String login) {
        Worker oldWorker = findById(id);
        if (oldWorker == null || !oldWorker.getOwnerLogin().equals(login)) {
            return false;
        }

        newWorker.setId(id);
        newWorker.setOwnerLogin(login);

        boolean success = workerDAO.updateWorker(id, newWorker, login);
        if (success) {
            collection.remove(oldWorker);
            collection.add(newWorker);
        }
        return success;
    }

    public int removeLower(Worker compareWorker, String login) {
        int removed = workerDAO.removeLower(compareWorker.getSalary(), login);
        if (removed > 0) {
            collection.removeIf(w ->
                    w.getSalary() < compareWorker.getSalary()
                            && w.getOwnerLogin().equals(login)
            );
        }
        return removed;
    }
    public boolean removeAnyByStatus(String status, String login) {
        boolean success = workerDAO.removeAnyByStatus(status, login);
        if (success) {
            // удаляем первый найденный с таким статусом
            for (Worker w : collection) {
                if (w.getStatus().name().equalsIgnoreCase(status)
                        && w.getOwnerLogin().equals(login)) {
                    collection.remove(w);
                    break;
                }
            }
        }
        return success;
    }

    public boolean addIfMax(Worker worker) {
        Worker max = getMaxWorker();
        if (max != null && worker.getSalary() <= max.getSalary()) {
            return false;
        }
        boolean success = workerDAO.addWorker(worker);
        if (success) {
            collection.add(worker);
        }
        return success;
    }
}
