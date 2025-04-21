package com.example.AdminServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Bean.Admin;
import org.example.service.AdminService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/admin/login") // Updated endpoint for API
public class AdminLoginServlet extends HttpServlet {
    private AdminService adminService;
    private ObjectMapper objectMapper; // Jackson ObjectMapper

    @Override
    public void init() {
        adminService = new AdminService();
        objectMapper = new ObjectMapper(); // Initialize Jackson
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            boolean auth = adminService.authAdmin(username, password);

            if (auth) {
                Admin admin = adminService.getAdminByUsername(username);
                HttpSession session = request.getSession();
                session.setAttribute("loggedInAdmin", admin);
                session.setMaxInactiveInterval(60 * 60 * 24);
                System.out.print(username+" "+password);

                // Prepare JSON response
                Map<String, Object> jsonResponse = new HashMap<>();
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Admin login successful.");
                jsonResponse.put("adminId", admin.getAdminUserId());
                jsonResponse.put("adminUsername", admin.getAdminUsername());

                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
            } else {
                // Failed login, return JSON error
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                Map<String, Object> jsonResponse = new HashMap<>();
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid admin username or password.");

                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Internal server error, return JSON
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "An error occurred during login.");

            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
        }
    }
}