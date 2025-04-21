package com.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.DTO.PostDTO;
import org.example.DAO.Comments.CommentException;
import org.example.DAO.Posts.PostDaoImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/trending")
public class TrendingServlet extends HttpServlet {

    private PostDaoImpl postDAO = new PostDaoImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<PostDTO> trendingPosts = new ArrayList<>();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not logged in.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        try {
            trendingPosts = postDAO.getTrendingPosts();
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(trendingPosts));
        } catch (CommentException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve trending posts.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}