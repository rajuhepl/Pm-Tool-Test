package com.example.ProjectTemplate.RepositoryTemplate;

import com.example.ProjectTemplate.ModelTemplate.MilestoneTemplate;
import com.example.ProjectTemplate.ModelTemplate.TaskListTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TaskListTemplateRepo extends MongoRepository<TaskListTemplate,String> {
    Optional<TaskListTemplate> findByMilestoneTemplate(MilestoneTemplate milestoneTemplate);
}
