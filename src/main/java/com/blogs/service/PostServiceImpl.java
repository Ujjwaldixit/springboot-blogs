package com.blogs.service;

import com.blogs.model.Post;
import com.blogs.model.PostTag;
import com.blogs.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post savePost(Post post) {
        post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        if (post.getContent().length() >= 100) {
            post.setExcerpt(post.getContent().substring(0, 90));
        } else {
            post.setExcerpt(post.getContent());
        }
        return this.postRepository.save(post);
    }

    @Override
    public Post findPostById(int id) {
        return postRepository.getOne(id);
    }

    @Override
    public void deletePost(int id) {
        postRepository.deleteById(id);
    }

    @Override
    public Page<Post> findPostsWithPaginationAndSorting(int page, int pageSize, String sortingField, String sortingOrder) {
        page = page / pageSize;
        if (sortingOrder.equals("asc") || sortingOrder.equals("ASC") || sortingOrder.equals("Asc"))
            return postRepository.findAll(PageRequest.of(page, pageSize).withSort(Sort.by(Sort.Direction.ASC, sortingField)));
        return postRepository.findAll(PageRequest.of(page, pageSize).withSort(Sort.by(Sort.Direction.DESC, sortingField)));
    }

    @Override
    public List<Post> findPostsByKeyword(String keyword,Pageable pageable) {
        List<Post> posts = postRepository.findByExcerptLike(keyword,pageable).getContent();
        posts.addAll(postRepository.findByAuthorLike(keyword,pageable).getContent());
        posts.addAll(postRepository.findByTitleLike(keyword,pageable).getContent());
        posts.addAll(postRepository.findByContentLike(keyword,pageable).getContent());
        HashSet<Post> uniquePosts = new HashSet<>(posts);
        return new ArrayList<>(uniquePosts);
    }

    @Override
    public List<Post> findPostsByPostTag(List<PostTag> postTags) {
        List<Post> posts = new ArrayList<>();
        for (PostTag postTag : postTags) {
            posts.add(postRepository.getOne(postTag.getPostId()));
        }
        return posts;
    }

    @Override
    public List<Post> findPostsByAuthor(String author,Pageable pageable) {
        return postRepository.findByAuthor(author,pageable).getContent();
    }

    @Override
    public List<Post> findPostsByPublishedAt(Timestamp publishedAt,Pageable pageable) {
        return postRepository.findByPublishedAt(publishedAt,pageable).getContent();
    }

    @Override
    public List<Post> findPostWithPaginationAndSorting(Pageable pageable) {
        Slice<Post> slice=null;
        slice=postRepository.findAll(pageable);
        return slice.getContent();
    }




}
