package com.example.ProjectTemplate.RepositoryTemplate;

import com.example.ProjectTemplate.ModelTemplate.MilestoneTemplate;
import com.example.ProjectTemplate.ModelTemplate.TaskListTemplate;
import com.example.ProjectTemplate.ModelTemplate.TaskTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TaskTemplateRepo extends MongoRepository<TaskTemplate,String> {
    //@Query("{'taskListTemplate': ?0}")
    Optional<TaskTemplate> findByTaskListTemplate(TaskListTemplate taskListTemplate);
}
