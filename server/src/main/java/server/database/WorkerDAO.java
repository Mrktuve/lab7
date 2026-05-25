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

    public boolean addWorker(Worker worker) {
        String sql = """
                INSERT INTO workers (
                name,
                coordinate_x,
                coordinate_y,
                creation_date,
                salary,
                start_date,
                end_date,
                status,
                person_height,
                person_eye_color,
                person_hair_color,
                person_nationality,
                owner_login
                )
                VALUES (
                ?,?,?,?,?,?,
                ?,?,?,?,?,?,
                ?
                )
                RETURNING id
                """;

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {

            stmt.setString(1, worker.getName());

            stmt.setDouble(2, worker.getCoordinates().getX());

            stmt.setLong(3, worker.getCoordinates().getY());

            stmt.setTimestamp(4, Timestamp.valueOf(worker.getCreationDate()));

            stmt.setDouble(5, worker.getSalary());

            stmt.setDate(6, Date.valueOf(String.valueOf(worker.getStartDate())));

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


    public List<Worker> loadCollection() {

        List<Worker> workers = new ArrayList<>();

        String sql = "SELECT * FROM workers";

        try (
                Statement stmt = connection.createStatement()
        ) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                Worker worker = Worker.fromResultSet(rs);

                workers.add(worker);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workers;
    }
}