package org.example.service;

import org.example.Bean.Admin;
import org.example.Bean.Post;
import org.example.Bean.User;
import org.example.DAO.Admin.AdminDaoImpl;
import org.example.DAO.Admin.AdminException;
import org.example.DAO.Posts.PostDaoImpl;
import org.example.DAO.Users.UsersDaoImpl;
import org.example.Utilities.PasswordUtils;

import java.sql.SQLException;
import java.util.List;

public class AdminService {
    AdminDaoImpl adminDao=new AdminDaoImpl();
    UsersDaoImpl usersDao=new UsersDaoImpl();
    PostDaoImpl postDao=new PostDaoImpl();
    public Boolean authAdmin(String username,String password) throws SQLException {
        Admin admin= new Admin.Builder().adminUsername(username).adminPassword(password).build();
        return adminDao.authenticate(admin);
    }

    public boolean addAdmin(String username, String password) throws AdminException, SQLException {
        Admin admin = new Admin.Builder().adminUsername(username).adminPassword(PasswordUtils.hashPassword(password)).build();
        return adminDao.addAdmin(admin);
    }

    public User userReport(int user_id) {
        return usersDao.getUserById(user_id);
    }


    public boolean removePost(int postId) throws SQLException {
        return postDao.deletePost(postId);

    }

    public List<Post> getPostsById(int userId) {
        return postDao.getPostListOfUser(userId);
    }

    public boolean changePassword(String adminUsername, String newPassword) throws AdminException, SQLException {
        return adminDao.changePassword(adminUsername,newPassword);
    }

    public boolean banUser(int userId) throws SQLException {
        return usersDao.banUser(userId);
    }
    public Admin getAdminByUsername(String username) throws SQLException{
    	return adminDao.getAdminByUsername(username);
    }
}
