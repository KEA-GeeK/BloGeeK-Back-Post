package Geek.Blog.repository;

import Geek.Blog.dto.BlogDTO;
import Geek.Blog.entity.Blog;

import java.util.Optional;

public interface BlogRepository {
    Blog create(BlogDTO blog);
    Optional<Blog> findById(Integer id);
    Blog edit(Blog blog);
    Integer deleteById(Integer id);
}