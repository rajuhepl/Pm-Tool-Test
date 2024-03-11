package com.example.ProjectTemplate.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "subtask")
public class SubTask {
    @Id
    private String id;
   // private String subtaskId;
    private String subtaskName;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
    private boolean deleteFlag;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String createdBy;
    private String updatedBy;
    @DocumentReference
    private Task task;

    public void setProjectId(String projectId) {

    }
}
