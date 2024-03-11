package com.example.ProjectTemplate.Repository;

import com.example.ProjectTemplate.Model.Task;
import com.example.ProjectTemplate.Model.TaskList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepo extends MongoRepository<Task,String> {
    Optional<Task> findByTaskList(TaskList taskList);
}
