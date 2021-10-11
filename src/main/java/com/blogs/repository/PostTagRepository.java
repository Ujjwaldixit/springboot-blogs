package com.blogs.repository;

import com.blogs.model.PostAndTagIdentity;
import com.blogs.model.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, PostAndTagIdentity> {
    List<PostTag> findPostTagByPostId(int postId);

    List<PostTag> findPostTagByTagId(int tagId);
}
