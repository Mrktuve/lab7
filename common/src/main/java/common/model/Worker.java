package common.model;

import common.enums.Country;
import common.enums.EyeColor;
import common.enums.HairColor;
import common.enums.Status;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Worker implements Serializable {
    private String name;
    private Long id; // генерируется сервером
    private String ownerLogin; // не null, не пустой
    private Coordinates coordinates; // не null
    private LocalDateTime creationDate; // генерируется сервером
    private Double salary; // > 0
    private LocalDateTime startDate; // не null
    private LocalDateTime endDate; // может быть null
    private Status status; // может быть null
    private Person person; // может быть null
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Worker(String name,
                  Coordinates coordinates,
                  Double salary,
                  LocalDateTime startDate,
                  LocalDateTime endDate,
                  Status status,
                  Person person,
                  String ownerLogin) {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        if (salary == null || salary <= 0) {
            throw new IllegalArgumentException("Salary must be > 0");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("StartDate cannot be null");
        }

        this.name = name;
        this.coordinates = coordinates;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.person = person;
        this.ownerLogin = ownerLogin;
    }

    public static Worker fromResultSet(ResultSet rs) throws SQLException {
        Coordinates coords = null;
        try {
            coords = new Coordinates(
                    rs.getInt("coordinate_x"),
                    rs.getLong("coordinate_y")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Person person = new Person(
                rs.getLong("person_height"),
                EyeColor.valueOf(rs.getString("person_eye_color")),
                HairColor.valueOf(rs.getString("person_hair_color")),
                Country.valueOf(rs.getString("person_nationality"))
        );

        Worker worker = new Worker(
                rs.getString("name"),
                coords,
                rs.getDouble("salary"),
                rs.getTimestamp("start_date").toLocalDateTime(),
                rs.getTimestamp("end_date") != null ? rs.getTimestamp("end_date").toLocalDateTime() : null,
                Status.valueOf(rs.getString("status")),
                person,
                rs.getString("owner_login")
        );

        worker.setId(rs.getLong("id"));
        worker.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());

        return worker;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Double getSalary() {
        return salary;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Status getStatus() {
        return status;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate.format(FORMATTER) +
                ", salary=" + salary +
                ", startDate=" + (startDate != null ? startDate.format(FORMATTER) : "null") +
                ", endDate=" + (endDate != null ? endDate.format(FORMATTER) : "null") +
                ", status=" + status +
                ", person=" + person +
                ", owner='" + ownerLogin + '\'' +
                '}';
    }
}