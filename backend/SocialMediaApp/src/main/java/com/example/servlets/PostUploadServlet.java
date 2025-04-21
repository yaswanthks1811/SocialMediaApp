package com.example.servlets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.example.DAO.Posts.PostDaoImpl;
import org.example.DAO.Posts.PostException;
import org.example.service.PostService;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/api/uploadPost")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class PostUploadServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper(); 

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not logged in.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        Integer userId = (Integer) request.getSession().getAttribute("userId");

        String caption = request.getParameter("caption");
        String postType = request.getParameter("postType");

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not logged in.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        Part filePart = null;
        String filePath="";

        if ("Image".equalsIgnoreCase(postType)) {
            filePart = request.getPart("image");
        } else if ("Video".equalsIgnoreCase(postType)) {
            filePart = request.getPart("video");
        }

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            String uploadPath = getServletContext().getRealPath("/uploads");
            System.out.println(uploadPath);
            

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            // Save the file
            File savedFile = new File(uploadDir, fileName);
            filePart.write(savedFile.getAbsolutePath());

            filePath = "uploads/" + fileName;
            System.out.println("Saved to: " + filePath);
        }


        PostService postService = new PostService();
        try {
            boolean postCreated = postService.createPost(userId, caption, postType, filePath);
            
            if (postCreated) {
                Map<String,String> resp = new HashMap<>();
                resp.put("message", "Post created");
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(resp));
            } else {
            	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Cannot Upload Post.");
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            }
        } catch (Exception | PostException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to fetch Messages.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }

    }
}

