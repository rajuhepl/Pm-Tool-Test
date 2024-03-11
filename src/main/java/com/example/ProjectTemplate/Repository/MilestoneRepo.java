package com.example.ProjectTemplate.Repository;

import com.example.ProjectTemplate.Model.Milestone;
import com.example.ProjectTemplate.Model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MilestoneRepo extends MongoRepository<Milestone,String> {
    Optional<Milestone> findByProject(Project project);
}
