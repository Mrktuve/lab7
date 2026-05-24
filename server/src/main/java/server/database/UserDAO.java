package server.database;

import common.security.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private final Connection connection;

    public UserDAO(
            DatabaseManager dbManager
    ) {

        this.connection =
                dbManager.getConnection();
    }

    public boolean register(
            String login,
            String password
    ) {

        String sql =
                """
                INSERT INTO users
                (login, password_hash)
                VALUES (?, ?)
                """;

        try (
                PreparedStatement stmt =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            stmt.setString(1, login);

            stmt.setString(
                    2,
                    PasswordHasher
                            .hashPassword(
                                    password
                            )
            );

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            return false;
        }
    }

    public boolean login(
            String login,
            String password
    ) {

        String sql =
                """
                SELECT *
                FROM users
                WHERE login = ?
                AND password_hash = ?
                """;

        try (
                PreparedStatement stmt =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            stmt.setString(1, login);

            stmt.setString(
                    2,
                    PasswordHasher
                            .hashPassword(
                                    password
                            )
            );

            ResultSet rs =
                    stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            return false;
        }
    }
}