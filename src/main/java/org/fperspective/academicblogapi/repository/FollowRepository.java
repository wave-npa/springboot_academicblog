package org.fperspective.academicblogapi.repository;

import org.fperspective.academicblogapi.model.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends MongoRepository<Follow, String> {
    
}
