package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBCImpl implements UserDao {
    private Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        Statement statement = null;

        try {
            statement = connection.createStatement();
            final String SQL = "CREATE TABLE if NOT EXISTS users "
                    + "(id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, username VARCHAR(20) NOT NULL, "
                    + "lastname VARCHAR(20), age INT NOT NULL)";
            statement.execute(SQL);
        } catch (SQLException e) {
            System.out.println("Такая таблица уже существует");
        }
    }

    public void dropUsersTable() {
        try {
            Statement statement = connection.createStatement();
            final String SQL = "DROP TABLE if EXISTS users";
            statement.execute(SQL);
        } catch (SQLException e) {
            System.out.println("Такой таблицы не существует");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            final String SQL = "INSERT INTO users (username, lastname, age) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement =
                    connection.prepareStatement(SQL);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void removeUserById(long id) {
        try {
            final String SQL = "DELETE FROM users WHERE id=?";
            PreparedStatement preparedStatement =
                    connection.prepareStatement(SQL);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            final String SQL = "SELECT * FROM users";
            ResultSet resultSet;
            resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("username"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
            System.out.println(users);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return users;
    }

    public void cleanUsersTable() {
        try {
            Statement statement = connection.createStatement();
            final String SQL = "DELETE FROM users";
            statement.execute(SQL);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
