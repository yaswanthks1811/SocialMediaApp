package org.example.DAO.Admin;

import org.example.Bean.Admin;
import org.example.Bean.User;
import org.example.DAO.Users.UsersDaoImpl;

import java.sql.SQLException;

public interface AdminDAO {
    Boolean authenticate(Admin admin) throws SQLException, IllegalArgumentException;

    Boolean addAdmin(Admin admin) throws SQLException, AdminException;

    boolean changePassword(String adminUsername, String newPassword) throws SQLException, AdminException;

    Admin getAdminByUsername(String username) throws SQLException;
}
