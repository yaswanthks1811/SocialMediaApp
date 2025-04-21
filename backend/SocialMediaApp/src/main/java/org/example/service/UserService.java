package org.example.service;

import org.example.Bean.Post;
import org.example.Bean.User;
import org.example.DAO.Posts.PostDaoImpl;
import org.example.DAO.Users.UsersDaoImpl;
import org.example.Utilities.PasswordUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {
    UsersDaoImpl usersDao=new UsersDaoImpl();
    PostDaoImpl postDao=new PostDaoImpl();
    public boolean registerUser(String username,String password,String email) throws SQLException, UsersDaoImpl.DataIntegrityException {
        if (usersDao.existsUser(username) || usersDao.existsEmail(email)){
            System.out.println("Username or Email Already exists.");
            return false;
        }
        String hashedPass= PasswordUtils.hashPassword(password);
        User user=new User.Builder().username(username)
                .password(hashedPass)
                .email(email)
                .accountStatus("ACTIVE")
                .build();
        return usersDao.createUser(user);
       
    }
    public boolean login(String username,String password) throws UsersDaoImpl.DataIntegrityException, SQLException {
        User user=new User.Builder().username(username).password(password).build();
        return usersDao.login(user);
    }

    public User getUser(String username) throws SQLException {
        User user=new User.Builder().username(username).build();
        return usersDao.getUserByUsername(user);
    }
    public User getUser(int user_id) throws SQLException {
        User user=new User.Builder().userId(user_id).build();
        return usersDao.getUserByUserId(user);

    }

    public boolean update(int userId, User user) {
        return usersDao.updateUser(userId,user);
    }

    public  List<User> searchUsersByRegex(String pattern, int currentUserId) {
        List<User> matchedUsers = new ArrayList<>();
        List<User> allUsers = usersDao.getAllUsers();
        try {
            Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            for (User user : allUsers) {
                if (user.getUserId() != currentUserId) {
                    Matcher matcher = regex.matcher(user.getUsername());
                    if (matcher.find()) {
                        matchedUsers.add(user);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid regex: " + e.getMessage());
        }

        return matchedUsers;
    }
    public List<User> getRecommendations(int userId) throws SQLException {
        return usersDao.getRecommendedUsers(userId);
    }


    public void updatePassword(int userId, String password) {
        usersDao.updatePassword(userId,PasswordUtils.hashPassword(password));
    }
    public String getUsernameByUserId(int userId){
    	return usersDao.getUsernameByUserId(userId);
    }
    
    public int getUserIdByUsername(String username){
    	return usersDao.getUserIdByUsername(username);
    }
    public List<User> getAllUser(){
    	return usersDao.getAllUsers();
    }
    public boolean deleteUser(int userId) throws SQLException{
    	return usersDao.deleteUser(userId);
    }
    public boolean banUser(int userId) throws SQLException{
    	return usersDao.banUser(userId);
    }
    public boolean unbanUser(int userId) throws SQLException{
    	return usersDao.unbanUser(userId);
    }
    
    public boolean isBanned(int userId){
    	return usersDao.isBanned(userId);
    }
	
}
