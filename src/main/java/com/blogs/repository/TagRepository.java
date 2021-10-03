package com.blogs.repository;

import com.blogs.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag,Integer> {
    public Tag findByName(String tagName);
}
