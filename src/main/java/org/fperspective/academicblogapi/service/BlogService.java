package org.fperspective.academicblogapi.service;

import java.util.Collection;

import org.fperspective.academicblogapi.model.Blog;
import org.fperspective.academicblogapi.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public BlogService (@Autowired BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public Blog save(Blog blog){
        return blogRepository.save(blog);
    }

    public Collection<Blog> get() {
        return blogRepository.findAll();
    }

    public Blog get(String blogId) {
        return blogRepository.findById(blogId).orElse(null);
    }

    public void remove(String blogId) {
        blogRepository.deleteById(blogId);
    }

}
