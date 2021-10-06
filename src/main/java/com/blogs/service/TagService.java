package com.blogs.service;

import com.blogs.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    List<Tag> getAllTags();
    List<Integer> saveTag(String tag);
    List<Tag> findByNameLike(String keyword);
}
