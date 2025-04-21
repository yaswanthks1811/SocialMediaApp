package com.example.AdminServlets;


import com.example.DTO.PostDTO;
import com.fasterxml.jackson.databind.ObjectMapper;


import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.example.Bean.Post;
import org.example.DAO.Comments.CommentException;
import org.example.DAO.Likes.LikeException;
import org.example.DAO.Posts.PostDaoImpl;
import org.example.service.CommentService;
import org.example.service.FollowerService;
import org.example.service.LikeService;
import org.example.service.PostService;
import org.example.service.UserService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/admin/post")
public class AdminGetPostServlet extends HttpServlet {
	private UserService userService;
    private PostService postService;
    private FollowerService followerService;
    private LikeService likeService;
    private CommentService commentService;
    private ObjectMapper objectMapper = new ObjectMapper(); // Initialize Jackson

    public void init() {
        userService = new UserService();
        postService = new PostService();
        followerService = new FollowerService();
        likeService = new LikeService();
        commentService = new CommentService();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String postIdParam = request.getParameter("postId");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (postIdParam == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"postId parameter is required\"}");
                return;
            }

            int postId = Integer.parseInt(postIdParam);
            Post post = postService.getPostById(postId);
            PostDTO dto = new PostDTO();
            dto.setPostId(post.getPostId());
            dto.setPostType(post.getPostType());
            dto.setCaption(post.getPostCaption());
            dto.setDateTime(post.getCreatedAt());
            dto.setUserId(post.getUserId());
            dto.setUsername(userService.getUser(post.getUserId()).getUsername());
            dto.setLikeCount(likeService.likeCount(post.getPostId()));
            dto.setComments(commentService.getCommentsDTO(post.getPostId()));
            dto.setMediaPath(post.getFilePath());
            

            if (post != null) {
                objectMapper.writeValue(response.getWriter(), new ResponseWrapper(dto));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Post not found\"}");
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid postId format\"}");
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LikeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    static class ResponseWrapper {
        public PostDTO post;

        public ResponseWrapper(PostDTO post) {
            this.post = post;
        }
    }
}
