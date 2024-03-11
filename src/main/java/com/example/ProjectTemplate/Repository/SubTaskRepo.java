package com.example.ProjectTemplate.Repository;

import com.example.ProjectTemplate.Model.SubTask;
import com.example.ProjectTemplate.Model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubTaskRepo extends MongoRepository<SubTask,String> {
    List<SubTask> findByTaskId(Task task);
}
