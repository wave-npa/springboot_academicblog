package org.fperspective.academicblogapi.repository;

import org.fperspective.academicblogapi.model.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends MongoRepository<Blog, String> {
    
}

