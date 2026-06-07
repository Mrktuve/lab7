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
    public boolean removeById(long id) {
        Worker worker = findById(id);
        if (worker == null) {
            return false;
        }
        // Удаляет из коллекции через ссылку на объект
        return collection.remove(worker);
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
        if (oldWorker == null) {
            return false;
        }
        if (!oldWorker.getOwnerLogin().equals(login)) {
            return false;
        }
        newWorker.setId(id);
        newWorker.setOwnerLogin(login);
        collection.remove(oldWorker);
        collection.add(newWorker);

        return true;
    }

    public int removeLower(Worker compareWorker, String login) {
        int removed = 0;
        collection.removeIf(worker -> worker.getSalary() < compareWorker.getSalary()
                && worker.getOwnerLogin().equals(login));

        return removed;
    }
    public boolean removeAnyByStatus(String status, String login) {

        Worker worker = collection.stream().filter(w -> w.getStatus().name().equalsIgnoreCase(status)
                && w.getOwnerLogin().equals(login)).findFirst().orElse(null);

        if (worker == null) {
            return false;
        }

        return collection.remove(worker);
    }
    public boolean addIfMax(Worker worker) {
        Worker max = getMaxWorker();

        if (max == null || worker.getSalary() > max.getSalary()) {
            return add(worker);
        }

        return false;
    }
}
