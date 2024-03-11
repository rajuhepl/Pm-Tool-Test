package com.example.ProjectTemplate.Repository;

import com.example.ProjectTemplate.Model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends MongoRepository<Project,String> {
}
