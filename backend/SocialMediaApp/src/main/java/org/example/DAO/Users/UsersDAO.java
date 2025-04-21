package org.example.DAO.Users;

import org.example.Bean.User;
import org.example.DAO.Users.UsersDaoImpl.DataIntegrityException;

import java.sql.SQLException;
import java.util.List;

public interface UsersDAO {

    boolean createUser(User user) throws DataIntegrityException, SQLException;

    List<User> getRecommendedUsers(int currentUserId) throws SQLException;

    boolean login(User user) throws DataIntegrityException, SQLException;

    User getUserByUsername(User user) throws SQLException;

    User getUserByUserId(User user) throws SQLException;

    boolean existsUser(String username);

    boolean existsEmail(String email);

    User getUserById(int user_id);

    boolean updateUser(int userId, User user);

    boolean deleteUser(int user_id) throws SQLException;

    List<User> getAllUsers();

    void updatePassword(int userId, String password);

    boolean banUser(int userId) throws SQLException;

    boolean unbanUser(int userId) throws SQLException;

    boolean isBanned(int userId);

    int getUserIdByUsername(String username);

    String getUsernameByUserId(int userId);
}
