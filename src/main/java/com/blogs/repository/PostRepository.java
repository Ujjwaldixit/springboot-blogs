package com.blogs.repository;

import com.blogs.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByAuthorLike(String keyword);

    List<Post> findByTitleLike(String keyword);

    List<Post> findByContentLike(String keyword);

    List<Post> findByExcerptLike(String keyword);

    List<Post> findByAuthorId(int authorId);

    List<Post> findByPublishedAt(Timestamp publishedAt);

    @Query(value = "select * from blog.post where to_char(published_at,'HH24:MI')=:time",nativeQuery = true)
    List<Post> findPostByTime(@Param("time") String time);

    @Query(value = "select * from blog.post where date(published_at)=:date",nativeQuery = true)
    List<Post> findPostByDate(@Param("date") String date);
}
