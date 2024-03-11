package com.example.ProjectTemplate.RepositoryTemplate;

import com.example.ProjectTemplate.ModelTemplate.ProjectTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTemplateRepo extends MongoRepository<ProjectTemplate,String> {
}
