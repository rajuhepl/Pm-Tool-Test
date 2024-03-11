package com.example.ProjectTemplate.RepositoryTemplate;

import com.example.ProjectTemplate.ModelTemplate.MilestoneTemplate;
import com.example.ProjectTemplate.ModelTemplate.ProjectTemplate;
import com.example.ProjectTemplate.Repository.MilestoneRepo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MilestoneTemplateRepo extends MongoRepository<MilestoneTemplate,String> {
    Optional<MilestoneTemplate> findByProjectTemplate(ProjectTemplate template);
}
