package com.blogs.service;

import com.blogs.model.Comment;
import com.blogs.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(int commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<Comment> findCommentsByPostId(int postId) {
        return commentRepository.getCommentByPostId(postId);
    }

    @Override
    public Comment findCommentById(int commentId) {
        return commentRepository.getOne(commentId);
    }

    @Override
    public void deleteCommentByPostId(int postId) {
        commentRepository.deleteByPostId(postId);
    }
}
