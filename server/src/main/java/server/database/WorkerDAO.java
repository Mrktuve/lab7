package server.database;

import common.model.Worker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkerDAO {

    private final Connection connection;

    public WorkerDAO(DatabaseManager manager) {
        this.connection = manager.getConnection();
    }


    public List<Worker> loadCollection() {
        List<Worker> workers = new ArrayList<>();
        String sql = "SELECT * FROM workers";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                workers.add(Worker.fromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workers;
    }


    public boolean addWorker(Worker worker) {
        String sql = """
            INSERT INTO workers (
                name, coordinate_x, coordinate_y, creation_date,
                salary, start_date, end_date, status,
                person_height, person_eye_color, person_hair_color,
                person_nationality, owner_login
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            fillWorkerParams(stmt, worker);
            stmt.setString(13, worker.getOwnerLogin());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                worker.setId(rs.getLong("id"));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateWorker(long id, Worker worker, String ownerLogin) {
        String sql = """
            UPDATE workers SET
                name = ?, coordinate_x = ?, coordinate_y = ?,
                salary = ?, start_date = ?, end_date = ?,
                status = ?, person_height = ?, person_eye_color = ?,
                person_hair_color = ?, person_nationality = ?
            WHERE id = ? AND owner_login = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            fillWorkerParams(stmt, worker);
            stmt.setLong(12, id);
            stmt.setString(13, ownerLogin);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean removeWorkerById(long id, String ownerLogin) {
        String sql = "DELETE FROM workers WHERE id = ? AND owner_login = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.setString(2, ownerLogin);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean clearByOwner(String ownerLogin) {
        String sql = "DELETE FROM workers WHERE owner_login = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ownerLogin);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public int removeLower(double salary, String ownerLogin) {
        String sql = "DELETE FROM workers WHERE salary < ? AND owner_login = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, salary);
            stmt.setString(2, ownerLogin);

            return stmt.executeUpdate(); // возвращает количество удалённых строк
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public boolean removeAnyByStatus(String status, String ownerLogin) {
        String sql = """
            DELETE FROM workers
            WHERE id = (
                SELECT id FROM workers
                WHERE status = ? AND owner_login = ?
                LIMIT 1
            )
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, ownerLogin);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void fillWorkerParams(PreparedStatement stmt, Worker worker) throws SQLException {
        stmt.setString(1, worker.getName());
        stmt.setDouble(2, worker.getCoordinates().getX());
        stmt.setLong(3, worker.getCoordinates().getY());
        stmt.setTimestamp(4, Timestamp.valueOf(worker.getCreationDate()));
        stmt.setDouble(5, worker.getSalary());
        stmt.setTimestamp(6, Timestamp.valueOf(worker.getStartDate().atStartOfDay()));

        if (worker.getEndDate() != null) {
            stmt.setTimestamp(7, Timestamp.valueOf(worker.getEndDate()));
        } else {
            stmt.setNull(7, Types.TIMESTAMP);
        }

        stmt.setString(8, worker.getStatus().name());
        stmt.setInt(9, Math.toIntExact(worker.getPerson().getHeight()));
        stmt.setString(10, worker.getPerson().getEyeColor().name());
        stmt.setString(11, worker.getPerson().getHairColor().name());
        stmt.setString(12, worker.getPerson().getNationality().name());
    }
}