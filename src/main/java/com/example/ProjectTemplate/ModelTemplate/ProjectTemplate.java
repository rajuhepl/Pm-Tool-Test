package com.example.ProjectTemplate.ModelTemplate;

import com.example.ProjectTemplate.Model.Milestone;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "projecttemplate")
public class ProjectTemplate {
    @Id
    private String id;
    //private String projectId;
    private String name;
    private String desc;
    private LocalDate startDate;
    private LocalDate endDate;
    private long duration;
    private boolean isActive;
    private boolean deleteFlag;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String createdBy;
    private String updatedBy;

}
