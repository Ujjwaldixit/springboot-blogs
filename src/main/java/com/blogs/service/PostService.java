package com.blogs.service;

import com.blogs.model.Post;
import com.blogs.model.PostTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public interface PostService {
    List<Post> getAllPosts();

    Post savePost(Post post);

    Post findPostById(int id);

    void deletePost(int id);

    Page<Post> findPostsWithPaginationAndSorting(int page, int pageSize, String sortingField, String sortingOrder);

    List<Post> findPostsByKeyword(String keyword,Pageable pageable);

    List<Post> findPostsByPostTag(List<PostTag> postTags);

    List<Post> findPostsByAuthor(String author,Pageable pageable);

    List<Post> findPostsByPublishedAt(Timestamp publishedAt,Pageable pageable);

    List<Post> findPostWithPaginationAndSorting(Pageable pageable);
}
