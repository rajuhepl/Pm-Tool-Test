package com.example.ProjectTemplate.Repository;

import com.example.ProjectTemplate.Model.Milestone;
import com.example.ProjectTemplate.Model.TaskList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskListRepo extends MongoRepository<TaskList,String> {
    Optional<TaskList> findByMilestone(Milestone milestone);
}
