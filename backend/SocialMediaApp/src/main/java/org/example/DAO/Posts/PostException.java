package org.example.DAO.Posts;

public class PostException extends Throwable {
    public PostException(String s) {
        System.out.println("Something wrong in post!");
    }
}
