package com.example.ProjectTemplate.RepositoryTemplate;

import com.example.ProjectTemplate.ModelTemplate.SubTaskTemplate;
import com.example.ProjectTemplate.ModelTemplate.TaskTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SubTaskTemplateRepo extends MongoRepository<SubTaskTemplate,String> {
    List<SubTaskTemplate> findByTaskTemplateId(TaskTemplate taskTemplate);
  //  List<SubTaskTemplate> findByTaskTemplateTaskId(String id);
}
