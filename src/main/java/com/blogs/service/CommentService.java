package com.blogs.service;

import com.blogs.model.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    void saveComment(Comment comment);

    void deleteComment(int commentId);

    List<Comment> findCommentsByPostId(int postId);

    Comment findCommentById(int commentId);

    void deleteCommentByPostId(int postId);
}
